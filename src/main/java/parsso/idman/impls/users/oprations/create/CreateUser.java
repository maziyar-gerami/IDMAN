package parsso.idman.impls.users.oprations.create;

import net.minidev.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import parsso.idman.configs.Prefs;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.group.GroupsChecks;
import parsso.idman.helpers.user.BuildAttributes;
import parsso.idman.helpers.user.BuildDnUser;
import parsso.idman.helpers.user.Operations;
import parsso.idman.impls.Parameters;
import parsso.idman.impls.users.oprations.create.helper.UsersCompare;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.GroupRepo;
import parsso.idman.repos.users.oprations.sub.UsersCreateRepo;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;
@Service
public class CreateUser extends Parameters implements UsersCreateRepo {
  private final BuildAttributes buildAttributes;
  private final Operations operations;
  private final GroupRepo groupRepo;

  public CreateUser(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, UniformLogger uniformLogger,
      UsersRetrieveRepo userOpRetrieve,
      BuildAttributes buildAttributes, Operations operations, GroupRepo groupRepo) {
    super(ldapTemplate, mongoTemplate, uniformLogger, userOpRetrieve);
    this.buildAttributes = buildAttributes;
    this.operations = operations;
    this.groupRepo = groupRepo;
  }

  @Override
  public JSONObject create(String doerID, User p) {

    if (p==null || p.get_id() == null || p.get_id().toString().equals("")) {
      return null;
    }

    p.setUserId(p.get_id().toString().toLowerCase());

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
          List<String> invalidAttributes = new LinkedList<>();
          jsonObject.put("userId", p.get_id().toString());
          if (p.getDisplayName() == null || p.getDisplayName().equals("")) invalidAttributes.add("ِDisplayName");
          
          if (p.getMail() == null || p.getMail().equals("")) invalidAttributes.add("Mail");
          
          if (p.getStatus() == null || p.getStatus().equals("")) invalidAttributes.add( "Status");

          if (p.getMobile() == null || p.getMobile().equals("")) invalidAttributes.add( "Mobile");

          if (p.getDisplayName() == null || p.getDisplayName().equals("")) invalidAttributes.add( "DisplayName");

          if (p.getFirstName() == null || p.getFirstName().equals("")) invalidAttributes.add( "FirstName");

          if (p.getLastName() == null || p.getLastName().equals("")) invalidAttributes.add( "LastName");

          if (p.getPassword() == null || p.getPassword().equals("")) invalidAttributes.add( "Password");

          jsonObject.put("invalid attributes", invalidAttributes);

          return jsonObject;
        }

        if (new GroupsChecks(groupRepo).checkGroup(p.getMemberOf())) {

          // create user in ldap
          ldapTemplate.bind(BuildDnUser.buildDn(p.get_id().toString()), null,
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
          jsonObject.put("invalidGroups", new GroupsChecks(groupRepo).invalidGroups(p.getMemberOf()));
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
