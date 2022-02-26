package parsso.idman.repoImpls.users.usersOprations.retrieve.subClass;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.OrFilter;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.SimpleUserAttributeMapper;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repoImpls.users.usersOprations.retrieve.helper.MongoQuery;
import parsso.idman.repoImpls.users.usersOprations.retrieve.helper.UsersCount;
import parsso.idman.repos.SystemRefresh;

import javax.naming.directory.SearchControls;
import java.util.List;

public class MainAttributes {
    final MongoTemplate mongoTemplate;
    LdapTemplate ldapTemplate;
    SystemRefresh systemRefresh;
    final String BASE_DN;

    public MainAttributes(MongoTemplate mongoTemplate, LdapTemplate ldapTemplate, String base_dn) {
        this.mongoTemplate = mongoTemplate;
        this.ldapTemplate = ldapTemplate;
        BASE_DN = base_dn;
    }

    public MainAttributes(MongoTemplate mongoTemplate, SystemRefresh systemRefresh, String base_dn) {
        this.mongoTemplate = mongoTemplate;
        this.systemRefresh = systemRefresh;
        BASE_DN = base_dn;
    }

    public List<UsersExtraInfo> get(int page, int number) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        String[] array = {"uid", "displayName", "ou", "createtimestamp", "pwdAccountLockedTime"};
        searchControls.setReturningAttributes(array);

        int skip = (page - 1) * number;

        List<UsersExtraInfo> usersExtraInfos;

        if (page == -1 && number == -1)
            usersExtraInfos = mongoTemplate.find(new Query(), UsersExtraInfo.class, Variables.col_usersExtraInfo);
        else
            usersExtraInfos = mongoTemplate.find(new Query().skip(skip).limit(number), UsersExtraInfo.class, Variables.col_usersExtraInfo);

        OrFilter orFilter = new OrFilter();

        for (UsersExtraInfo usersExtraInfo : usersExtraInfos)
            orFilter.or(new EqualsFilter("uid", usersExtraInfo.get_id().toString()));

        return ldapTemplate.search("ou=People," + BASE_DN, orFilter.encode(), searchControls, new SimpleUserAttributeMapper());

    }

    public UsersExtraInfo get(String userId) {

        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        return ldapTemplate.search("ou=People," + BASE_DN, new EqualsFilter("uid", userId).encode(), searchControls, new SimpleUserAttributeMapper()).get(0);

    }

    public User.ListUsers get(int page, int nCount, String sortType, String groupFilter, String searchUid, String searchDisplayName, String mobile, String userStatus) {

        new Thread(() -> systemRefresh.refreshLockedUsers()).start();

        int skip = (page - 1) * nCount;

        Query query = MongoQuery.builder(groupFilter, searchUid, searchDisplayName, mobile, userStatus);

        switch (sortType) {
            case "":
            case "uid_m2M":
                query.with(Sort.by(Sort.Direction.ASC, "userId"));
                break;
            case "uid_M2m":
                query.with(Sort.by(Sort.Direction.DESC, "userId"));
                break;
            case "displayName_m2M":
                query.with(Sort.by(Sort.Direction.ASC, "displayName"));
                break;
            case "displayName_M2m":
                query.with(Sort.by(Sort.Direction.DESC, "displayName"));
                break;
        }

        List<UsersExtraInfo> userList = mongoTemplate.find(query.skip(skip).limit(nCount),
                UsersExtraInfo.class, Variables.col_usersExtraInfo);

        int size = new UsersCount(mongoTemplate).mongoSize(groupFilter, searchUid, searchDisplayName, mobile, userStatus);

        return new User.ListUsers(size, userList, (int) Math.ceil((double) size / (double) nCount));


    }
}
