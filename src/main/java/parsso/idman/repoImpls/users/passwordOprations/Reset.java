package parsso.idman.repoImpls.users.passwordOprations;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
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

public class Reset {
    final UserRepo.UsersOp.Retrieve usersOpRetrieve;
    final UserRepo.Supplementary supplementary;
    final UniformLogger uniformLogger;
    final MongoTemplate mongoTemplate;
    final LdapTemplate ldapTemplate;
    final Token tokenClass;
    private String BASE_URL;
    private String BASE_DN;

    public Reset(UserRepo.UsersOp.Retrieve usersOpRetrieve, UserRepo.Supplementary supplementary, UniformLogger uniformLogger,
                 MongoTemplate mongoTemplate, LdapTemplate ldapTemplate, Token tokenClass, String BASE_URL, String BASE_DN) {
        this.usersOpRetrieve = usersOpRetrieve;
        this.supplementary = supplementary;
        this.uniformLogger = uniformLogger;
        this.mongoTemplate = mongoTemplate;
        this.ldapTemplate = ldapTemplate;
        this.tokenClass = tokenClass;
        this.BASE_URL = BASE_URL;
        this.BASE_DN = BASE_DN;
    }

    public HttpStatus resetPassword(String userId, String pass, String token, int pwdin) {

        User user;

        try {
            user = usersOpRetrieve.retrieveUsers(userId);

        } catch (NullPointerException e) {
            return HttpStatus.FORBIDDEN;
        }

        user = supplementary.setRole(user);

        try {
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!supplementary.accessChangePassword(user))
            return HttpStatus.NOT_ACCEPTABLE;

        HttpStatus httpStatus = tokenClass.checkToken(userId, token);

        if (httpStatus == HttpStatus.OK) {
            DirContextOperations contextUser;

            contextUser = ldapTemplate.lookupContext(new BuildDnUser(BASE_DN).buildDn(user.get_id().toString()));
            contextUser.setAttributeValue("userPassword", pass);

            try {
                if (!supplementary.increaseSameDayPasswordChanges(usersOpRetrieve.retrieveUsers(userId)))
                    return HttpStatus.TOO_MANY_REQUESTS;
                supplementary.removeCurrentEndTime(userId);
                ldapTemplate.modifyAttributes(contextUser);

                uniformLogger.info(userId, new ReportMessage(Variables.MODEL_USER, userId, Variables.ATTR_PASSWORD,
                        Variables.ACTION_RESET, Variables.RESULT_SUCCESS, ""));


                if (Boolean.parseBoolean(new Settings(mongoTemplate).retrieve(Variables.PASSWORD_CHANGE_NOTIFICATION).getValue()))
                    new Notification(mongoTemplate).sendPasswordChangeNotify(user,BASE_URL);


            } catch (org.springframework.ldap.InvalidAttributeValueException e) {
                uniformLogger.warn(userId, new ReportMessage(Variables.MODEL_USER, userId, Variables.ATTR_PASSWORD, Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Repetitive password"));
                UsersExtraInfo usersExtraInfo = user.getUsersExtraInfo();
                long extraTime = Long.parseLong(usersExtraInfo.getResetPassToken()) + ((pwdin / 2) * 60000L);
                Update update = new Update();
                update.set("resetPassToken", extraTime);
                mongoTemplate.upsert(new Query(Criteria.where("_id").is(userId)), update, Variables.col_usersExtraInfo);
                return HttpStatus.FOUND;
            } catch (Exception e) {
                e.printStackTrace();
                uniformLogger.warn(userId, new ReportMessage(Variables.MODEL_USER, userId, Variables.ATTR_PASSWORD,
                        Variables.ACTION_RESET, Variables.RESULT_FAILED, "writing to ldap"));

            }
            return HttpStatus.OK;
        } else {
            return HttpStatus.FORBIDDEN;
        }
    }

}
