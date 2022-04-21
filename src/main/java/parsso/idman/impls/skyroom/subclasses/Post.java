package parsso.idman.impls.skyroom.subclasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;

import parsso.idman.helpers.Settings;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;

public class Post {
  final MongoTemplate mongoTemplate;
  final UniformLogger uniformLogger;

  public Post(MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
  }

  public JSONObject post(String json) throws IOException {
    boolean skyroomEnable = Boolean
        .parseBoolean(new Settings(mongoTemplate).retrieve(Variables.SKYROOM_ENABLE).getValue().toString());
    if (skyroomEnable) {
      URL url = null;
      try {
        url = new URL(new Settings(mongoTemplate).retrieve(Variables.SKYROOM_API_KEY).getValue().toString());
      } catch (Exception e) {
        uniformLogger.info("System",
            new ReportMessage("skyroom", "", "", "retrieve url", Variables.RESULT_FAILED, "malformed url"));
      }
      HttpsURLConnection con = (HttpsURLConnection) Objects.requireNonNull(url).openConnection();
      con.setRequestMethod("POST");
      con.setRequestProperty("Content-Type", "application/json");
      con.setRequestProperty("user-Agent", "PostmanRuntime/7.26.10");
      con.setRequestProperty("Accept-Encoding", "*/*");
      con.setRequestProperty("Accept", "gzip, deflate");
      con.setRequestProperty("Connection", "close");
      con.setDoOutput(true);

      try (OutputStream os = con.getOutputStream()) {
        os.write(json.getBytes());
      }
      try (BufferedReader br = new BufferedReader(
          new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
          response.append(responseLine.trim());
        }
        return new JSONObject(response.toString());
      }
    } else
      return new JSONObject();
  }

}
