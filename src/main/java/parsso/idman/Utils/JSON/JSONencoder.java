package parsso.idman.Utils.JSON;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import parsso.idman.Models.Logs.Setting;

import java.util.List;

public class JSONencoder {
    private final List<Setting> settings;

    public JSONencoder(List<Setting> settings) {
        this.settings = settings;
    }

    public JSONArray encode(List<Setting> settings) {

        JSONObject jsonObject = new JSONObject();

        JSONArray jsonArray = new JSONArray();

        for (Setting setting : settings) {

            if (setting.getName() != null) {

                jsonObject = new JSONObject();

                jsonObject.put("name", setting.getName());
                jsonObject.put("value", setting.getValue());
                jsonObject.put("description", setting.getDescription());
                jsonObject.put("group", setting.getGroup());
                jsonObject.put("system", setting.getSystem());

                jsonArray.add(jsonObject);
            }

        }

        return jsonArray;
    }
}