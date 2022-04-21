package parsso.idman.repos;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import parsso.idman.models.users.User;

import java.util.List;

public interface RolesRepo {
  List<User.UserRole> retrieve();

  HttpStatus updateRole(String doerId, String role, JSONObject userIDs);
}
