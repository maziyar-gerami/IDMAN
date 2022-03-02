package parsso.idman.models.other;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

@Setter
@Getter
public class Setting {
    private String _id;
    private String value;
    private String descriptionEN;
    private String descriptionFA;
    private String groupFA;
    private String groupEN;
    private String helpEN;
    private String helpFA;
    private Object type;

    public Setting(){

    }
    public Setting(LinkedHashMap hashMap) {
        this._id = (String) hashMap.get("_id");
        this.value = (String) hashMap.get("value");
        this.descriptionEN = (String) hashMap.get("descriptionEN");
        this.descriptionFA = (String) hashMap.get("descriptionFA");
        this.groupFA = (String) hashMap.get("groupFA");
        this.groupEN = (String) hashMap.get("groupEN");
        this.helpEN = (String) hashMap.get("helpEN");
        this.helpFA = (String) hashMap.get("helpFA");
        this.type = hashMap.get("type");
    }

    public Setting(Setting setting) {
        this._id = setting.get_id();
        this.descriptionEN = setting.getDescriptionEN();
        this.descriptionFA = setting.getDescriptionFA();
        this.groupEN = setting.getGroupEN();
        this.groupFA = setting.getGroupFA();
        this.helpEN = setting.getHelpEN();
        this.helpFA = setting.getHelpFA();
        this.type = setting.getType();
    }
}