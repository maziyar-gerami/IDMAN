package parsso.idman.impls.users.oprations.retrieve.sub;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.LdapTemplate;
import parsso.idman.helpers.Variables;
import parsso.idman.impls.users.oprations.retrieve.helper.MongoQuery;
import parsso.idman.impls.users.oprations.retrieve.helper.UsersCount;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.models.users.User.ListUsers;

import java.util.List;

public class MainAttributes {
  final MongoTemplate mongoTemplate;
  LdapTemplate ldapTemplate;

  public MainAttributes(MongoTemplate mongoTemplate, LdapTemplate ldapTemplate) {
    this.mongoTemplate = mongoTemplate;
    this.ldapTemplate = ldapTemplate;
  }

  public MainAttributes(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public ListUsers get(int page, int number, String sort, String role, String userId, String displayName) {
    Query query = new Query();

    if (!userId.equals("")) {
      query.addCriteria(Criteria.where("_id").regex(userId));
    }

    if (!displayName.equals("")) {
      query.addCriteria(Criteria.where("displayName").regex(displayName));
    }

    if (!role.equals("")) {
      query.addCriteria(Criteria.where("roleClass.role").is(role.toUpperCase()));
    }

    if (sort.equalsIgnoreCase("role")) {
      query.with(Sort.by(Sort.Direction.ASC, "roleClass._id"));
    }

    if (page == -1 && number == -1) {
      return new ListUsers((int)(mongoTemplate.count(query, UsersExtraInfo.class, Variables.col_usersExtraInfo)),
      mongoTemplate.find(query, UsersExtraInfo.class, Variables.col_usersExtraInfo), 1);
    }

    else{
      int count = (int) mongoTemplate.count(query, UsersExtraInfo.class,Variables.col_usersExtraInfo);
      return new ListUsers(count,
      mongoTemplate.find(query.skip((page-1)*number).limit(number), UsersExtraInfo.class, Variables.col_usersExtraInfo), (int) Math.ceil(count/number)+1);
    }
  }

  public UsersExtraInfo get(String userId) {

    return mongoTemplate.findOne(new Query(Criteria.where("_id").is(userId)), UsersExtraInfo.class,
        Variables.col_usersExtraInfo);

  }

  public User.ListUsers get(int page, int nCount, String sortType, String groupFilter, String searchUid,
      String searchDisplayName, String mobile, String userStatus) {

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
