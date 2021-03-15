package parsso.idman.Repos;



import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import parsso.idman.Models.UserRole;

import java.util.List;

public interface RolesRepo {

    List<UserRole> retrieve();

    HttpStatus updateRole(String role, JSONObject userIDs);
}
