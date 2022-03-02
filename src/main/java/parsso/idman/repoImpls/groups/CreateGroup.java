package parsso.idman.repoImpls.groups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.groups.Group;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.repoImpls.groups.helper.BuildAttribute;
import parsso.idman.repoImpls.groups.helper.BuildDnGroup;
import parsso.idman.repos.GroupRepo;

@Service
public class CreateGroup implements GroupRepo.Create {
    final LdapTemplate ldapTemplate;
    final UniformLogger uniformLogger;
    final RetrieveGroup retrieveGroup;
    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;

    @Autowired
    public CreateGroup(LdapTemplate ldapTemplate, UniformLogger uniformLogger, RetrieveGroup retrieveGroup) {
        this.ldapTemplate = ldapTemplate;
        this.uniformLogger = uniformLogger;
        this.retrieveGroup = retrieveGroup;
    }

    public HttpStatus create(String doerID, Group ou) {

        for (Group group : retrieveGroup.retrieve())
            if (group.getId().equals(ou.getId()))
                return HttpStatus.FOUND;

        try {
            ldapTemplate.bind(new BuildDnGroup(BASE_DN).buildDn(ou.getId()), null, new BuildAttribute().build(ou));

            uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_GROUP, ou.getId(), Variables.MODEL_GROUP,
                    Variables.ACTION_CREATE, Variables.RESULT_SUCCESS, ""));

            return HttpStatus.CREATED;

        } catch (Exception e) {
            e.printStackTrace();
            uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_GROUP, ou.getId(), Variables.MODEL_GROUP,
                    Variables.ACTION_CREATE, Variables.RESULT_FAILED, "Writing to ldap"));

            return HttpStatus.SERVICE_UNAVAILABLE;
        }
    }
}
