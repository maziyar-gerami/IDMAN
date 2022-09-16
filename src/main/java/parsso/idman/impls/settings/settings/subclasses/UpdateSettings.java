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
      if (property.get_id().equals("pwdFailureCountInterval") ||
          property.get_id().equals("pwdInHistory") ||
          property.get_id().equals("pwdLockout") ||
          property.get_id().equals("pwdMaxFailure") ||
          property.get_id().equals("pwdMaxAge") ||
          property.get_id().equals("pwdLockoutDuration")) {
        switch (property.get_id()) {
          case "pwdFailureCountInterval":
            storedSetting.setValue(property.getValue().toString());
            ldapProperties.add(new Property(storedSetting, "en"));
            break;

          case "pwdInHistory":
            storedSetting.setValue(property.getValue().toString());
            ldapProperties.add(new Property(storedSetting, "en"));
            break;

          case "pwdLockout":
            storedSetting.setValue(property.getValue().toString());
            ldapProperties.add(new Property(storedSetting, "en"));
            break;

          case "pwdMaxFailure":
            storedSetting.setValue(property.getValue().toString());
            ldapProperties.add(new Property(storedSetting, "en"));
            break;

          case "pwdMaxAge":
            storedSetting.setValue(property.getValue().toString());
            ldapProperties.add(new Property(storedSetting, "en"));
            break;

          case "pwdLockoutDuration":
            storedSetting.setValue(property.getValue().toString());
            ldapProperties.add(new Property(storedSetting, "en"));
            break;

        }
      } else if (storedSetting != null && !storedSetting.getValue().equalsIgnoreCase(property.getValue().toString()))
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
