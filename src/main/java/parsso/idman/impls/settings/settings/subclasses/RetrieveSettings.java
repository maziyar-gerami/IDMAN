package parsso.idman.impls.settings.settings.subclasses;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.configs.PasswordSettings;
import parsso.idman.models.other.PWD;
import parsso.idman.models.other.Setting;

public class RetrieveSettings {
  MongoTemplate mongoTemplate;
  PasswordSettings passwordSettings;

  public RetrieveSettings(MongoTemplate mongoTemplate, PasswordSettings passwordSettings) {
    this.mongoTemplate = mongoTemplate;
    this.passwordSettings = passwordSettings;
  }

  public List<Setting> retrieve() {

    List<Setting> settings = retrieveALL();
    System.out.println("***************");
    Setting sms_sdk = mongoTemplate.findOne(new Query(Criteria.where("_id").is(Variables.SMS_SDK)), Setting.class,
        Variables.col_properties);

    assert sms_sdk != null;
    if (sms_sdk.getValue().equalsIgnoreCase("magfa")) {
      settings.removeIf(s -> s.get_id().equalsIgnoreCase(Variables.KAVENEGAR_API_KEY));
    } else if (sms_sdk.getValue().toString().equalsIgnoreCase("kavenegar")) {
      settings.removeIf(s -> s.get_id().equalsIgnoreCase(Variables.SMS_MAGFA_USERNAME));
      settings.removeIf(s -> s.get_id().equalsIgnoreCase(Variables.SMS_MAGFA_PASSWORD));
    }

    Setting passwordLimit = mongoTemplate.findOne(
        new Query(Criteria.where("_id").is(Variables.PASSWORD_CHANGE_LIMIT)), Setting.class,
        Variables.col_properties);

    if (Boolean.getBoolean(passwordLimit.getValue())) {
      settings.removeIf(s -> s.get_id().equalsIgnoreCase(Variables.PASSWORD_CHANGE_LIMIT_NUMBER));
    }

    boolean skyroom = Boolean
        .parseBoolean(mongoTemplate.findOne(new Query(Criteria.where("_id").is(Variables.SKYROOM_ENABLE)),
            Setting.class, Variables.col_properties).getValue().toString());

    if (!skyroom) {
      settings.removeIf(s -> s.get_id().equalsIgnoreCase("skyroom.api.key"));
    }

    return settings;
  }

  public List<Setting> retrieveALL() {

    List<Setting> settings = mongoTemplate.find(new Query(), Setting.class, Variables.col_properties);
    PWD pwd = passwordSettings.retrieve();
    for (Setting setting : settings) {
      if (setting.get_id().equals("pwdFailureCountInterval") ||
      setting.get_id().equals("pwdInHistory") ||
      setting.get_id().equals("pwdLockout") ||
      setting.get_id().equals("pwdMaxFailure") ||
      setting.get_id().equals("pwdMaxAge") ||
      setting.get_id().equals("pwdLockoutDuration")) {
        switch (setting.get_id()) {
          case ("pwdFailureCountInterval"):
            setting.setValue(pwd.getPwdFailureCountInterval());
            break;

          case ("pwdInHistory"):
            setting.setValue(pwd.getPwdInHistory());
            break;

          case ("pwdLockout"):
            setting.setValue(pwd.getPwdLockout().toLowerCase());
            break;

          case ("pwdLockoutDuration"):
            setting.setValue(pwd.getPwdLockoutDuration());
            break;

          case ("pwdMaxAge"):
            setting.setValue(pwd.getPwdMaxAge());
            break;

          case ("pwdMaxFailure"):
            setting.setValue(pwd.getPwdMaxFailure());
            break;

          default:
            break;
        }

      }
    }

    return settings;
  }
}
