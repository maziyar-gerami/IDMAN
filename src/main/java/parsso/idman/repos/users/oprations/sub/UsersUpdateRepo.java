package parsso.idman.repos.users.oprations.sub;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.io.IOException;
import net.minidev.json.JSONObject;
import parsso.idman.models.users.User;

public interface UsersUpdateRepo {
  HttpStatus update(String doer, String uid, User p);

  JSONObject groupOfUsers(String doerID, String groupId, JSONObject gu);

  JSONObject mass(String doerID, List<User> users);

  void usersWithSpecificOU(String doerID, String old_ou, String new_ou);

  List<String> addGroupToUsers(String doer, MultipartFile file, String ou) throws IOException, java.io.IOException;

}
