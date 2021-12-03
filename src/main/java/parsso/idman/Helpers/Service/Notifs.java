package parsso.idman.helpers.service;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Notification;
import parsso.idman.models.other.Return;
import parsso.idman.models.services.ServiceGist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings({"unchecked","rawTypes"})
public class Notifs {
    public ServiceGist getNotifications(String userId, String notificationApiURL, String notificationApiKey) throws IOException {
        URL url;
        try {
            url = new URL(notificationApiURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return new ServiceGist(new Return(405, Variables.MSG_FA_CODE_405));
        }


        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "*/*");

        con.setRequestProperty("Accept", "*/*");

        con.setDoOutput(true);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("api_key", notificationApiKey);
        jsonObject.put("user_id", userId);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8);
            String string = new String(input);
            os.write(input, 0, input.length);
        } catch (Exception e) {
            return new ServiceGist(new Return(503, e.toString()));
        }
        JSONObject json = null;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(),
                        StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            JSONParser parser = new JSONParser();
            try {
                json = (JSONObject) parser.parse(response.toString());
            } catch (Exception e) {
                return new ServiceGist(new Return(400, Variables.MSG_FA_CODE_400));
            }

            if (!ServiceGist.parseServiceGist(json).isNotExist())
                return new ServiceGist(new Return(501, Variables.MSG_FA_CODE_501), ServiceGist.parseServiceGist(json));

        } catch (Exception ignored) {
        }
        int count = Integer.parseInt(Objects.requireNonNull(json).get("count").toString());
        ArrayList<Notification> notifications;
        try {
            notifications = (ArrayList<Notification>) json.get("notifications");
        } catch (Exception e) {
            e.printStackTrace();
            notifications = new ArrayList<>();
        }

        return new ServiceGist(count, notifications, new Return(200, Variables.MSG_FA_CODE_200));

    }
}
