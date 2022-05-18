package parsso.idman.impls.users.oprations.update.helper;

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
import parsso.idman.helpers.user.Password;
import parsso.idman.helpers.user.UserAttributeMapper;
import parsso.idman.impls.users.oprations.retrieve.RetrieveUser;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.Time;
import parsso.idman.models.services.Service;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.ServiceRepo;
import javax.naming.Name;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class UpdateUser {

  final LdapTemplate ldapTemplate;
  final MongoTemplate mongoTemplate;
  final UniformLogger uniformLogger;
  final BuildAttributes buildAttributes;
  final ExcelAnalyzer excelAnalyzer;
  final ServiceRepo serviceRepo;
  final String BASE_DN;

  public UpdateUser(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, UniformLogger uniformLogger,
       BuildAttributes buildAttributes, ExcelAnalyzer excelAnalyzer,ServiceRepo serviceRepo,
      String BASE_DN) {
    this.ldapTemplate = ldapTemplate;
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
    this.buildAttributes = buildAttributes;
    this.excelAnalyzer = excelAnalyzer;
    this.serviceRepo = serviceRepo;
    this.BASE_DN = BASE_DN;
  }

  public void updateOuIdChange(String doerID, Service service, long sid, String name, String oldOu, String newOu) throws IOException {

        //Update ou
        new UsersWithSpecificOU(uniformLogger, ldapTemplate, mongoTemplate, buildAttributes,
        new UserAttributeMapper(mongoTemplate), BASE_DN).updateUsersWithSpecificOU(doerID, oldOu, newOu);

        //Update text
        String fileName = String.valueOf(sid);
        String s1 = fileName.replaceAll("\\s+", "");
        String filePath = name + "-" + sid + ".json";

        ObjectMapper mapper = new ObjectMapper();
        //Converting the Object to JSONString
        String jsonString = mapper.writeValueAsString(service);

        try {
            FileWriter file = new FileWriter(new Settings(mongoTemplate).retrieve(Variables.SERVICE_FOLDER_PATH).getValue().toString() + filePath, false);
            file.write(jsonString);
            file.close();
            uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_SERVICE, sid, "", Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, service, ""));
        } catch (Exception e) {
            e.printStackTrace();
            uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_SERVICE, sid, "", Variables.ACTION_UPDATE, Variables.RESULT_FAILED, service, ""));

        }

    }

  public HttpStatus update(String doerID, String usid, User p) {

    p.setUserId(usid.trim());
    Name dn = new BuildDnUser(BASE_DN).buildDn(p.get_id().toString());

    User user = new RetrieveUser(ldapTemplate,mongoTemplate,serviceRepo).retrieveUsers(p.get_id().toString());

    try {
      if (!new RetrieveUser(ldapTemplate,mongoTemplate,serviceRepo).retrieveUsers(doerID).getRole().equals(Variables.ROLE_USER)
          && !new RetrieveUser(ldapTemplate,mongoTemplate,serviceRepo).retrieveUsers(doerID).getRole().equals("PRESENTER")
          && !new RetrieveUser(ldapTemplate,mongoTemplate,serviceRepo).retrieveUsers(usid).getRole().equals("USER") &&
          user.getRole().equals("USER") && new Settings(mongoTemplate).retrieve(Variables.USER_PROFILE_ACCESS)
              .getValue().equalsIgnoreCase("false")) {
        return HttpStatus.FORBIDDEN;
      }
    } catch (Exception ignored) {
    }
    DirContextOperations context;

    context = buildAttributes.buildAttributes(doerID, usid, p, dn);
    Query query = new Query(Criteria.where("_id").is(usid));
    UsersExtraInfo usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class,
        Variables.col_usersExtraInfo);

    try {
      Objects.requireNonNull(usersExtraInfo).setUnDeletable(p.isUnDeletable());
    } catch (Exception e) {
      user.setUnDeletable(p.isUnDeletable());
    }

    if (p.getCStatus() != null) {
      if ((p.getCStatus().equals("unlock") || p.getCStatus().equals("enable"))
          || (p.getStatus().equals("unlock") || p.getStatus().equals("enable"))) {
        p.setStatus("enable");
      } else if (p.getCStatus().equals("disable") || p.getStatus().equals("disable")) {
        p.setStatus("disable");
      }
      Objects.requireNonNull(usersExtraInfo).setStatus(p.getStatus());
    } else {
      Objects.requireNonNull(usersExtraInfo).setStatus(p.getStatus());
    }

    if (p.getMemberOf() != null) {
      usersExtraInfo.setMemberOf(p.getMemberOf());
    }

    if (p.getDisplayName() != null) {
      usersExtraInfo.setDisplayName(p.getDisplayName().trim());
    }

    if (p.getPhoto() != null) {
      usersExtraInfo.setPhotoName(p.getPhoto());
    }

    if (p.isUnDeletable()) {
      usersExtraInfo.setUnDeletable(true);
    }

    if (p.getMobile() != null) {
      usersExtraInfo.setMobile(p.getMobile());
    }

    usersExtraInfo.setTimeStamp(new Date().getTime());

    if (p.getUserPassword() != null && !p.getUserPassword().equals("")) {
      if(new Password(mongoTemplate).check(p.getUserPassword())){
        return HttpStatus.NOT_ACCEPTABLE;}

      context.setAttributeValue("userPassword", p.getUserPassword());
      p.setPasswordChangedTime(
          Long.parseLong(new Time().epochToDateLdapFormat(new Date().getTime()).substring(0, 14)));

    } else {
      p.setPasswordChangedTime(user.getPasswordChangedTime());
    }

    try {
      ldapTemplate.modifyAttributes(context);
      mongoTemplate.save(usersExtraInfo, Variables.col_usersExtraInfo);
      uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_USER, usid, "", Variables.ACTION_UPDATE,
          Variables.RESULT_SUCCESS, ""));
    } catch (org.springframework.ldap.InvalidAttributeValueException e) {
      uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, p.get_id().toString(),
          Variables.ATTR_PASSWORD, Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Repetitive password"));
      return HttpStatus.FOUND;
    } catch (Exception e) {
      e.printStackTrace();
      uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, usid, "", Variables.ACTION_UPDATE,
          Variables.RESULT_FAILED, "Writing to DB"));
    }

    return HttpStatus.OK;
  }

  public JSONObject massUpdate(String doerID, List<User> users) {
    int nCount = users.size();
    int nSuccessful = 0;
    for (User user : users) {
      if (user != null && user.get_id() != null) {
        try {
          update(doerID, user.get_id().toString(), user);
          nSuccessful++;
        } catch (Exception e) {
          e.printStackTrace();
        }
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
      User user = new RetrieveUser(ldapTemplate,mongoTemplate,serviceRepo).retrieveUsers(uid);
      if (user.getMemberOf() != null) {
        if (!user.getMemberOf().contains(groupId)) {
          user.getMemberOf().add(groupId);
        }
      } else {
        groups.add(groupId);
        user.setMemberOf(groups);
      }

      update(doerID, uid, user);
    }
    for (String uid : remove) {
      User user = new RetrieveUser(ldapTemplate,mongoTemplate,serviceRepo).retrieveUsers(uid);
      if (user.getMemberOf().contains(groupId)) {
        user.getMemberOf().remove(groupId);
        update(doerID, uid, user);
      }
    }

    return HttpStatus.OK;
  }
}
