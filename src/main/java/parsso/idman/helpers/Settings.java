package parsso.idman.helpers;

import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import parsso.idman.configs.Prefs;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.Setting;

public class Settings {
  MongoTemplate mongoTemplate;
  UniformLogger uniformLogger;

  public Settings(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = new UniformLogger(mongoTemplate);
  }

  public Settings() {

  }


  public Setting retrieve(String settingName) {
    Setting s = null;
    try {
      String value = Prefs.get(settingName);
      s = new Setting(settingName, value);
    } catch (Exception e) {
      try {
        s = mongoTemplate.findOne(new Query(Criteria.where("_id").is(settingName)), Setting.class,
            Variables.col_properties);
      } catch (NullPointerException ne) {
        uniformLogger.error("System",
            new ReportMessage(Variables.MODEL_SETTINGS, settingName, Variables.ACTION_GET, Variables.RESULT_FAILED));
      }
    }
    return s;
  }

  public List<JSONObject> password() {

    Query condition = new Query(
        new Criteria()
            .orOperator(
                Criteria.where("_id").is(Variables.PASSWORD_CAPITAL_ALPHABET),
                Criteria.where("_id").is(Variables.PASSWORD_SMALL_ALPHABET),
                Criteria.where("_id").is(Variables.PASSWORD_NUMBER),
                Criteria.where("_id").is(Variables.PASSWORD_SPECIAL),
                Criteria.where("_id").is(Variables.PASSWORD_LENGTH)));
    return mongoTemplate.find(condition, JSONObject.class, Variables.col_properties);
  }
}
