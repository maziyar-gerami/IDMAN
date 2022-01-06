package parsso.idman.models.other;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
public class Property {

    private String _id;
    private String value;
    private String description;
    private String group;
    private Object type;
    private String help;

    public Property(Setting setting, String lang){
        this._id = setting.get_id();
        this.value = setting.getValue();
        if(lang.equalsIgnoreCase("en"))
            this.description = setting.getDescriptionEN();
        else
            this.description = setting.getDescriptionFA();

        if(lang.equalsIgnoreCase("en"))
            this.group = setting.getGroupEN();
        else
            this.group = setting.getGroupFA();

        this.type = setting.getType();
        if(lang.equalsIgnoreCase("en"))
            this.help = setting.getHelpEN();
        else
            this.help = setting.getHelpFA();

    }

    public Property() {

    }

    public List<Property> settingsToProperty(List<Setting> settings,String lang){
        List<Property> properties = new LinkedList<>();
        for (Setting setting: settings ) {
            properties.add(new Property(setting,lang));
        }
        return properties;
    }
}
