package parsso.idman.Helpers.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.TimeHelper;
import parsso.idman.Helpers.UniformLogger;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.Users.UsersExtraInfo;

import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.util.LinkedList;
import java.util.List;

@Service
public class ExpirePassword {
    @Autowired
    LdapTemplate ldapTemplate;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    BuildDnUser buildDnUser;
    @Autowired
    UniformLogger uniformLogger;

    public List<String> expire(String doer, List<UsersExtraInfo> users) {

        List<String> superAdminUsers = new LinkedList<>();

        for (UsersExtraInfo user : users) {
            if (user == null)
                continue;
            if (!user.getRole().equals("SUPERADMIN")) {

                ModificationItem[] modificationItems;
                modificationItems = new ModificationItem[1];

                modificationItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("pwdEndTime", TimeHelper.getCurrentTimeStampOffset()));

                try {
                    ldapTemplate.modifyAttributes(buildDnUser.buildDn(user.getUserId()), modificationItems);
                    mongoTemplate.remove(new Query(Criteria.where("userId").is(user.getUserId())), Variables.col_usersExtraInfo);
                    mongoTemplate.save(user, Variables.col_usersExtraInfo);
                    uniformLogger.record(doer, Variables.LEVEL_INFO, new ReportMessage("User", user.getUserId(), "expire password", "add", "success", ""));

                } catch (Exception e) {
                    try {
                        modificationItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("pwdEndTime", TimeHelper.getCurrentTimeStampOffset()));
                        ldapTemplate.modifyAttributes(buildDnUser.buildDn(user.getUserId()), modificationItems);
                        mongoTemplate.remove(new Query(Criteria.where("userId").is(user.getUserId())), Variables.col_usersExtraInfo);
                        mongoTemplate.save(user, Variables.col_usersExtraInfo);
                        uniformLogger.record(doer, Variables.LEVEL_INFO, new ReportMessage("User", user.getUserId(), "expire password", "replace", "success", ""));
                    } catch (Exception e1) {
                        uniformLogger.record(doer, Variables.LEVEL_WARN, new ReportMessage("User", user.getUserId(), "expire password", "Add", "failed", "writing to ldap"));
                    }
                }
            } else {
                superAdminUsers.add(user.getUserId());
                uniformLogger.record(doer, Variables.LEVEL_WARN, new ReportMessage("User", user.getUserId(), "expire password", "Add", "failed", "Cant add to SUPERUSER role"));

            }
        }
        return superAdminUsers;
    }

}
