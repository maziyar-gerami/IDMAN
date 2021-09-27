package parsso.idman.Repos.roles;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import parsso.idman.Models.Users.UserRole;

import java.util.List;

public interface UpdateRoles {
	HttpStatus updateRole(String doerId, String role, JSONObject userIDs);
}
