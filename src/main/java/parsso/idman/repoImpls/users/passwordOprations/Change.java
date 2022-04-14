package parsso.idman.repoImpls.users.passwordOprations;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.communicate.Token;
import parsso.idman.helpers.user.BuildDnUser;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.Notification;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.UserRepo;

public class Change {
    final UserRepo.UsersOp.Retrieve usersOpRetrieve;
    final LdapTemplate ldapTemplate;
    final MongoTemplate mongoTemplate;
    final UserRepo.Supplementary supplementary;
    final String BASE_DN;
    Token tokenClass;
    final UniformLogger uniformLogger;
    final String BASE_URL;

    public Change(UserRepo.UsersOp.Retrieve usersOpRetrieve, LdapTemplate ldapTemplate, MongoTemplate mongoTemplate,
            UserRepo.Supplementary supplementary, String BASE_DN, String BASE_URL, UniformLogger uniformLogger) {
        this.usersOpRetrieve = usersOpRetrieve;
        this.ldapTemplate = ldapTemplate;
        this.mongoTemplate = mongoTemplate;
        this.supplementary = supplementary;
        this.BASE_DN = BASE_DN;
        this.BASE_URL = BASE_URL;
        this.uniformLogger = uniformLogger;
    }

    public Change(UserRepo.UsersOp.Retrieve usersOpRetrieve, LdapTemplate ldapTemplate, MongoTemplate mongoTemplate,
            UserRepo.Supplementary supplementary, String BASE_DN, String BASE_URL, Token tokenClass,
            UniformLogger uniformLogger) {
        this.usersOpRetrieve = usersOpRetrieve;
        this.ldapTemplate = ldapTemplate;
        this.mongoTemplate = mongoTemplate;
        this.supplementary = supplementary;
        this.BASE_DN = BASE_DN;
        this.BASE_URL = BASE_URL;
        this.tokenClass = tokenClass;
        this.uniformLogger = uniformLogger;
    }

    public HttpStatus change(String uId, String newPassword, String token) {
        User user = usersOpRetrieve.retrieveUsers(uId);


        AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass", "person"));
        andFilter.and(new EqualsFilter("uid", uId));

        HttpStatus httpStatus = HttpStatus.FORBIDDEN;

        if (token != null) {
            httpStatus = tokenClass.checkToken(uId, token);
            if (httpStatus == HttpStatus.OK) {

                DirContextOperations contextUser;
                contextUser = ldapTemplate.lookupContext(new BuildDnUser(BASE_DN).buildDn(user.get_id().toString()));
                contextUser.setAttributeValue("userPassword", newPassword);

                try {
                    if (!supplementary.increaseSameDayPasswordChanges(user)){
                        uniformLogger.warn(uId, new ReportMessage(Variables.MODEL_USER, uId, Variables.ATTR_PASSWORD,
                            Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Too many password changes"));
                        return HttpStatus.TOO_MANY_REQUESTS;
                    }
                    ldapTemplate.modifyAttributes(contextUser);
                    uniformLogger.info(uId, new ReportMessage(Variables.MODEL_USER, uId, Variables.ATTR_PASSWORD,
                            Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, ""));
                    if (Boolean.parseBoolean(
                            new Settings(mongoTemplate).retrieve(Variables.PASSWORD_CHANGE_NOTIFICATION).getValue()))
                        new Notification(mongoTemplate).sendPasswordChangeNotify(user, BASE_URL);

                    return HttpStatus.OK;
                } catch (org.springframework.ldap.InvalidAttributeValueException e) {
                    uniformLogger.warn(uId, new ReportMessage(Variables.MODEL_USER, uId, Variables.ATTR_PASSWORD,
                            Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Repetitive password"));
                    return HttpStatus.FOUND;
                }

            }
        }

        return httpStatus;
    }

    public HttpStatus publicChange(String userId, String currentPassword, String newPassword) {

        User user = usersOpRetrieve.retrieveUsers(userId);

        AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass", "person"));
        andFilter.and(new EqualsFilter("uid", userId));

        UsersExtraInfo usersExtraInfo = usersOpRetrieve.retrieveUserMain(userId);
        if (usersExtraInfo == null)
            return HttpStatus.NOT_FOUND;

        if (ldapTemplate.authenticate("ou=People," + BASE_DN, andFilter.toString(), currentPassword)) {
            DirContextOperations contextUser;

            if (usersExtraInfo.isLoggedIn())
                return HttpStatus.FORBIDDEN;

            contextUser = ldapTemplate.lookupContext(new BuildDnUser(BASE_DN).buildDn(user.get_id().toString()));
            contextUser.setAttributeValue("userPassword", newPassword);
            try {
                if (!supplementary.increaseSameDayPasswordChanges(usersOpRetrieve.retrieveUsers(userId))){
                    uniformLogger.warn(userId, new ReportMessage(Variables.MODEL_USER, userId, Variables.ATTR_PASSWORD,
                            Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Too many password changes"));
                    return HttpStatus.TOO_MANY_REQUESTS;
                }
                ldapTemplate.modifyAttributes(contextUser);
                usersExtraInfo.setLoggedIn(true);
                mongoTemplate.save(usersExtraInfo, Variables.col_usersExtraInfo);
            } catch (org.springframework.ldap.InvalidAttributeValueException e) {
                uniformLogger.warn(userId, new ReportMessage(Variables.MODEL_USER, userId, Variables.ATTR_PASSWORD,
                            Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Repetitive password"));
                return HttpStatus.FOUND;
            } 

        } else {
            return HttpStatus.NOT_FOUND;
        }

        return HttpStatus.OK;
    }

}
