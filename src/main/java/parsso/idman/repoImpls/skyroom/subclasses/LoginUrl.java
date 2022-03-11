package parsso.idman.repoImpls.skyroom.subclasses;

import org.json.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;

import io.jsonwebtoken.io.IOException;
import parsso.idman.helpers.UniformLogger;

public class LoginUrl {
    final MongoTemplate mongoTemplate;
    final UniformLogger uniformLogger;


    public LoginUrl(MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
        this.mongoTemplate = mongoTemplate;
        this.uniformLogger = uniformLogger;
    }

    public String create(int room_id, String user_id, String nickname) throws IOException, java.io.IOException {
        JSONObject root = new JSONObject();
        root.put("action", "createLoginUrl");
        JSONObject params = new JSONObject();
        params.put("room_id", room_id);
        params.put("user_id", user_id);
        params.put("nickname", nickname);
        params.put("access", 3);
        params.put("concurrent", 1);
        params.put("language", "en");
        params.put("ttl", 3600);
        root.put("params", params);
        JSONObject res =  new Post(mongoTemplate,uniformLogger).post(root.toString());
        try {
            if (res.getBoolean("ok")) {
                return res.getString("result");
            }
        } catch (Exception e) {
            return "error";
        }

        return "error";
    }
    
}
