package parsso.idman.RepoImpls;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import parsso.idman.Models.SkyRoom;
import parsso.idman.Repos.SkyroomRepo;

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

    public SkyRoom Run(String name) throws IOException {
        int userId = Register(name, RandomPassMaker(8), "niki");
        SkyRoom skyRoom;
        if (userId == 0) {
            int roomId =GetRoomId(name);
            skyRoom = new SkyRoom(true,CreateLoginUrl(roomId, String.valueOf(userId), name),GetRoomGuestUrl(roomId));
            return skyRoom;
        }
        int roomId =CreateRoom(name);
        AddUserRooms(userId, roomId);
        skyRoom = new SkyRoom(true,CreateLoginUrl(roomId, String.valueOf(userId), name),GetRoomGuestUrl(roomId));
        return skyRoom;
    }
    public JSONObject Post(String json) throws IOException {
       String api=apiKey;
        URL url = new URL(api);
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
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
            return  new JSONObject(response.toString());
        }
    }
    @Override
    public String RandomPassMaker(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }
    public int Register(String username, String password, String nickname) throws IOException {
        JSONObject root = new JSONObject();
        root.put("action","createUser");
        JSONObject params = new JSONObject();
        params.put("username", username);
        params.put("password", password);
        params.put("nickname", "کاربر");
        params.put("status", 1);
        params.put("is_public",true);
        root.put("params",params);
        System.out.println(root);
        JSONObject res=Post(root.toString());
        if (res.getBoolean("ok")){
            return res.getInt("result");
        }
        return 0;
    }
    public String CreateLoginUrl(int room_id, String user_id, String nickname, int access, int concurrent, String language, int ttl) throws IOException {
        JSONObject root = new JSONObject();
        root.put("action","createLoginUrl");
        JSONObject params = new JSONObject();
        params.put("room_id",room_id);
        params.put("user_id",user_id);
        params.put("nickname",nickname);
        params.put("access",access);
        params.put("concurrent",concurrent);
        params.put("language",language);
        params.put("ttl", ttl);
        root.put("params",params);
        JSONObject res=Post(root.toString());
        if (res.getBoolean("ok")){
            return res.getString("result");
        }
        return "error";
    }
    public String CreateLoginUrl(int room_id, String user_id, String nickname) throws IOException {
        JSONObject root = new JSONObject();
        root.put("action","createLoginUrl");
        JSONObject params = new JSONObject();
        params.put("room_id",room_id);
        params.put("user_id",user_id);
        params.put("nickname",nickname);
        params.put("access",3);
        params.put("concurrent",1);
        params.put("language","en");
        params.put("ttl", 3600);
        root.put("params",params);
        JSONObject res=Post(root.toString());
        if (res.getBoolean("ok")){
            return res.getString("result");
        }
        return "error";
    }
    public int CreateRoom(String name, String title, boolean guest_login, boolean op_login_first, int max_users)throws IOException{
        JSONObject root = new JSONObject();
        root.put("action","createRoom");
        JSONObject params = new JSONObject();
        params.put("name", name);
        params.put("title", title);
        params.put("guest_login", guest_login);
        params.put("op_login_first", op_login_first);
        params.put("max_users",max_users);
        root.put("params",params);
        JSONObject res=Post(root.toString());
        if (res.getBoolean("ok")){
            return res.getInt("result");
        }
        return 0;

    }
    public int CreateRoom(String name)throws IOException{
        JSONObject root = new JSONObject();
        root.put("action","createRoom");
        JSONObject params = new JSONObject();
        params.put("name", name);
        params.put("title", name);
        params.put("guest_login", true);
        params.put("op_login_first", true);
        params.put("max_users",50);
        root.put("params",params);
        JSONObject res=Post(root.toString());
        if (res.getBoolean("ok")){
            return res.getInt("result");
        }
        return 0;
    }
    public int GetRoomId(String name) throws IOException {
        JSONObject root = new JSONObject();
        root.put("action","getRoom");
        JSONObject params = new JSONObject();
        params.put("name", name.toLowerCase());
        root.put("params",params);
        JSONObject res=Post(root.toString());
        if (res.getBoolean("ok")){
            return res.getJSONObject("result").getInt("id");
        }
        return 0;
    }
    public String GetRoomGuestUrl(int room_id) throws IOException {
        JSONObject root = new JSONObject();
        root.put("action","getRoomUrl");
        JSONObject params = new JSONObject();
        params.put("room_id",room_id);
        params.put("language", "fa");
        root.put("params",params);
        JSONObject res=Post(root.toString());
        if (res.getBoolean("ok")){
            return res.getString("result");
        }
        return "error";
    }
    public boolean AddUserRooms(int user_id, int rooms) throws IOException{
        JSONObject root = new JSONObject();
        root.put("action","addUserRooms");
        JSONObject params = new JSONObject();
        JSONArray roomsJsonArray = new JSONArray();
        JSONObject jsonRooms=new JSONObject();
        jsonRooms.put("room_id",rooms);
        roomsJsonArray.put(jsonRooms);
        params.put("user_id", user_id);
        params.put("rooms", roomsJsonArray);
        root.put("params",params);
        JSONObject res=Post(root.toString());
        if (res.getBoolean("ok")){
            return true;
        }
        return false;
    }

}
