package parsso.idman.RepoImpls;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.UniformLogger;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.Users.User;
import parsso.idman.Models.other.SkyRoom;
import parsso.idman.repos.SkyroomRepo;
import parsso.idman.repos.UserRepo;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class SkyroomRepoImpl implements SkyroomRepo {
	@Value("${skyroom.api.key}")
	String apiKey;
	@Value("${skyroom.enable}")
	String skyroomEnable;
	@Autowired
	UserRepo userRepo;
	@Autowired
	UniformLogger uniformLogger;

	public SkyRoom Run(User user) throws IOException, ParseException {

		String Realname = user.getFirstName() + user.getLastName();
		String Classname = user.getFirstName().split("")[0] + user.getLastName().split("")[0] + (int) (Long.parseLong(user.getMobile()) % 937);
		int userId = Register(Realname, RandomPassMaker(8), user.getDisplayName());
		SkyRoom skyRoom;
		if (userId == 0) {
			int roomId = GetRoomId(Classname);
			Realname = user.getFirstName() + " " + user.getLastName();

			skyRoom = new SkyRoom(skyroomEnable, user.getUsersExtraInfo().getRole()
					, CreateLoginUrl(roomId, String.valueOf(userId), Realname), GetRoomGuestUrl(roomId));
			uniformLogger.info("System", new ReportMessage("Skyroom", "", "", "created", Variables.RESULT_SUCCESS, "for User \"" + user.getUserId() + "\""));
			return skyRoom;
		}
		int roomId = CreateRoom(Classname);
		AddUserRooms(userId, roomId);
		Realname = user.getFirstName() + " " + user.getLastName();
		try {
			return new SkyRoom(skyroomEnable, user.getUsersExtraInfo().getRole(), CreateLoginUrl(roomId, String.valueOf(userId), Realname), GetRoomGuestUrl(roomId));
		} catch (Exception e) {
			return null;
		}
	}

	public JSONObject Post(String json) throws IOException, ParseException {
		if (skyroomEnable.equalsIgnoreCase("true")) {
			URL url = null;
			try {
				url = new URL(apiKey);
			} catch (Exception e) {
				uniformLogger.info("System", new ReportMessage("skyroom", "", "", "retrieve url", Variables.RESULT_FAILED, "malformed url"));
			}
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("User-Agent", "PostmanRuntime/7.26.10");
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
		} else return new JSONObject();
	}

	@Override
	public String RandomPassMaker(int n) {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
	}

	public int Register(String username, String password, String nickname) {
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
			res = Post(root.toString());

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

	public String CreateLoginUrl(int room_id, String user_id, String nickname) throws IOException, ParseException {
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
		JSONObject res = Post(root.toString());
		try {
			if (res.getBoolean("ok")) {
				return res.getString("result");
			}
		} catch (Exception e) {
			return "error";
		}

		return "error";
	}

	public int CreateRoom(String name) throws IOException, ParseException {
		JSONObject root = new JSONObject();
		root.put("action", "createRoom");
		JSONObject params = new JSONObject();
		params.put("name", name);
		params.put("title", name);
		params.put("guest_login", true);
		params.put("op_login_first", true);
		params.put("max_users", 50);
		root.put("params", params);
		JSONObject res = Post(root.toString());
		if (res.getBoolean("ok")) {
			return res.getInt("result");
		}
		return 0;
	}

	public int GetRoomId(String name) throws IOException, ParseException {
		JSONObject root = new JSONObject();
		root.put("action", "getRoom");
		JSONObject params = new JSONObject();
		params.put("name", name.toLowerCase());
		root.put("params", params);
		JSONObject res = Post(root.toString());
		try {
			if (res.getBoolean("ok")) {
				return res.getJSONObject("result").getInt("id");
			}
		} catch (Exception e) {
			return 0;
		}
		return 0;
	}

	public String GetRoomGuestUrl(int room_id) throws IOException, ParseException {
		if (skyroomEnable.equalsIgnoreCase("true")) {
			JSONObject root = new JSONObject();
			root.put("action", "getRoomUrl");
			JSONObject params = new JSONObject();
			params.put("room_id", room_id);
			params.put("language", "fa");
			root.put("params", params);
			JSONObject res = Post(root.toString());
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

	public boolean AddUserRooms(int user_id, int rooms) throws IOException, ParseException {
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
		JSONObject res = Post(root.toString());
		return res.getBoolean("ok");
	}

}
