package parsso.idman.repos;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import parsso.idman.models.users.UserRole;

import java.util.List;

public interface RolesRepo {
    List<UserRole> retrieve();

    HttpStatus updateRole(String doerId, String role, JSONObject userIDs);
}