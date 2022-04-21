package parsso.idman.impls.skyroom.subclasses;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;

import parsso.idman.helpers.Settings;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;

public class UserRooms {
  final MongoTemplate mongoTemplate;
  final UniformLogger uniformLogger;

  public UserRooms(MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
  }

  public boolean add(int user_id, int rooms) throws IOException {
    JSONObject root = new JSONObject();
    root.put("action", "addUserRooms");
    JSONObject params = new JSONObject();
    JSONArray roomsJsonArray = new JSONArray();
    JSONObject jsonRooms = new JSONObject();
    jsonRooms.put("room_id", rooms);
    roomsJsonArray.put(jsonRooms);
    params.put("user_id", user_id);
    params.put("rooms", roomsJsonArray);
    root.put("params", params);
    JSONObject res = new Post(mongoTemplate, uniformLogger).post(root.toString());
    return res.getBoolean("ok");
  }

  public String getGuestUrl(int room_id) throws IOException {
    boolean skyroomEnable = Boolean
        .parseBoolean(new Settings(mongoTemplate).retrieve(Variables.SKYROOM_ENABLE).getValue().toString());
    if (skyroomEnable) {
      JSONObject root = new JSONObject();
      root.put("action", "getRoomUrl");
      JSONObject params = new JSONObject();
      params.put("room_id", room_id);
      params.put("language", "fa");
      root.put("params", params);
      JSONObject res = new Post(mongoTemplate, uniformLogger).post(root.toString());
      try {
        if (res.getBoolean("ok")) {
          return res.getString("result");
        }
      } catch (Exception e) {
        e.printStackTrace();
        return "error";
      }
    }

    return "error";
  }

  public int getRoomId(String name) throws IOException {
    JSONObject root = new JSONObject();
    root.put("action", "getRoom");
    JSONObject params = new JSONObject();
    params.put("name", name.toLowerCase());
    root.put("params", params);
    JSONObject res = new Post(mongoTemplate, uniformLogger).post(root.toString());
    try {
      if (res.getBoolean("ok")) {
        return res.getJSONObject("result").getInt("id");
      }
    } catch (Exception e) {
      return 0;
    }
    return 0;
  }

  public int create(String name) throws IOException {
    JSONObject root = new JSONObject();
    root.put("action", "createRoom");
    JSONObject params = new JSONObject();
    params.put("name", name);
    params.put("title", name);
    params.put("guest_login", true);
    params.put("op_login_first", true);
    params.put("max_users", 50);
    root.put("params", params);
    JSONObject res = new Post(mongoTemplate, uniformLogger).post(root.toString());
    if (res.getBoolean("ok")) {
      return res.getInt("result");
    }
    return 0;
  }
}
