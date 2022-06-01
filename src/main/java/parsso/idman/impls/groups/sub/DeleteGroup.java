package parsso.idman.impls.groups.sub;

import net.minidev.json.JSONObject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;

import parsso.idman.configs.Prefs;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.BuildDnUser;
import parsso.idman.impls.Parameters;
import parsso.idman.impls.groups.helper.BuildDnGroup;
import parsso.idman.impls.users.oprations.retrieve.RetrieveUser;
import parsso.idman.models.groups.Group;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.UserRepo;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class DeleteGroup extends Parameters {
  final UserRepo.UsersOp.Retrieve usersOpRetrieve;
  final RetrieveGroup retrieveGroup;
  public DeleteGroup(LdapTemplate ldapTemplate,  MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
        super(ldapTemplate, mongoTemplate, uniformLogger);
    this.usersOpRetrieve = new RetrieveUser(ldapTemplate, mongoTemplate);
    this.retrieveGroup = new RetrieveGroup(ldapTemplate, mongoTemplate);
  }

  public HttpStatus remove(String doerID, JSONObject jsonObject) {

    ArrayList<String> jsonArray = (ArrayList<String>) jsonObject.get("names");
    DirContextOperations context;
    for (String s : jsonArray) {
      Group group = retrieveGroup.retrieve(false, s);
      Name dn = null;
      try {
        dn = new BuildDnGroup(Prefs.get(Variables.PREFS_BASE_DN)).buildDn(group.getId());
      } catch (Exception e) {
        uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_GROUP, s, Variables.MODEL_GROUP,
            Variables.ACTION_REMOVE, Variables.RESULT_FAILED, "Not exist"));
        continue;
      }
      try {
        ldapTemplate.unbind(dn);
        uniformLogger.info(doerID,
            new ReportMessage(Variables.MODEL_GROUP, group.getId(), Variables.MODEL_GROUP,
                Variables.ACTION_REMOVE, Variables.RESULT_SUCCESS, ""));

      } catch (Exception e) {
        e.printStackTrace();
        uniformLogger.warn(doerID,
            new ReportMessage(Variables.MODEL_GROUP, group.getId(), Variables.MODEL_GROUP,
                Variables.ACTION_REMOVE, Variables.RESULT_FAILED, "writing to ldap"));
      }

      for (UsersExtraInfo user : usersOpRetrieve.retrieveUsersGroup(group.getId())) {
        if (user != null && user.getMemberOf() != null)
          for (String groupN : user.getMemberOf()) {
            if (groupN.equalsIgnoreCase(group.getId())) {
              context = ldapTemplate
                  .lookupContext(new BuildDnUser(Prefs.get("BASE_DN")).buildDn(user.get_id().toString()));
              context.removeAttributeValue("ou", group.getId());
              try {
                ldapTemplate.modifyAttributes(context);
                UsersExtraInfo simpleUser = mongoTemplate.findOne(
                    new Query(Criteria.where("_id").is(user.get_id())), UsersExtraInfo.class,
                    Variables.col_usersExtraInfo);
                try {
                  Objects.requireNonNull(simpleUser).getMemberOf().remove(group.getId());
                } catch (Exception e) {
                  e.printStackTrace();
                }
                mongoTemplate.save(Objects.requireNonNull(simpleUser), Variables.col_usersExtraInfo);
                uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_USER, user.get_id(),
                    Variables.MODEL_GROUP, Variables.ACTION_REMOVE, Variables.RESULT_SUCCESS,
                    groupN + "Removing 'OU'=+" + groupN));

              } catch (Exception e) {
                e.printStackTrace();
                uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, user.get_id(),
                    Variables.MODEL_GROUP, Variables.ACTION_REMOVE, Variables.RESULT_FAILED, groupN,
                    "Changing LDAP for removing 'OU'=" + groupN));

              }
            }

          }
      }
    }

    return HttpStatus.NO_CONTENT;
  }
}
