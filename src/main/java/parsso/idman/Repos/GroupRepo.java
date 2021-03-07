package parsso.idman.Repos;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.Group;
import parsso.idman.Models.User;

import java.io.IOException;
import java.util.List;

public interface GroupRepo {

    List<Group> retrieve();

    List<Group> retrieve(String ou);

    HttpStatus create(Group ou);

    HttpStatus update(String name, Group ou);

    HttpStatus remove(JSONObject jsonObject);

    Group retrieveOu(String name);

    List<Group> retrieveCurrentUserGroup(User user);



}