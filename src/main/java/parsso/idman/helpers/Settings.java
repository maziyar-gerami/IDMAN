package parsso.idman.helpers;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.models.other.Setting;

public class Settings {
    MongoTemplate mongoTemplate;
    public Settings(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    public Settings() {

    }
    public Setting retrieve(String settingName) {
        Setting s = null;
        try {
            s = mongoTemplate.findOne(new Query(Criteria.where("_id").is(settingName)), Setting.class, Variables.col_properties);
        } catch (Exception e) {
            e.printStackTrace();
            //uniformLogger.error("System", new ReportMessage(Variables.MODEL_SETTINGS, settingName, Variables.ACTION_GET, Variables.RESULT_FAILED));
        }
        return s;
    }
}
