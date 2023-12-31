package parsso.idman.helpers.user;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.AttributesMapper;
import parsso.idman.helpers.Variables;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;

import javax.naming.directory.Attributes;
import java.util.LinkedList;
import java.util.List;

public class UserAttributeMapper implements AttributesMapper<User> {
  MongoTemplate mongoTemplate;

  @Autowired
  public UserAttributeMapper(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @SneakyThrows
  @Override
  public User mapFromAttributes(Attributes attributes) {
    User user = new User();

    user.set_id(null != attributes.get("uid") ? attributes.get("uid").get().toString() : null);
    user.setFirstName(null != attributes.get("givenName") ? attributes.get("givenName").get().toString() : "");
    user.setLastName(null != attributes.get("sn") ? attributes.get("sn").get().toString() : null);
    user.setDisplayName(
        (null != attributes.get("displayName") && !attributes.get("displayName").toString().equals(""))
            ? attributes.get("displayName").get().toString()
            : null);
    user.setMobile(null != attributes.get("mobile") ? attributes.get("mobile").get().toString() : null);
    user.setMail(null != attributes.get("mail") ? attributes.get("mail").get().toString() : null);
    user.setTimeStamp(null != attributes.get("createtimestamp")
        ? Long.parseLong(attributes.get("createtimestamp").get().toString().substring(0, 14))
        : 0);
    user.setPasswordChangedTime(null != attributes.get("pwdChangedTime")
        ? Long.parseLong(attributes.get("pwdChangedTime").get().toString().substring(0, 14))
        : user.getTimeStamp());
    user.setEmployeeNumber(
        (null != attributes.get("employeeNumber") && !attributes.get("employeeNumber").toString().equals(""))
            ? attributes.get("employeeNumber").get().toString()
            : null);
    int nGroups;
    try {
      nGroups = (null == attributes.get("ou") && !attributes.get("ou").toString().equals("")) ? 0
          : attributes.get("ou").size();
    } catch (Exception e) {
      nGroups = 0;
    }
    user.setDescription(
        attributes.get("description") != null ? attributes.get("description").get().toString() : " ");
    List<String> ls = new LinkedList<>();
    for (int i = 0; i < nGroups; i++)
      ls.add(attributes.get("ou").get(i).toString());
    if (user.getUsersExtraInfo() != null)
      user.getUsersExtraInfo().setResetPassToken(
          null != attributes.get("resetPassToken") ? attributes.get("resetPassToken").get().toString()
              : null);
    user.setMemberOf(null != attributes.get("ou") ? ls : null);

    Query query = new Query(Criteria.where("_id").is(user.get_id()));

    UsersExtraInfo usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class,
        Variables.col_usersExtraInfo);

    if (usersExtraInfo != null) {

      user.setPhoto(usersExtraInfo.getPhotoName());
      user.setUnDeletable(usersExtraInfo.isUnDeletable());

      // user.getUsersExtraInfo().setMobileToken(null != attributes.get("mobileToken")
      // ? attributes.get("mobileToken").get().toString() : null);
      user.setExpiredTime(String.valueOf(usersExtraInfo.getExpiredTime()));

      if (usersExtraInfo.getRole() == null)
        user.setRole(usersExtraInfo.getRole());

    }
    if (null != attributes.get("pwdAccountLockedTime")) {

      if (attributes.get("pwdAccountLockedTime").get().toString().equals("00010101000000Z")) {
        user.setEnabled(false);
        user.setLocked(false);
        user.setStatus("disable");

      } else {
        user.setEnabled(true);
        user.setLocked(true);
        user.setStatus("lock");

      }
    } else {
      user.setEnabled(true);
      user.setLocked(false);
      user.setStatus("enable");

    }

    return user;
  }

}
