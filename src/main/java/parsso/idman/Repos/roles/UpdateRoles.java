package parsso.idman.Repos.roles;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;

public interface UpdateRoles {
	HttpStatus updateRole(String doerId, String role, JSONObject userIDs);
}
