package parsso.idman.impls.refresh.subclass;

import java.util.List;
import java.util.UUID;

import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import parsso.idman.configs.Prefs;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.DashboardData;
import parsso.idman.helpers.user.SimpleUserAttributeMapper;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;

@Service
public class UserRefresh {
  MongoTemplate mongoTemplate;
  LdapTemplate ldapTemplate;
  UniformLogger uniformLogger;
  UsersRetrieveRepo usersOpRetrieve;
  DashboardData dashboardData;

  @Autowired
  public UserRefresh(MongoTemplate mongoTemplate, LdapTemplate ldapTemplate, UniformLogger uniformLogger,
      UsersRetrieveRepo usersOpRetrieve, DashboardData dashboardData) {
    this.mongoTemplate = mongoTemplate;
    this.ldapTemplate = ldapTemplate;
    this.uniformLogger = uniformLogger;
    this.usersOpRetrieve = usersOpRetrieve;
    this.dashboardData = dashboardData;
  }

  public HttpStatus refresh(String doer) {

    SearchControls searchControls = new SearchControls();
    searchControls.setReturningAttributes(new String[] { "*", "+" });
    searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

    // 0. create collection, if not exist
    mongoTemplate.getCollection(Variables.col_usersExtraInfo);

    uniformLogger.info(doer, new ReportMessage(Variables.MODEL_USER, "", "", Variables.ACTION_REFRESH,
        Variables.RESULT_STARTED, "Step 1"));

    UsersExtraInfo userExtraInfo;

    // 1. create documents
    for (User user : usersOpRetrieve.fullAttributes()) {

      try {

        userExtraInfo = mongoTemplate.findOne(
            new Query(new Criteria("_id").regex(user.get_id().toString(), "i")), UsersExtraInfo.class,
            Variables.col_usersExtraInfo);

        if (userExtraInfo != null) {

          if (userExtraInfo.getQrToken() == null || userExtraInfo.getQrToken().equals(""))
            userExtraInfo.setQrToken(UUID.randomUUID().toString());

          String photoName = ldapTemplate.search(
              "ou=People," + Prefs.get(Variables.PREFS_BASE_DN),
              new EqualsFilter("uid", user.get_id().toString()).encode(),
              searchControls,
              (AttributesMapper<String>) attrs -> {
                if (attrs.get("photoName") != null)
                  return attrs.get("photoName").get().toString();

                return "";
              }).get(0);

          if (photoName != null)
            userExtraInfo.setPhotoName(photoName);

        } else {

          userExtraInfo = new UsersExtraInfo();
          userExtraInfo.setUserId(user.get_id().toString());
          userExtraInfo.setQrToken(UUID.randomUUID().toString());
        }

        if (userExtraInfo.getRole() == null)
          userExtraInfo.setRole("USER");

        else if (userExtraInfo.get_id() != null && userExtraInfo.get_id().toString().equalsIgnoreCase("su"))
          userExtraInfo.setRole("SUPERUSER");

        else if (userExtraInfo.getRole() != null)
          userExtraInfo.setRole(userExtraInfo.getRole());

        userExtraInfo.setUnDeletable(userExtraInfo.isUnDeletable());

      } catch (Exception e) {
        userExtraInfo = new UsersExtraInfo(user.get_id().toString());
      }

      userExtraInfo.setMobile(user.getMobile());

      userExtraInfo.setDisplayName(user.getDisplayName());

      userExtraInfo.setMemberOf(user.getMemberOf());

      userExtraInfo.setStatus(user.getStatus());

      userExtraInfo.setPasswordChangedTime(user.getPasswordChangedTime());

      userExtraInfo.setCreationTimeStamp(user.getTimeStamp());

      try {

        mongoTemplate.save(userExtraInfo, Variables.col_usersExtraInfo);
        uniformLogger.info(doer, new ReportMessage(Variables.MODEL_USER, user.get_id(), "",
            Variables.ACTION_REFRESH, Variables.RESULT_SUCCESS, "Step 1: creating documents"));
      } catch (Exception e) {
        e.printStackTrace();
        uniformLogger.warn(doer, new ReportMessage(Variables.MODEL_USER, user.get_id(), "",
            Variables.ACTION_REFRESH, Variables.RESULT_SUCCESS, "writing to mongo"));
      }

    }

    try {
      dashboardData.updateDashboardData();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    uniformLogger.info(doer, new ReportMessage(Variables.MODEL_USER, "", "", Variables.ACTION_REFRESH,
        Variables.RESULT_FINISHED, "Step 1: Started"));

    uniformLogger.info(doer, new ReportMessage(Variables.MODEL_USER, "", "", Variables.ACTION_REFRESH,
        Variables.RESULT_STARTED, "Step 2"));

    // 2. cleanUp mongo
    List<UsersExtraInfo> usersMongo = mongoTemplate.findAll(UsersExtraInfo.class, Variables.col_usersExtraInfo);
    for (UsersExtraInfo usersExtraInfo : usersMongo) {
      List<UsersExtraInfo> usersExtraInfoList = ldapTemplate.search("ou=People," + Prefs.get(Variables.PREFS_BASE_DN),
          new EqualsFilter("uid", usersExtraInfo.get_id().toString()).encode(), searchControls,
          new SimpleUserAttributeMapper());
      if (usersExtraInfoList.size() == 0) {
        mongoTemplate.findAndRemove(new Query(new Criteria("userId").is(usersExtraInfo.get_id())),
            UsersExtraInfo.class, Variables.col_usersExtraInfo);
        uniformLogger.info(doer,
            new ReportMessage(Variables.MODEL_USER, usersExtraInfo.get_id(), "MongoDB Document",
                Variables.ACTION_DELETE, Variables.RESULT_SUCCESS, "Step 2: removing extra document"));
      }
    }

    uniformLogger.info(doer, new ReportMessage(Variables.ACTION_REFRESH, Variables.RESULT_FINISHED, "Step 2"));

    uniformLogger.info(doer, new ReportMessage(Variables.MODEL_USER, "", "",
        Variables.ACTION_REFRESH, Variables.RESULT_SUCCESS, ""));

    return HttpStatus.OK;
  }

}
