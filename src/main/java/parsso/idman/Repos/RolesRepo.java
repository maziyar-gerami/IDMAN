package parsso.idman.Repos;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import parsso.idman.Models.Users.UserRole;

import java.io.IOException;
import java.util.List;

public interface RolesRepo {
	List<UserRole> retrieve();

	HttpStatus updateRole(String doerId, String role, JSONObject userIDs) throws IOException, ParseException;
}
