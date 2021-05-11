package parsso.idman.Repos;

import org.json.JSONObject;
import parsso.idman.Models.SkyRoom;

import java.io.IOException;

public interface SkyroomRepo {
    SkyRoom Run(String name) throws IOException;
    int CreateRoom(String name, String title, boolean guest_login, boolean op_login_first, int max_users) throws IOException;
    int CreateRoom(String name) throws IOException;
    int GetRoomId(String name) throws IOException;
    String GetRoomGuestUrl(int room_id) throws IOException;
    JSONObject Post(String json) throws IOException;
    String RandomPassMaker(int n);
    int Register(String username, String password, String nickname) throws IOException;
    boolean AddUserRooms(int user_id, int rooms) throws IOException;
    String CreateLoginUrl(int room_id, String user_id, String nickname, int access, int concurrent, String language, int ttl) throws IOException;
    String CreateLoginUrl(int room_id, String user_id, String nickname) throws IOException;
}
