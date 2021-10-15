package parsso.idman.repos;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import parsso.idman.Models.Groups.Group;
import parsso.idman.Models.Users.User;

import java.util.List;

@SuppressWarnings("SameReturnValue")
public interface GroupRepo {
	List<Group> retrieve();

	HttpStatus create(String doerId, Group ou);

	HttpStatus update(String doerID, String name, Group ou);

	HttpStatus remove(String doerId, JSONObject jsonObject);

	Group retrieveOu(boolean simple, String name);

	List<Group> retrieveCurrentUserGroup(User user);

}