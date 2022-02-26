package parsso.idman.helpers.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.users.UsersExtraInfo;

import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class ExpirePassword {
    final LdapTemplate ldapTemplate;
    final MongoTemplate mongoTemplate;
    final UniformLogger uniformLogger;

    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;

    @Autowired
    public ExpirePassword(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate,UniformLogger uniformLogger) {
        this.ldapTemplate = ldapTemplate;
        this.mongoTemplate = mongoTemplate;
        this.uniformLogger = uniformLogger;
    }



    public List<String> expire(String doer, List<UsersExtraInfo> users) {

        List<String> superUsers = new LinkedList<>();

        for (UsersExtraInfo user : users) {
            if (user == null)
                continue;
            if (!user.getRole().equals("SUPERUSER")) {

                ModificationItem[] modificationItems;
                modificationItems = new ModificationItem[1];

                modificationItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("pwdReset", "TRUE"));

                try {
                    ldapTemplate.modifyAttributes(new BuildDnUser(BASE_DN).buildDn(user.get_id().toString()), modificationItems);
                    user.setEndTimeEpoch(new Date().getTime());
                    mongoTemplate.save(user, Variables.col_usersExtraInfo);

                    uniformLogger.info(doer, new ReportMessage(Variables.MODEL_USER, user.get_id(), Variables.ATTR_PASSWORD,
                            Variables.ACTION_EXPIREPASSWORD, Variables.RESULT_SUCCESS, ""));

                } catch (Exception e) {
                    try {
                        modificationItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("pwdReset", "TRUE"));
                        ldapTemplate.modifyAttributes(new BuildDnUser(BASE_DN).buildDn(user.get_id().toString()), modificationItems);
                        user.setEndTimeEpoch(new Date().getTime());
                        mongoTemplate.save(user, Variables.col_usersExtraInfo);

                        uniformLogger.info(doer, new ReportMessage(Variables.MODEL_USER, user.get_id(),
                                Variables.ACTION_EXPIREPASSWORD, Variables.ACTION_REPLACE, Variables.RESULT_SUCCESS, ""));
                    } catch (Exception e1) {
                        e.printStackTrace();
                        uniformLogger.warn(doer, new ReportMessage(Variables.MODEL_USER, user.get_id(),
                                Variables.ACTION_EXPIREPASSWORD, Variables.ACTION_INSERT, Variables.RESULT_FAILED, "writing to ldap"));
                    }
                }
            } else {
                superUsers.add(user.get_id().toString());

                uniformLogger.warn(doer, new ReportMessage(Variables.MODEL_USER, user.get_id(), Variables.ACTION_EXPIREPASSWORD,
                        Variables.ACTION_INSERT, Variables.RESULT_FAILED, "Cant add to SUPERUSER role"));

            }
        }
        return superUsers;
    }

}
