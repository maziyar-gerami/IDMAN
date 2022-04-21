package parsso.idman.helpers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.UserRepo;

import javax.naming.Name;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

@Service
@SuppressWarnings({ "unchecked" })
public class Operations {
  UserRepo.UsersOp.Retrieve usersOpRetrieve;
  LdapTemplate ldapTemplate;
  UniformLogger uniformLogger;
  MongoTemplate mongoTemplate;

  @Autowired
  public Operations(UserRepo.UsersOp.Retrieve usersOpRetrieve, LdapTemplate ldapTemplate, UniformLogger uniformLogger,
      MongoTemplate mongoTemplate) {
    this.usersOpRetrieve = usersOpRetrieve;
    this.ldapTemplate = ldapTemplate;
    this.uniformLogger = uniformLogger;
    this.mongoTemplate = mongoTemplate;
  }

  @Value("${spring.ldap.base.dn}")
  private String BASE_DN;

  public HttpStatus enable(String doer, String uid) {

    Name dn = new BuildDnUser(BASE_DN).buildDn(uid);

    ModificationItem[] modificationItems;
    modificationItems = new ModificationItem[1];

    User user = usersOpRetrieve.retrieveUsers(uid);
    UsersExtraInfo userExtraInfo = mongoTemplate.findOne(new Query(Criteria.where("_id").is(uid)),
        UsersExtraInfo.class, Variables.col_usersExtraInfo);
    String status = user.getStatus();

    if (status.equalsIgnoreCase("disable")) {
      modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE,
          new BasicAttribute("pwdAccountLockedTime"));
      userExtraInfo.setStatus("enable");

      try {
        ldapTemplate.modifyAttributes(dn, modificationItems);
        mongoTemplate.save(userExtraInfo, Variables.col_usersExtraInfo);
        uniformLogger.info(doer,
            new ReportMessage(Variables.MODEL_USER, user.get_id(), "", Variables.STATUS_CHANGE,
                Variables.RESULT_SUCCESS, Variables.STATUS_ENABLE, ""));

        return HttpStatus.OK;

      } catch (Exception e) {
        e.printStackTrace();
        uniformLogger.warn(doer,
            new ReportMessage(Variables.MODEL_USER, user.get_id(), "", Variables.STATUS_CHANGE,
                Variables.RESULT_FAILED, Variables.STATUS_ENABLE, "Writing to ldap"));
        return HttpStatus.BAD_REQUEST;
      }
    } else {
      return HttpStatus.BAD_REQUEST;
    }
  }

  public HttpStatus disable(String doerID, String uid) {

    Name dn = new BuildDnUser(BASE_DN).buildDn(uid);

    ModificationItem[] modificationItems;
    modificationItems = new ModificationItem[1];

    User user = usersOpRetrieve.retrieveUsers(uid);
    UsersExtraInfo userExtraInfo = mongoTemplate.findOne(new Query(Criteria.where("_id").is(uid)),
        UsersExtraInfo.class, Variables.col_usersExtraInfo);
    userExtraInfo.setStatus("disable");

    if (user.isEnabled()) {

      modificationItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE,
          new BasicAttribute("pwdAccountLockedTime", "00010101000000Z"));
      userExtraInfo.setStatus("disable");

      try {
        ldapTemplate.modifyAttributes(dn, modificationItems);
        mongoTemplate.save(userExtraInfo, Variables.col_usersExtraInfo);

        uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_USER, user.get_id(), "",
            Variables.RESULT_SUCCESS, Variables.ACTION_DISBLAE, ""));
        return HttpStatus.OK;

      } catch (Exception e) {
        e.printStackTrace();
        uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, user.get_id(), Variables.ATTR_STATUS,
            Variables.ACTION_UPDATE, Variables.RESULT_FAILED, Variables.ACTION_DISBLAE, "Writing to DB"));
        return HttpStatus.BAD_REQUEST;
      }
    } else {
      return HttpStatus.BAD_REQUEST;
    }
  }

  public HttpStatus unlock(String doerID, String uid) {

    Name dn = new BuildDnUser(BASE_DN).buildDn(uid);

    ModificationItem[] modificationItems;
    modificationItems = new ModificationItem[1];

    User user = usersOpRetrieve.retrieveUsers(uid);
    UsersExtraInfo userExtraInfo = mongoTemplate.findOne(new Query(Criteria.where("_id").is(uid)),
        UsersExtraInfo.class, Variables.col_usersExtraInfo);

    String locked = user.getStatus();

    if (locked.equalsIgnoreCase("lock")) {
      modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE,
          new BasicAttribute("pwdAccountLockedTime"));
      userExtraInfo.setStatus("enable");

      try {
        ldapTemplate.modifyAttributes(dn, modificationItems);
        mongoTemplate.save(userExtraInfo, Variables.col_usersExtraInfo);
        try {
          modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE,
              new BasicAttribute("pwdFailureTime"));
          ldapTemplate.modifyAttributes(dn, modificationItems);
        } catch (Exception e) {
          e.printStackTrace();
          uniformLogger.warn(doerID,
              new ReportMessage(Variables.MODEL_USER, user.get_id(), Variables.ATTR_STATUS,
                  Variables.ACTION_UPDATE, Variables.RESULT_FAILED, Variables.ACTION_UNLOCK,
                  "Problem with LDAP modifyAttribute"));
        }

        uniformLogger.info(doerID, new ReportMessage(Variables.MODEL_USER, user.get_id(), Variables.ATTR_STATUS,
            Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, Variables.ACTION_UNLOCK, ""));
        return HttpStatus.OK;

      } catch (Exception e) {
        e.printStackTrace();
        uniformLogger.warn(doerID, new ReportMessage(Variables.MODEL_USER, user.get_id(), Variables.ATTR_STATUS,
            Variables.ACTION_UPDATE, Variables.RESULT_FAILED, Variables.ACTION_UNLOCK,
            "Problem with LDAP modifyAttribute"));
        return HttpStatus.BAD_REQUEST;
      }

    } else {
      return HttpStatus.BAD_REQUEST;
    }

  }

  public String activeMobile(User user) {
    String qrDevicesPath = new Settings(mongoTemplate).retrieve(Variables.QR_DEVICES_PATH).getValue();

    String uuid = UUID.randomUUID().toString();

    {
      // JSON parser object to parse read file
      JSONParser jsonParser = new JSONParser();

      try (FileReader reader = new FileReader(qrDevicesPath)) {
        // Read JSON file
        Object obj = jsonParser.parse(reader);
        org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;

        boolean existed = false;

        for (Object o : jsonObject.keySet()) {

          String key = (String) o;
          String value = (String) jsonObject.get(key);
          if (value.equalsIgnoreCase(user.get_id().toString())) {
            jsonObject.remove(key, value);
            existed = true;
            break;
          }
        }

        jsonObject.put(uuid, user.get_id());

        ObjectMapper mapper = new ObjectMapper();

        try {
          // Writing to a file
          mapper.writeValue(new File(qrDevicesPath), jsonObject);

        } catch (IOException e) {
          e.printStackTrace();
          uniformLogger.warn(user.get_id().toString(), new ReportMessage(Variables.MODEL_USER, user.get_id(),
              Variables.ATTR_DEVICEID, Variables.ACTION_INSERT, Variables.RESULT_FAILED,
              "Saving File problem"));

        }

        if (!existed)
          uniformLogger.info(user.get_id().toString(), new ReportMessage(Variables.MODEL_USER, user.get_id(),
              Variables.ATTR_DEVICEID, Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, ""));
        return uuid;

      } catch (FileNotFoundException e) {
        e.printStackTrace();
        uniformLogger.warn(user.get_id().toString(), new ReportMessage(Variables.MODEL_USER, user.get_id(),
            Variables.ATTR_DEVICEID, Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "File not found"));
      } catch (IOException e) {
        e.printStackTrace();
        uniformLogger.warn(user.get_id().toString(), new ReportMessage(Variables.MODEL_USER, user.get_id(),
            Variables.ATTR_DEVICEID, Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Saving problem"));

      } catch (org.json.simple.parser.ParseException e) {
        e.printStackTrace();
        uniformLogger.warn(user.get_id().toString(), new ReportMessage(Variables.MODEL_USER, user.get_id(),
            Variables.ATTR_DEVICEID, Variables.ACTION_UPDATE, Variables.RESULT_FAILED,
            "Json ّfile parse problem"));

      }
    }
    return null;
  }

}
