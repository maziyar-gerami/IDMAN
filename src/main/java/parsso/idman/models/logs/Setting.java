package parsso.idman.models.logs;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Variables;

@Setter
@Getter
public class Setting {
    private String name;
    private String value;
    private String description;
    private String groupFA;
    @JsonProperty("group")
    private String groupEN;
    private String system;
    @JsonIgnore
    MongoTemplate mongoTemplate;

    public Setting(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Setting() {

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