package parsso.idman.Repos.users;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.Users.User;

import java.io.IOException;
import java.util.List;

public interface UpdateUser {

	HttpStatus changePassword(String uId, String oldPassword, String newPassword, String token) throws IOException, ParseException;

	HttpStatus uploadProfilePic(MultipartFile file, String name) throws IOException, ParseException;

	User update(String doer, String uid, User p) throws IOException, ParseException;

	HttpStatus updateUsersWithSpecificOU(String doerID, String old_ou, String new_ou);

	int sendEmail(String email, String uid, String cid, String answer) throws IOException, ParseException;

	HttpStatus resetPassword(String userId, String oldPass, String token) throws IOException, ParseException;

	int requestToken(User user);

	JSONObject massUpdate(String doerID, List<User> users);

	HttpStatus massUsersGroupUpdate(String doerID, String groupId, JSONObject gu) throws IOException, ParseException;

	HttpStatus syncUsersDBs();

	List<String> addGroupToUsers(String doer, MultipartFile file, String ou) throws IOException, ParseException;

	List<String> expirePassword(String name, JSONObject jsonObject);

}