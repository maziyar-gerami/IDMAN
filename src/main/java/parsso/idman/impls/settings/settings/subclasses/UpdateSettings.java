package parsso.idman.impls.settings.settings.subclasses;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;

import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.configs.PasswordSettings;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.PWD;
import parsso.idman.models.other.Property;
import parsso.idman.models.other.Setting;

public class UpdateSettings {
  final PasswordSettings passwordSettings;
  final MongoTemplate mongoTemplate;
  final UniformLogger uniformLogger;

  public UpdateSettings(PasswordSettings passwordSettings, MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
    this.passwordSettings = passwordSettings;
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
  }

  public HttpStatus update(String doer, List<Property> properties) {
    List<Property> ldapProperties = new ArrayList<>();
    List<Property> mongoProperties = new ArrayList<>();
    PWD ldapPasswords = passwordSettings.retrieve();
    for (Property property : properties) {
      Setting storedSetting = mongoTemplate.findOne(new Query(Criteria.where("_id").is(property.get_id())),
          Setting.class, Variables.col_properties);
      if (property.get_id().equals("pwdCheckQuality") ||
          property.get_id().equals("pwdFailureCountInterval") ||
          property.get_id().equals("pwdGraceAuthNLimit") ||
          property.get_id().equals("pwdInHistory") ||
          property.get_id().equals("pwdLockout") ||
          property.get_id().equals("pwdMinLength") ||
          property.get_id().equals("pwdMaxFailure") ||
          property.get_id().equals("pwdMaxAge") ||
          property.get_id().equals("pwdLockoutDuration")) {
        switch (property.get_id()) {
          case "pwdCheckQuality":
            storedSetting.setValue(ldapPasswords.getPwdCheckQuality());
            break;

          case "pwdFailureCountInterval":
            storedSetting.setValue(ldapPasswords.getPwdFailureCountInterval());
            break;

          case "pwdGraceAuthNLimit":
            storedSetting.setValue(ldapPasswords.getPwdGraceAuthNLimit());
            break;

          case "pwdInHistory":
            storedSetting.setValue(ldapPasswords.getPwdInHistory());
            break;

          case "pwdLockout":
            storedSetting.setValue(ldapPasswords.getPwdLockout());
            break;

          case "pwdMinLength":
            storedSetting.setValue(ldapPasswords.getPwdMinLength());
            break;

          case "pwdMaxFailure":
            storedSetting.setValue(ldapPasswords.getPwdMaxFailure());
            break;

          case "pwdMaxAge":
            storedSetting.setValue(ldapPasswords.getPwdMaxAge());
            break;

          case "pwdLockoutDuration":
            storedSetting.setValue(ldapPasswords.getPwdLockoutDuration());
            break;

        }

        if (property.get_id().equals("pwdCheckQuality")
            && property.getValue().toString().equalsIgnoreCase("true"))
          property.setValue("1");
        else if (property.get_id().equals("pwdCheckQuality")
            && property.getValue().toString().equalsIgnoreCase("false"))
          property.setValue("0");

        ldapProperties.add(property);
      }
      if (!storedSetting.getValue().equalsIgnoreCase(property.getValue().toString()))
        mongoProperties.add(property);
    }

    for (Property property : mongoProperties) {
      Update update = new Update();
      update.set("value", property.getValue());
      new Thread(() -> {
        mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(property.get_id())), update,
            Variables.col_properties);
        uniformLogger.info(doer, new ReportMessage(Variables.MODEL_SETTINGS, property.get_id(),
            Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, property.getValue().toString(), ""));
      }).start();
    }
    try {
      passwordSettings.update(doer, ldapProperties);
    } catch (Exception e) {
      e.printStackTrace();
      return HttpStatus.BAD_REQUEST;
    }

    return HttpStatus.OK;
  }

}
