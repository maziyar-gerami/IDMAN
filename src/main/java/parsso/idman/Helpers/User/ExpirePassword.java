package parsso.idman.Helpers.User;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.TimeHelper;
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

    @Autowired LdapTemplate ldapTemplate;

    @Autowired MongoTemplate mongoTemplate;

    @Autowired BuildDnUser buildDnUser;

    public List<String> expire(String doer, List<UsersExtraInfo> users) {
        Logger logger = LogManager.getLogger(doer);

        List<String> superAdminUsers = new LinkedList<>();

        for (UsersExtraInfo user : users) {
            if (!user.getRole().equals("SUPERADMIN")) {

                ModificationItem[] modificationItems;
                modificationItems = new ModificationItem[1];

                modificationItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("pwdEndTime", TimeHelper.getCurrentTimeStampOffset()));

                try {
                    ldapTemplate.modifyAttributes(buildDnUser.buildDn(user.getUserId()), modificationItems);
                    mongoTemplate.remove(new Query(Criteria.where("userId").is(user.getUserId())), Variables.col_usersExtraInfo);
                    mongoTemplate.save(user, Variables.col_usersExtraInfo);
                    logger.warn(new ReportMessage("User", user.getUserId(), "expire password", "add", "success", "").toString());

                } catch (Exception e) {
                    try {
                        modificationItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("pwdEndTime", TimeHelper.getCurrentTimeStampOffset()));
                        ldapTemplate.modifyAttributes(buildDnUser.buildDn(user.getUserId()), modificationItems);
                        mongoTemplate.remove(new Query(Criteria.where("userId").is(user.getUserId())), Variables.col_usersExtraInfo);
                        mongoTemplate.save(user, Variables.col_usersExtraInfo);
                        logger.warn(new ReportMessage("User", user.getUserId(), "expire password", "replace", "success", "").toString());
                    } catch (Exception e1) {
                        logger.warn(new ReportMessage("User", user.getUserId(), "expire password", "Add", "failed", "writing to ldap").toString());
                    }
                }
            } else {
                superAdminUsers.add(user.getUserId());
                logger.warn(new ReportMessage("User", user.getUserId(), "expire password", "Add", "failed", "Cant add to SUPERUSER role").toString());

            }
        }
        return superAdminUsers;
    }

}
