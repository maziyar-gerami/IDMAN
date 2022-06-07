package parsso.idman.repos;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import parsso.idman.models.groups.Group;
import parsso.idman.models.users.User;

import java.util.List;

public interface GroupRepo {
  List<Group> retrieve();

  Group retrieve(boolean simple, String name);

  List<Group> retrieve(User user);

  HttpStatus update(String doerID, String name, Group ou);

  HttpStatus create(String doerId, Group ou);

  HttpStatus remove(String doerId, JSONObject jsonObject);
}