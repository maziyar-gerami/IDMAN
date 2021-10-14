package parsso.idman.repos;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import parsso.idman.Models.Groups.Group;
import parsso.idman.Models.Users.User;

import java.io.IOException;
import java.util.List;

public interface GroupRepo {
	List<Group> retrieve();

	HttpStatus create(String doerId, Group ou) throws IOException, ParseException;

	HttpStatus update(String doerID, String name, Group ou) throws IOException, ParseException;

	HttpStatus remove(String doerId, JSONObject jsonObject) throws IOException, ParseException;

	Group retrieveOu(boolean simple, String name) throws IOException, ParseException;

	List<Group> retrieveCurrentUserGroup(User user);

}