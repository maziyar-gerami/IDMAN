package parsso.idman.repoImpls.groups;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.BuildDnUser;
import parsso.idman.models.groups.Group;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repoImpls.groups.helper.BuildAttribute;
import parsso.idman.repoImpls.groups.helper.BuildDnGroup;
import parsso.idman.repoImpls.services.DeleteService;
import parsso.idman.repoImpls.services.RetrieveService;
import parsso.idman.repoImpls.services.update.UpdateService;
import parsso.idman.repos.FilesStorageService;
import parsso.idman.repos.UserRepo;

import javax.naming.Name;
import java.util.List;
import java.util.Objects;

@Service
public class UpdateGroup {
    final LdapTemplate ldapTemplate;
    final MongoTemplate mongoTemplate;
    final UniformLogger uniformLogger;
    final FilesStorageService filesStorageService;
    @Value("${spring.ldap.base.dn}")
    protected String BASE_DN;
    final UserRepo.UsersOp.Retrieve usersOpRetrieve;
    final RetrieveGroup retrieveGroup;
    final CreateGroup createGroup;

    @Autowired
    public UpdateGroup(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate,
                       UniformLogger uniformLogger, UserRepo.UsersOp.Retrieve usersOpRetrieve, FilesStorageService filesStorageService, RetrieveGroup retrieveGroup, CreateGroup createGroup) {
        this.ldapTemplate = ldapTemplate;
        this.mongoTemplate = mongoTemplate;
        this.uniformLogger = uniformLogger;
        this.filesStorageService = filesStorageService;
        this.usersOpRetrieve = usersOpRetrieve;
        this.retrieveGroup = retrieveGroup;
        this.createGroup = createGroup;
    }

    public HttpStatus update(String doerID, String id, Group ou) {

        Name dn = new BuildDnGroup(BASE_DN).buildDn(id);

        List<Group> groups = retrieveGroup.retrieve();

        for (Group group : groups)
            if (!(id.equals(ou.getId())) && group.getId().equals(ou.getId()))
                return HttpStatus.FOUND;

        if (!(id.equals(ou.getId()))) {

            ldapTemplate.unbind(dn);
            createGroup.create(doerID, ou);
            DirContextOperations contextUser;
            uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_GROUP, id, Variables.MODEL_GROUP,
                    Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, ou, ""));

            for (UsersExtraInfo user : usersOpRetrieve.retrieveUsersGroup(id)) {
                for (String group : user.getMemberOf()) {
                    if (group.equalsIgnoreCase(id)) {
                        contextUser = ldapTemplate.lookupContext(new BuildDnUser(BASE_DN).buildDn(user.get_id().toString()));
                        contextUser.removeAttributeValue("ou", id);
                        contextUser.addAttributeValue("ou", ou.getId());
                        ldapTemplate.modifyAttributes(contextUser);
                        UsersExtraInfo usersExtraInfo = mongoTemplate.findOne
                                (new Query(Criteria.where("_id").is(user.get_id())), UsersExtraInfo.class, Variables.col_usersExtraInfo);
                        if (usersExtraInfo != null) usersExtraInfo.getMemberOf().remove(id);
                        Objects.requireNonNull(usersExtraInfo).getMemberOf().add(ou.getId());
                        List<String> temp = usersExtraInfo.getMemberOf();
                        temp.add(ou.getId());
                        usersExtraInfo.setMemberOf(temp);

                        mongoTemplate.save
                                (usersExtraInfo, Variables.col_usersExtraInfo);
                    }
                }
            }

            List<parsso.idman.models.services.Service> services = new RetrieveService(mongoTemplate).listServicesWithGroups(id);
            if (services != null)
                for (parsso.idman.models.services.Service service : services) {

                    //remove old id and add new id
                    //noinspection unchecked
                    ((List<String>) ((JSONArray) service.getAccessStrategy().getRequiredAttributes().get("ou")).get(1)).remove(id);
                    //noinspection unchecked
                    ((List<String>) ((JSONArray) service.getAccessStrategy().getRequiredAttributes().get("ou")).get(1)).add(ou.getId());

                    // delete old service
                    org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
                    //noinspection unchecked
                    jsonObject.put("names", ((((JSONArray) service.getAccessStrategy().getRequiredAttributes().get("ou")).get(1))));
                    new DeleteService(mongoTemplate,uniformLogger).delete(doerID, jsonObject);

                    // create new service

                    new UpdateService(filesStorageService).updateOuIdChange(doerID, service, service.getId(), service.getName(), id, ou.getId());

                }

            uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_GROUP, id, "", Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, ou, ""));

            return HttpStatus.OK;

        } else {

            try {
                ldapTemplate.rebind(dn, null, new BuildAttribute().build(ou));
                uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_GROUP, ou.getId(), "",
                        Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, ou, ""));

                return HttpStatus.OK;

            } catch (Exception e) {
                e.printStackTrace();
                uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_GROUP, doerID, ou.getId(), Variables.ACTION_UPDATE,
                        Variables.RESULT_FAILED, "Writing to ldap"));
                return HttpStatus.BAD_REQUEST;
            }
        }


    }
}
