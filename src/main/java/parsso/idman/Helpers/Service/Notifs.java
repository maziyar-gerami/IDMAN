package parsso.idman.Helpers.Service;


import parsso.idman.Models.Services.ServiceGist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Notifs {
	public ServiceGist getNotifications(String userId, String notificationApiURL, String notificationApiKey) throws IOException {
		notificationApiURL = notificationApiURL +"/"+ notificationApiKey;
		URL url = new URL(notificationApiURL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");

		con.setRequestProperty("Content-Type", "application/json; utf-8");

		con.setRequestProperty("Accept", "application/json");

		con.setDoOutput(true);

		String jsonInputString = "{\"api-key\"" + ":" + notificationApiKey +
				"\"user-id\""+":"+userId+"}";

		try(OutputStream os = con.getOutputStream()) {
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);
		}


		try(BufferedReader br = new BufferedReader(
				new InputStreamReader(con.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			System.out.println(response.toString());
		}

		return null;
	}
}
