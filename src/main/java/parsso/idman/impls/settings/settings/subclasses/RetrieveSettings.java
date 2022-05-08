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
    PWD pwd = passwordSettings.retrieve();
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

    if (Integer.parseInt(pwd.getPwdCheckQuality()) == 0) {
      settings.removeIf(s -> s.get_id().equalsIgnoreCase("pwdMinLength"));
    }

    return settings;
  }

  public List<Setting> retrieveALL() {

    List<Setting> settings = mongoTemplate.find(new Query(), Setting.class, Variables.col_properties);
    PWD pwd = passwordSettings.retrieve();
    for (Setting setting : settings) {
      if (setting.getGroupEN().equalsIgnoreCase("Password") && setting.getValue() == null) {
        switch (setting.get_id()) {
          case ("pwdCheckQuality"):
            if (Integer.parseInt(pwd.getPwdCheckQuality()) > 0) {
              setting.setValue("true");
            } else {
              setting.setValue("false");
            }
            break;

          case ("pwdFailureCountInterval"):
            setting.setValue(pwd.getPwdFailureCountInterval());
            break;

          case ("pwdGraceAuthNLimit"):
            setting.setValue(pwd.getPwdGraceAuthNLimit());
            break;

          case ("pwdInHistory"):
            setting.setValue(pwd.getPwdInHistory());
            break;

          case ("pwdLockout"):
            setting.setValue(pwd.getPwdLockout());
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

          case ("pwdMinLength"):
            if (Integer.parseInt(pwd.getPwdCheckQuality()) > 0) {
              setting.setValue(pwd.getPwdMinLength());
            }
            break;

          default:
            break;
        }

      }
    }

    return settings;
  }
}
