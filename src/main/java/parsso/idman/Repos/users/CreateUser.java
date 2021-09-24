package parsso.idman.Repos.users;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import parsso.idman.Models.Users.User;

import java.io.IOException;

public interface CreateUser {

	JSONObject create(String doerID, User p) throws IOException, ParseException;

	JSONObject createUserImport(String doerId, User p) throws IOException, ParseException;

	String createUrl(String userId, String token);

}