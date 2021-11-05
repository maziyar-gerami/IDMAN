package parsso.idman.utils.json;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import parsso.idman.models.logs.Setting;

import java.util.List;

@SuppressWarnings("unchecked")
public class JSONencoder {

    public JSONencoder(List<Setting> settings) {
    }

    public JSONArray encode(List<Setting> settings) {

        JSONObject jsonObject;

        JSONArray jsonArray = new JSONArray();

        for (Setting setting : settings) {

            if (setting.getName() != null) {

                jsonObject = new JSONObject();

                jsonObject.put("name", setting.getName());
                jsonObject.put("value", setting.getValue());
                jsonObject.put("description", setting.getDescription());
                jsonObject.put("group", setting.getGroupEN());
                jsonObject.put("system", setting.getSystem());

                jsonArray.add(jsonObject);
            }

        }

        return jsonArray;
    }
}