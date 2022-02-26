package parsso.idman.repoImpls.users.usersOprations.retrieve.subClass;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.UserAttributeMapper;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repoImpls.users.usersOprations.retrieve.helper.SkyroomAccess;
import parsso.idman.repoImpls.users.usersOprations.retrieve.helper.UserRole;

import javax.naming.directory.SearchControls;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class FullAttributes {
    final LdapTemplate ldapTemplate;
    final MongoTemplate mongoTemplate;
    final String BASE_DN;

    public FullAttributes(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, String BASE_DN) {
        this.ldapTemplate = ldapTemplate;
        this.mongoTemplate = mongoTemplate;
        this.BASE_DN = BASE_DN;
    }

    public List<User> get() {
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        final AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass", "person"));

        List<User> people = ldapTemplate.search("ou=People," + BASE_DN, andFilter.toString(), searchControls, new UserAttributeMapper(mongoTemplate)
        );
        List<User> relatedPeople = new LinkedList<>();

        for (User user : people) {
            if (user != null && user.getDisplayName() != null) {
                relatedPeople.add(user);
            }

        }

        Collections.sort(relatedPeople);

        return relatedPeople;
    }

    public User get(String userId) {
        userId = userId.toLowerCase();

        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

        User user = new User();
        UsersExtraInfo usersExtraInfo;
        List<User> people = ldapTemplate.search("ou=People," + BASE_DN, new EqualsFilter("uid", userId).encode(), searchControls, new UserAttributeMapper(mongoTemplate));

        if (people.size() != 0) {
            user = people.get(0);
            Query query = new Query(Criteria.where("_id").is(user.get_id().toString()));
            usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class, Variables.col_usersExtraInfo);
            user.setUsersExtraInfo(mongoTemplate.findOne(query, UsersExtraInfo.class, Variables.col_usersExtraInfo));
            try {
                user.setUnDeletable(Objects.requireNonNull(usersExtraInfo).isUnDeletable());
            } catch (Exception e) {
                user.setUnDeletable(false);
            }

        }

        user.setSkyroomAccess(new SkyroomAccess(mongoTemplate).get(user));

        if (user.getRole() == null)
            user =  UserRole.set(user);

        if (user.getRole().equals("USER") && !Boolean.parseBoolean(new Settings(mongoTemplate).retrieve(Variables.USER_PROFILE_ACCESS).getValue()))
            user.setProfileInaccessibility(true);

        if (user.get_id() == null)
            return null;

        else return user;
    }


}
