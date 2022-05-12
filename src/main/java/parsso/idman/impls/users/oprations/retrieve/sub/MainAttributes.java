package parsso.idman.impls.users.oprations.retrieve.sub;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.OrFilter;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.SimpleUserAttributeMapper;
import parsso.idman.impls.users.oprations.retrieve.helper.MongoQuery;
import parsso.idman.impls.users.oprations.retrieve.helper.UsersCount;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.SystemRefresh;

import javax.naming.directory.SearchControls;

import java.util.LinkedList;
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

  public List<UsersExtraInfo> get(int page, int number, String sort) {
    Query query = new Query();
    if(sort.equalsIgnoreCase("role")){
      query.addCriteria(Criteria.where("_id").exists(true)).with(Sort.by(Sort.Direction.ASC,"roleClass._id"));
    }
    if (page == -1 && number == -1){
      return mongoTemplate.find(query, UsersExtraInfo.class, Variables.col_usersExtraInfo);
    }
    else
      return mongoTemplate.find(query.skip((page - 1) * number).limit(number), UsersExtraInfo.class,
          Variables.col_usersExtraInfo);
  }

  public UsersExtraInfo get(String userId) {

    return mongoTemplate.findOne(new Query(Criteria.where("_id").is(userId)), UsersExtraInfo.class,
        Variables.col_usersExtraInfo);

  }

  public User.ListUsers get(int page, int nCount, String sortType, String groupFilter, String searchUid,
      String searchDisplayName, String mobile, String userStatus) {

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
      default:
        break;
    }

    List<UsersExtraInfo> userList = mongoTemplate.find(query.skip(skip).limit(nCount),
        UsersExtraInfo.class, Variables.col_usersExtraInfo);

    int size = new UsersCount(mongoTemplate).mongoSize(groupFilter, searchUid, searchDisplayName, mobile,
        userStatus);

    return new User.ListUsers(size, userList, (int) Math.ceil((double) size / (double) nCount));

  }
}
