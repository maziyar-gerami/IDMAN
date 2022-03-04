package parsso.idman.repoImpls.users.usersOprations.update.helper;

import lombok.val;
import net.minidev.json.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.BuildAttributes;
import parsso.idman.helpers.user.BuildDnUser;
import parsso.idman.helpers.user.ExcelAnalyzer;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.Time;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.UserRepo;

import javax.naming.Name;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class UpdateUser extends parsso.idman.repoImpls.users.usersOprations.update.UpdateUser {

    public UpdateUser(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, UniformLogger uniformLogger,
                      UserRepo.UsersOp.Retrieve userOpRetrieve, BuildAttributes buildAttributes, ExcelAnalyzer excelAnalyzer) {
        super(ldapTemplate, mongoTemplate, uniformLogger, userOpRetrieve, buildAttributes, excelAnalyzer);
    }

    public HttpStatus update(String doerID, String usid, User p) {

        p.setUserId(usid.trim());
        Name dn = new BuildDnUser(BASE_DN).buildDn(p.get_id().toString());

        User user = userOpRetrieve.retrieveUsers(p.get_id().toString());

        try {
            if (!userOpRetrieve.retrieveUsers(doerID).getRole().equals(Variables.ROLE_USER)
                    && !userOpRetrieve.retrieveUsers(doerID).getRole().equals("PRESENTER")
                    && !userOpRetrieve.retrieveUsers(usid).getRole().equals("USER") &&
                    user.getRole().equals("USER") && new Settings(mongoTemplate).retrieve(Variables.USER_PROFILE_ACCESS).getValue().equalsIgnoreCase("false"))
                return HttpStatus.FORBIDDEN;
        } catch (Exception ignored) {
        }
        DirContextOperations context;

        //remove current pwdEndTime
        if (p.getExpiredTime() == null ||
                p.getExpiredTime().equals("")
                        && user.getExpiredTime() != null)
            new EndTime(userOpRetrieve,ldapTemplate,BASE_DN).remove(p.get_id().toString());

        context = buildAttributes.buildAttributes(doerID, usid, p, dn);
        Query query = new Query(Criteria.where("_id").is(p.get_id().toString().toLowerCase()));
        UsersExtraInfo usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class, Variables.col_usersExtraInfo);

        try {
            Objects.requireNonNull(usersExtraInfo).setUnDeletable(p.isUnDeletable());
        } catch (Exception e) {
            user.setUnDeletable(p.isUnDeletable());
        }

        if (p.getCStatus() != null) {
            if (p.getCStatus().equals("unlock") || p.getCStatus().equals("enable"))
                p.setStatus("enable");
            else if (p.getCStatus().equals("disable"))
                p.setStatus("disable");
            Objects.requireNonNull(usersExtraInfo).setStatus(p.getStatus());
        } else
            Objects.requireNonNull(usersExtraInfo).setStatus("enable");

        if (p.getMemberOf() != null)
            usersExtraInfo.setMemberOf(p.getMemberOf());

        if (p.getDisplayName() != null)
            usersExtraInfo.setDisplayName(p.getDisplayName().trim());

        if (p.getPhoto() != null)
            usersExtraInfo.setPhotoName(p.getPhoto());

        if (p.isUnDeletable())
            usersExtraInfo.setUnDeletable(true);

        if (p.getMobile()!=null)
            usersExtraInfo.setMobile(p.getMobile());

        usersExtraInfo.setTimeStamp(new Date().getTime());

        if (p.getUserPassword() != null && !p.getUserPassword().equals("")) {
            context.setAttributeValue("userPassword", p.getUserPassword());
            p.setPasswordChangedTime(Long.parseLong(new Time().epochToDateLdapFormat(new Date().getTime()).substring(0, 14)));


        } else
            p.setPasswordChangedTime(user.getPasswordChangedTime());

        try {
            ldapTemplate.modifyAttributes(context);
            mongoTemplate.save(usersExtraInfo, Variables.col_usersExtraInfo);
            uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_USER, usid, "", Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, ""));
        } catch (org.springframework.ldap.InvalidAttributeValueException e) {
            uniformLogger.warn(p.get_id().toString(), new ReportMessage(Variables.MODEL_USER, p.get_id().toString(), Variables.ATTR_PASSWORD, Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Repetitive password"));
            return HttpStatus.FOUND;
        } catch (Exception e) {
            e.printStackTrace();
            uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, usid, "", Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Writing to DB"));
        }

        return HttpStatus.OK;
    }

    public JSONObject massUpdate(String doerID, List<User> users) {
        int nCount = users.size();
        int nSuccessful = 0;
        for (User user : users) {
            if (user != null && user.get_id() != null)
                try {
                    update(doerID, user.get_id().toString(), user);
                    nSuccessful++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nCount", nCount);
        jsonObject.put("nSuccessful", nSuccessful);
        jsonObject.put("nUnSuccessful", nCount - nSuccessful);

        return jsonObject;
    }


    public HttpStatus massUsersGroupUpdate(String doerID, String groupId, JSONObject gu) {
        val add = (List<String>) gu.get("add");
        List<String> remove;
        remove = (List<String>) gu.get("remove");
        List<String> groups = new LinkedList<>();
        for (String uid : add) {
            User user = userOpRetrieve.retrieveUsers(uid);
            if (user.getMemberOf() != null) {
                if (!user.getMemberOf().contains(groupId))
                    user.getMemberOf().add(groupId);
            } else {
                groups.add(groupId);
                user.setMemberOf(groups);
            }

            update(doerID, uid, user);
        }
        for (String uid : remove) {
            User user = userOpRetrieve.retrieveUsers(uid);
            if (user.getMemberOf().contains(groupId)) {
                user.getMemberOf().remove(groupId);
                update(doerID, uid, user);
            }
        }

        return HttpStatus.OK;
    }
}