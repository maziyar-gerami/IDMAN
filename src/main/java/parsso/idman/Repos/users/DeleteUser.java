package parsso.idman.Repos.users;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

public interface DeleteUser {
	List<String> remove(String doerID, JSONObject jsonObject) throws IOException, ParseException;
}
