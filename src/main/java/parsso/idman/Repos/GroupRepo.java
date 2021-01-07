package parsso.idman.Repos;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import parsso.idman.Models.Group;
import parsso.idman.Models.User;

import java.util.List;

public interface GroupRepo {

    List<Group> retrieve();

    HttpStatus create(Group ou);

    HttpStatus update(String name, Group ou);

    HttpStatus remove(JSONObject jsonObject);

    Group retrieveOu(String name);

    List<Group> retrieveCurrentUserGroup(User user);


}