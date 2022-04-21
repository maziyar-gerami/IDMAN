package parsso.idman.utils.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import parsso.idman.models.other.Setting;

import java.util.List;

@SuppressWarnings("unchecked")
public class JSONencoder {

  public JSONencoder(List<Setting> settings) {
  }

  public JSONArray encode(List<Setting> settings) {

    JSONObject jsonObject;

    JSONArray jsonArray = new JSONArray();

    for (Setting setting : settings) {

      if (setting.get_id() != null) {

        jsonObject = new JSONObject();

        jsonObject.put("_id", setting.get_id());
        jsonObject.put("value", setting.getValue());
        jsonObject.put("descriptionEN", setting.getDescriptionEN());
        jsonObject.put("descriptionFA", setting.getDescriptionFA());
        jsonObject.put("group", setting.getGroupEN());

        jsonArray.add(jsonObject);
      }

    }

    return jsonArray;
  }
}