package parsso.idman.impls.users.password;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.LdapTemplate;

import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.BuildDnUser;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;

import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Expire {
  final MongoTemplate mongoTemplate;
  final UniformLogger uniformLogger;
  final LdapTemplate ldapTemplate;
  private String BASE_DN;
  UsersRetrieveRepo userOpRetrieve;

  public Expire(MongoTemplate mongoTemplate, LdapTemplate ldapTemplate, UniformLogger uniformLogger,
      UsersRetrieveRepo userOpRetrieve, String BASE_DN) {
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
    this.ldapTemplate = ldapTemplate;
    this.userOpRetrieve = userOpRetrieve;
    this.BASE_DN = BASE_DN;
  }

  public List<String> expire(String doer, List<UsersExtraInfo> usersExtraInfo) {

    List<String> superUsers = new LinkedList<>();

    for (UsersExtraInfo user : usersExtraInfo) {

      if (user.getRole() == null) {
        user.setRole("USER");
      }

      if (!user.getRole().equals("SUPERUSER")) {

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        modificationItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE,
            new BasicAttribute("pwdReset", "TRUE"));

        try {
          ldapTemplate.modifyAttributes(BuildDnUser.buildDn(user.get_id().toString()),
              modificationItems);
          user.setEndTimeEpoch(new Date().getTime());
          mongoTemplate.save(user, Variables.col_usersExtraInfo);

          uniformLogger.info(doer,
              new ReportMessage(Variables.MODEL_USER, user.get_id(), Variables.ATTR_PASSWORD,
                  Variables.ACTION_EXPIREPASSWORD, Variables.RESULT_SUCCESS, ""));

        } catch (Exception e) {
          try {
            modificationItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                new BasicAttribute("pwdReset", "TRUE"));
            ldapTemplate.modifyAttributes(BuildDnUser.buildDn(user.get_id().toString()),
                modificationItems);
            user.setEndTimeEpoch(new Date().getTime());
            mongoTemplate.save(user, Variables.col_usersExtraInfo);

            uniformLogger.info(doer, new ReportMessage(Variables.MODEL_USER, user.get_id(),
                Variables.ACTION_EXPIREPASSWORD, Variables.ACTION_REPLACE, Variables.RESULT_SUCCESS,
                ""));
          } catch (Exception e1) {
            e.printStackTrace();
            uniformLogger.warn(doer, new ReportMessage(Variables.MODEL_USER, user.get_id(),
                Variables.ACTION_EXPIREPASSWORD, Variables.ACTION_INSERT, Variables.RESULT_FAILED,
                "writing to ldap"));
          }
        }
      } else {
        superUsers.add(user.get_id().toString());

        uniformLogger.warn(doer,
            new ReportMessage(Variables.MODEL_USER, user.get_id(), Variables.ACTION_EXPIREPASSWORD,
                Variables.ACTION_INSERT, Variables.RESULT_FAILED, "Cant add to SUPERUSER role"));

      }
    }
    return superUsers;
  }
}
