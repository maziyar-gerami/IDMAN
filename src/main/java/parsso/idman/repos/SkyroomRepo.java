package parsso.idman.repos;


import org.json.JSONObject;
import parsso.idman.models.other.SkyRoom;
import parsso.idman.models.users.User;

import java.io.IOException;

public interface SkyroomRepo {
    SkyRoom Run(User user) throws IOException;

    @SuppressWarnings("unused")
    int CreateRoom(String name) throws IOException;

    @SuppressWarnings("unused")
    int GetRoomId(String name) throws IOException;

    @SuppressWarnings("unused")
    String GetRoomGuestUrl(int room_id) throws IOException;

    @SuppressWarnings("unused")
    JSONObject Post(String json) throws IOException;

    @SuppressWarnings("unused")
    String RandomPassMaker(int n);

    @SuppressWarnings("unused")
    int Register(String username, String password, String nickname);

    @SuppressWarnings("unused")
    boolean AddUserRooms(int user_id, int rooms) throws IOException;

    @SuppressWarnings("unused")
    String CreateLoginUrl(int room_id, String user_id, String nickname) throws IOException;
}
