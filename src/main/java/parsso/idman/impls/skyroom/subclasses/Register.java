package parsso.idman.impls.skyroom.subclasses;

import org.json.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;

import parsso.idman.helpers.UniformLogger;

public class Register {

  final MongoTemplate mongoTemplate;
  final UniformLogger uniformLogger;

  public Register(MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
  }

  public int register(String username, String password, String nickname) {
    JSONObject root = new JSONObject();
    root.put("action", "createUser");
    JSONObject params = new JSONObject();
    params.put("username", username);
    params.put("password", password);
    params.put("nickname", nickname);
    params.put("status", 1);
    params.put("is_public", true);
    root.put("params", params);
    JSONObject res;
    try {
      res = new Post(mongoTemplate, uniformLogger).post(root.toString());

    } catch (Exception e) {
      return 0;
    }
    try {
      if (res.getBoolean("ok")) {
        return res.getInt("result");
      }
    } catch (Exception e) {
      return 0;
    }
    return 0;
  }

}
