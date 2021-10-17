package parsso.idman.Helpers.Service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Services.ServiceGist;
import parsso.idman.Models.other.Return;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Notifs {
    public ServiceGist getNotifications(String userId, String notificationApiURL, String notificationApiKey) throws IOException {
        URL url;
        try {
            url = new URL(notificationApiURL);
        } catch (MalformedURLException e) {
            return new ServiceGist(new Return(405 , Variables.MSG_FA_CODE_405));
        }


        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json; utf-8");

        con.setRequestProperty("Accept", "application/json");

        con.setDoOutput(true);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("api-key", notificationApiKey);
        jsonObject.put("user-id", userId);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }catch (Exception e){
            return new ServiceGist(new Return(503 , Variables.MSG_FA_CODE_503));
        }


        ServiceGist sg = null;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(),
                        StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            JSONParser parser = new JSONParser();
            JSONObject json;
            try {
                json = (JSONObject) parser.parse(response.toString());
            } catch (Exception e){
                return new ServiceGist(new Return(400 , Variables.MSG_FA_CODE_400));
            }

            if (!ServiceGist.parseServiceGist(json).isNotExist())
                return new ServiceGist(new Return(501 , Variables.MSG_FA_CODE_501),ServiceGist.parseServiceGist(json));

            ObjectMapper objectMapper = new ObjectMapper();

            sg = objectMapper.readValue(json.toJSONString(), ServiceGist.class);
        } catch (Exception e) {
            e.getMessage();
        }

        return new ServiceGist(sg.getCount(), sg.getNotifications(), new Return(200 , Variables.MSG_FA_CODE_200));

    }
}
