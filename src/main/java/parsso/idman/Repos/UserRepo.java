package parsso.idman.Repos;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.Users.ListUsers;
import parsso.idman.Models.Users.User;
import parsso.idman.Models.Users.UsersExtraInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface UserRepo {
	List<String> remove(String doerID, JSONObject jsonObject) throws IOException, ParseException;

	HttpStatus changePassword(String uId, String oldPassword, String newPassword, String token) throws IOException, ParseException;

	String showProfilePic(HttpServletResponse response, User user);

	byte[] showProfilePic(User user);

	HttpStatus uploadProfilePic(MultipartFile file, String name) throws IOException, ParseException;

	List<UsersExtraInfo> retrieveUsersMain(int page, int number);

	int retrieveUsersSize(String groupFilter, String searchUid, String searchDisplayName, String userStatus);

	User getName(String uid, String token) throws IOException, ParseException;

	List<User> retrieveUsersFull();

	JSONObject create(String doerID, User p) throws IOException, ParseException;

	JSONObject createUserImport(String doerId, User p) throws IOException, ParseException;

	User update(String doer, String uid, User p) throws IOException, ParseException;

	List<User> getUsersOfOu(String ou);

	HttpStatus updateUsersWithSpecificOU(String doerID, String old_ou, String new_ou) throws IOException, ParseException;

	User retrieveUsers(String userId) throws IOException, ParseException;

	User retrieveUsersWithLicensed(String userId) throws IOException, ParseException;

	List<UsersExtraInfo> retrieveGroupsUsers(String groupId);

	String getByMobile(String mobile);

	ListUsers retrieveUsersMain(int page, int number, String sortType, String groupFilter, String searchUid, String searchDisplayName, String userStatus);

	int sendEmail(String email, String uid, String cid, String answer) throws IOException, ParseException;

	HttpStatus resetPassword(String userId, String oldPass, String token) throws IOException, ParseException;

	String createUrl(String userId, String token);

	int requestToken(User user);

	JSONObject massUpdate(String doerID, List<User> users);

	ListUsers retrieveUsersMainWithGroupId(String groupId, int page, int nRec);

	HttpStatus massUsersGroupUpdate(String doerID, String groupId, JSONObject gu) throws IOException, ParseException;

	HttpStatus syncUsersDBs();

	List<String> addGroupToUsers(String doer, MultipartFile file, String ou) throws IOException, ParseException;

	List<String> expirePassword(String name, JSONObject jsonObject) throws IOException, ParseException;

}