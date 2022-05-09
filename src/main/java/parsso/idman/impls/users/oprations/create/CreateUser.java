package parsso.idman.impls.users.oprations.create;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.group.GroupsChecks;
import parsso.idman.helpers.user.BuildAttributes;
import parsso.idman.helpers.user.BuildDnUser;
import parsso.idman.helpers.user.Operations;
import parsso.idman.impls.groups.RetrieveGroup;
import parsso.idman.impls.users.oprations.create.helper.UsersCompare;
import parsso.idman.impls.users.oprations.update.helper.Parameters;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.UserRepo;

@Service
public class CreateUser extends Parameters implements UserRepo.UsersOp.Create {
  private final BuildAttributes buildAttributes;
  private final Operations operations;
  private final RetrieveGroup retrieveGroup;

  @Autowired
  public CreateUser(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, UniformLogger uniformLogger,
      UserRepo.UsersOp.Retrieve userOpRetrieve,
      BuildAttributes buildAttributes, Operations operations, RetrieveGroup retrieveGroup) {
    super(ldapTemplate, mongoTemplate, uniformLogger, userOpRetrieve);
    this.buildAttributes = buildAttributes;
    this.operations = operations;
    this.retrieveGroup = retrieveGroup;
  }

  @Override
  public JSONObject create(String doerID, User p) {
    p.setUserId(p.get_id().toString().toLowerCase());

    if (p.get_id() == null || p.get_id().toString().equals("")) {

      JSONObject jsonObject = new JSONObject();
      jsonObject.put("invalidGroups", p.get_id().toString());
      return jsonObject;

    }
    User user;
    try {
      user = userOpRetrieve.retrieveUsers(p.get_id().toString());

    } catch (Exception e) {
      user = null;
    }

    try {
      if (user == null || user.get_id().toString() == null) {

        if (p.getDisplayName() == null || p.getDisplayName().equals("") ||
            p.getMail() == null || p.getMail().equals("") || p.getStatus() == null
            || p.getStatus().equals("")) {
          uniformLogger.warn(doerID,
              new ReportMessage(Variables.MODEL_USER, p.get_id().toString(), "", Variables.ACTION_CREATE,
                  Variables.RESULT_FAILED, "essential parameter not exist"));
          JSONObject jsonObject = new JSONObject();
          jsonObject.put("userId", p.get_id().toString());
          if (p.getDisplayName() == null || p.getDisplayName().equals("")) {
            jsonObject.put("invalidParameter", "ِDisplayName");
          }
          if (p.getMail() == null || p.getMail().equals("")) {
            jsonObject.put("invalidParameter", "Mail");
          }
          if (p.getStatus() == null || p.getStatus().equals("")) {
            jsonObject.put("invalidParameter", "Status");
          }
          return jsonObject;
        }

        if (new GroupsChecks(retrieveGroup).checkGroup(p.getMemberOf())) {

          // create user in ldap
          ldapTemplate.bind(new BuildDnUser(BASE_DN).buildDn(p.get_id().toString()), null,
              buildAttributes.build(p));

          if (p.getStatus() != null) {
            if (p.getStatus().equals("disable")) {
              operations.disable(doerID, p.get_id().toString());
            }
          }

          mongoTemplate.save(new UsersExtraInfo(p, p.getPhoto(), p.isUnDeletable()),
              Variables.col_usersExtraInfo);

          uniformLogger.info(doerID,
              new ReportMessage(Variables.MODEL_USER, p.get_id().toString(), "", Variables.ACTION_CREATE,
                  Variables.RESULT_SUCCESS, ""));

          return new JSONObject();
        } else {
          uniformLogger.warn(doerID,
              new ReportMessage(Variables.MODEL_USER, p.get_id().toString(), "", Variables.ACTION_CREATE,
                  Variables.RESULT_FAILED, "group not exist"));
          JSONObject jsonObject = new JSONObject();
          jsonObject.put("userId", p.get_id().toString());
          jsonObject.put("invalidGroups", new GroupsChecks(retrieveGroup).invalidGroups(p.getMemberOf()));
          return jsonObject;
        }
      } else {
        uniformLogger.warn(doerID,
            new ReportMessage(Variables.MODEL_USER, p.get_id().toString(), "", Variables.ACTION_CREATE,
                Variables.RESULT_FAILED, "already exist"));
        return new UsersCompare().compareUsers(user, p);
      }
    } catch (Exception e) {
      e.printStackTrace();
      uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, p.get_id().toString(), "",
          Variables.ACTION_CREATE, Variables.RESULT_FAILED, "Unknown reason"));
      return null;
    }
  }

  @Override
  public JSONObject createUserImport(String doerID, User p) {
    if (p.getUserPassword() == null || p.getUserPassword().equals("")) {
      
      p.setUserPassword(new Settings(mongoTemplate).retrieve(Variables.DEFAULT_USER_PASSWORD).getValue());
    }

    return create(doerID, p);
  }

}
