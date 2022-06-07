package parsso.idman.impls.groups.sub;

import org.json.simple.JSONArray;
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
import parsso.idman.impls.groups.helper.BuildAttribute;
import parsso.idman.impls.groups.helper.BuildDnGroup;
import parsso.idman.impls.services.DeleteService;
import parsso.idman.impls.services.RetrieveService;
import parsso.idman.impls.services.update.UpdateService;
import parsso.idman.impls.users.oprations.retrieve.RetrieveUser;
import parsso.idman.models.groups.Group;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.FilesStorageService;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;

import javax.naming.Name;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class UpdateGroup extends Parameters {
  final LdapTemplate ldapTemplate;
  final MongoTemplate mongoTemplate;
  final UniformLogger uniformLogger;
  final FilesStorageService filesStorageService;
  final UsersRetrieveRepo usersOpRetrieve;
  final CreateGroup createGroup;

  public UpdateGroup(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate,
      UniformLogger uniformLogger, FilesStorageService filesStorageService) {
    super(ldapTemplate, mongoTemplate, uniformLogger);
    this.ldapTemplate = ldapTemplate;
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
    this.filesStorageService = filesStorageService;
    this.usersOpRetrieve = new RetrieveUser(ldapTemplate, mongoTemplate);
    this.createGroup = new CreateGroup(ldapTemplate, mongoTemplate, uniformLogger);
  }

  @SuppressWarnings("unchecked")
  public HttpStatus update(String doerID, String id, Group ou) {

    if (ou.getId() == null || ou.getId().equals("") ||
        ou.getName() == null || ou.getName().equals("") ||
        ou.getDescription() == null || ou.getDescription().equals("") ||
        id == null || id.equals(""))
      return HttpStatus.BAD_REQUEST;

    if (new RetrieveGroup(ldapTemplate, mongoTemplate).retrieve(true, id) == null)
      return HttpStatus.NOT_FOUND;

    Name dn = new BuildDnGroup(Prefs.get(Variables.PREFS_BASE_DN)).buildDn(id);

    if (!(id.equals(ou.getId()))) {
      ldapTemplate.unbind(dn);
      createGroup.create(doerID, ou);

      uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_GROUP, id, Variables.MODEL_GROUP,
          Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, ou, ""));

      for (UsersExtraInfo user : usersOpRetrieve.retrieveUsersGroup(id)) {
        for (String group : user.getMemberOf()) {
          if (group.equalsIgnoreCase(id)) {
            DirContextOperations contextUser = ldapTemplate
                .lookupContext(BuildDnUser.buildDn(user.get_id().toString()));
            contextUser.removeAttributeValue("ou", id);
            contextUser.addAttributeValue("ou", ou.getId());
            ldapTemplate.modifyAttributes(contextUser);
            UsersExtraInfo usersExtraInfo = mongoTemplate.findOne(
                new Query(Criteria.where("_id").is(user.get_id())), UsersExtraInfo.class,
                Variables.col_usersExtraInfo);
            if (usersExtraInfo != null) {
              usersExtraInfo.getMemberOf().remove(id);
            } else {
              usersExtraInfo = new UsersExtraInfo(usersOpRetrieve.retrieveUsers(user.get_id().toString()));
            }

            try {
              usersExtraInfo.getMemberOf().add(ou.getId());
            } catch (NullPointerException nu) {
              List<String> ls = new LinkedList<>();
              ls.add(ou.getId());
              usersExtraInfo.setMemberOf(ls);
            }

            Objects.requireNonNull(usersExtraInfo).getMemberOf().add(ou.getId());
            List<String> temp = usersExtraInfo.getMemberOf();
            temp.add(ou.getId());
            usersExtraInfo.setMemberOf(temp);

            mongoTemplate.save(usersExtraInfo, Variables.col_usersExtraInfo);
          }
        }
      }

      List<parsso.idman.models.services.Service> services = new RetrieveService(mongoTemplate)
          .listServicesWithGroups(id);
      if (services != null)
        for (parsso.idman.models.services.Service service : services) {

          // remove old id and add new id
          // noinspection unchecked
          ((List<?>) ((JSONArray) service.getAccessStrategy().getRequiredAttributes().get("ou")).get(1))
              .remove(id);
          // noinspection unchecked
          ((List<String>) ((JSONArray) service.getAccessStrategy().getRequiredAttributes().get("ou")).get(1))
              .add(ou.getId());

          // delete old service
          org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
          // noinspection unchecked
          jsonObject.put("names",
              ((((JSONArray) service.getAccessStrategy().getRequiredAttributes().get("ou")).get(1))));
          new DeleteService(mongoTemplate, uniformLogger).delete(doerID, jsonObject);

          // create new service

          new UpdateService(filesStorageService).updateOuIdChange(doerID, service, service.getId(),
              service.getName(), id, ou.getId());

        }

      uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_GROUP, id, "", Variables.ACTION_UPDATE,
          Variables.RESULT_SUCCESS, ou, ""));

      return HttpStatus.OK;

    } else {

      try {
        ldapTemplate.rebind(dn, null, new BuildAttribute().build(ou));
        uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_GROUP, ou.getId(), "",
            Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, ou, ""));

        return HttpStatus.OK;

      } catch (Exception e) {
        e.printStackTrace();
        uniformLogger.warn(doerID,
            new ReportMessage(Variables.MODEL_GROUP, doerID, ou.getId(), Variables.ACTION_UPDATE,
                Variables.RESULT_FAILED, "Writing to ldap"));
        return HttpStatus.BAD_REQUEST;
      }
    }

  }
}
