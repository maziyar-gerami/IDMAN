package parsso.idman.Repos;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import parsso.idman.Models.Groups.Group;
import parsso.idman.Models.Users.User;

import java.util.List;

public interface GroupRepo {

    List<Group> retrieve();

    List<Group> retrieve(String ou);

    HttpStatus create(String doerId, Group ou);

    HttpStatus update(String doerID, String name, Group ou);

    HttpStatus remove(String doerId, JSONObject jsonObject);

    Group retrieveOu(String name);

    List<Group> retrieveCurrentUserGroup(User user);


}