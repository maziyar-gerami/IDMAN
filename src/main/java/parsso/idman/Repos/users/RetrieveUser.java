package parsso.idman.Repos.users;


import org.json.simple.parser.ParseException;
import parsso.idman.Models.Users.ListUsers;
import parsso.idman.Models.Users.User;
import parsso.idman.Models.Users.UsersExtraInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface RetrieveUser {

	String showProfilePic(HttpServletResponse response, User user);

	byte[] showProfilePic(User user);

	List<UsersExtraInfo> retrieveUsersMain(int page, int number);

	int retrieveUsersSize(String groupFilter, String searchUid, String searchDisplayName, String userStatus);

	User getName(String uid, String token) throws IOException, ParseException;

	List<User> retrieveUsersFull();

	List<User> getUsersOfOu(String ou);

	User retrieveUsers(String userId) throws IOException, ParseException;

	User retrieveUsersWithLicensed(String userId) throws IOException, ParseException;

	List<UsersExtraInfo> retrieveGroupsUsers(String groupId);

	String getByMobile(String mobile);

	ListUsers retrieveUsersMain(int page, int number, String sortType, String groupFilter, String searchUid, String searchDisplayName, String userStatus);

	ListUsers retrieveUsersMainWithGroupId(String groupId, int page, int nRec);

}