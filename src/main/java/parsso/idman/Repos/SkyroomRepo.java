package parsso.idman.Repos;


import org.json.JSONObject;
import parsso.idman.Models.other.SkyRoom;
import parsso.idman.Models.Users.User;

import java.io.IOException;

public interface SkyroomRepo {
	SkyRoom Run(User user) throws IOException;

	int CreateRoom(String name) throws IOException;

	int GetRoomId(String name) throws IOException;

	String GetRoomGuestUrl(int room_id) throws IOException;

	JSONObject Post(String json) throws IOException;

	String RandomPassMaker(int n);

	int Register(String username, String password, String nickname) throws IOException;

	boolean AddUserRooms(int user_id, int rooms) throws IOException;

	String CreateLoginUrl(int room_id, String user_id, String nickname) throws IOException;
}
