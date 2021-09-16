package parsso.idman.Helpers.Service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import parsso.idman.Models.Services.ServiceGist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Notifs {
	public ServiceGist getNotifications(String userId, String notificationApiURL, String notificationApiKey) throws IOException {
		URL url = new URL(notificationApiURL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");

		con.setRequestProperty("Content-Type", "application/json; utf-8");

		con.setRequestProperty("Accept", "application/json");

		con.setDoOutput(true);

		String jsonInputString = "{\"api-key\" " + ":" + notificationApiKey+","+ "\n" +
				"\"user-id\""+":"+"\""+userId+"\""+"}";

		try(OutputStream os = con.getOutputStream()) {
			byte[] input = "utf-8".getBytes(jsonInputString);
			os.write(input, 0, input.length);
		}


		ServiceGist sg = null;
		
		try(BufferedReader br = new BufferedReader(
				new InputStreamReader(con.getInputStream(),
						"utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response.toString());

			ObjectMapper objectMapper = new ObjectMapper();

			sg = objectMapper.readValue (json.toJSONString(),ServiceGist.class) ;
		}catch (Exception e){
			e.printStackTrace();
		}

		return sg;
	}
}
