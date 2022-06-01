package parsso.idman.impls.groups.sub;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;

import parsso.idman.configs.Prefs;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.impls.groups.helper.BuildAttribute;
import parsso.idman.impls.groups.helper.BuildDnGroup;
import parsso.idman.models.groups.Group;
import parsso.idman.models.logs.ReportMessage;

public class CreateGroup {
  final LdapTemplate ldapTemplate;
  final UniformLogger uniformLogger;
  final MongoTemplate mongoTemplate;
  final RetrieveGroup retrieveGroup;

  public CreateGroup(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
    this.ldapTemplate = ldapTemplate;
    this.uniformLogger = uniformLogger;
    this.mongoTemplate = mongoTemplate;
    this.retrieveGroup = new RetrieveGroup(ldapTemplate, mongoTemplate);
    
  }

  public HttpStatus create(String doerID, Group ou) {

    if (ou.getId() == null || ou.getId().equals("") ||
        ou.getName() == null || ou.getName().equals("") ||
        ou.getDescription() == null || ou.getDescription().equals("")) {
      return HttpStatus.BAD_REQUEST;
    }

    for (Group group : new RetrieveGroup(ldapTemplate, mongoTemplate).retrieve()) {
      if (group.getId().equals(ou.getId()))
        return HttpStatus.FOUND;

      try {
        ldapTemplate.bind(new BuildDnGroup(Prefs.get(Variables.PREFS_BASE_DN)).buildDn(ou.getId()), null, new BuildAttribute().build(ou));
        uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_GROUP, ou.getId(), Variables.MODEL_GROUP,
            Variables.ACTION_CREATE, Variables.RESULT_SUCCESS, ""));

        return HttpStatus.CREATED;

      } catch (Exception e) {
        e.printStackTrace();
        uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_GROUP, ou.getId(), Variables.MODEL_GROUP,
            Variables.ACTION_CREATE, Variables.RESULT_FAILED, "Writing to ldap"));
      }
    }
    return HttpStatus.SERVICE_UNAVAILABLE;
  }
}
