package parsso.idman.repoImpls.groups;

import net.minidev.json.JSONObject;
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
import parsso.idman.models.groups.Group;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repoImpls.groups.helper.BuildDnGroup;
import parsso.idman.repos.UserRepo;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class DeleteGroup {
    final LdapTemplate ldapTemplate;
    final UniformLogger uniformLogger;
    final MongoTemplate mongoTemplate;
    final UserRepo.UsersOp.Retrieve usersOpRetrieve;
    final RetrieveGroup retrieveGroup;
    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;

    @Autowired
    public DeleteGroup(LdapTemplate ldapTemplate, UniformLogger uniformLogger, MongoTemplate mongoTemplate, UserRepo.UsersOp.Retrieve usersOpRetrieve, RetrieveGroup retrieveGroup) {
        this.ldapTemplate = ldapTemplate;
        this.uniformLogger = uniformLogger;
        this.mongoTemplate = mongoTemplate;
        this.usersOpRetrieve = usersOpRetrieve;
        this.retrieveGroup = retrieveGroup;
    }

    public HttpStatus remove(String doerID, JSONObject jsonObject) {

        ArrayList<String> jsonArray = (ArrayList<String>) jsonObject.get("names");
        DirContextOperations context;
        for (String s : jsonArray) {
            Group group = retrieveGroup.retrieve(false, s);

            Name dn = new BuildDnGroup(BASE_DN).buildDn(group.getId());
            try {
                ldapTemplate.unbind(dn);
                uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_GROUP, group.getId(), Variables.MODEL_GROUP,
                        Variables.ACTION_REMOVE, Variables.RESULT_SUCCESS, ""));


            } catch (Exception e) {
                e.printStackTrace();
                uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_GROUP, group.getId(), Variables.MODEL_GROUP,
                        Variables.ACTION_REMOVE, Variables.RESULT_FAILED, "writing to ldap"));
            }

            for (UsersExtraInfo user : usersOpRetrieve.retrieveUsersGroup(group.getId())) {
                if (user != null && user.getMemberOf() != null)
                    for (String groupN : user.getMemberOf()) {
                        if (groupN.equalsIgnoreCase(group.getId())) {
                            context = ldapTemplate.lookupContext(new BuildDnGroup(BASE_DN).buildDn(user.get_id().toString()));
                            context.removeAttributeValue("ou", group.getId());
                            try {
                                ldapTemplate.modifyAttributes(context);
                                UsersExtraInfo simpleUser = mongoTemplate.findOne
                                        (new Query(Criteria.where("_id").is(user.get_id())), UsersExtraInfo.class, Variables.col_usersExtraInfo);
                                try {
                                    Objects.requireNonNull(simpleUser).getMemberOf().remove(group.getId());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                mongoTemplate.save
                                        (Objects.requireNonNull(simpleUser), Variables.col_usersExtraInfo);
                                uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_USER, user.get_id(),
                                        Variables.MODEL_GROUP, Variables.ACTION_REMOVE, Variables.RESULT_SUCCESS, groupN + "Removing 'OU'=+" + groupN));


                            } catch (Exception e) {
                                e.printStackTrace();
                                uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, user.get_id(),
                                        Variables.MODEL_GROUP, Variables.ACTION_REMOVE, Variables.RESULT_FAILED, groupN, "Changing LDAP for removing 'OU'=" + groupN));

                            }
                        }

                    }
            }
        }

        return HttpStatus.NO_CONTENT;
    }
}
