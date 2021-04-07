package parsso.idman.Repos;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.DashboardData.Dashboard;
import parsso.idman.Models.Users.ListUsers;
import parsso.idman.Models.Users.SimpleUser;
import parsso.idman.Models.Users.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface UserRepo {

    List<String> remove(String doerID, JSONObject jsonObject);

    HttpStatus changePassword(String uId, String oldPassword, String newPassword, String token);

    String showProfilePic(HttpServletResponse response, User user);

    byte[] showProfilePic(User user);

    HttpStatus uploadProfilePic(MultipartFile file, String name) throws IOException;

    List<SimpleUser> retrieveUsersMain(int page, int number);

    int retrieveUsersSize(String groupFilter, String searchUid, String searchDisplayName, String userStatus);

    User getName(String uid, String token);

    List<User> retrieveUsersFull();

    JSONObject create(String doerID, User p);

    JSONObject createUserImport(String doerId, User p);

    HttpStatus update(String doer, String uid, User p);

    HttpStatus updateUsersWithSpecificOU(String doerID, String old_ou, String new_ou);

    User retrieveUsers(String userId);

    List<SimpleUser> retrieveGroupsUsers(String groupId);

    List<JSONObject> checkMail(String token);

    HttpStatus sendEmail(JSONObject jsonObject);

    ListUsers retrieveUsersMain(int page, int number, String sortType, String groupFilter, String searchUid, String searchDisplayName, String userStatus);

    int sendEmail(String email, String uid, String cid, String answer);

    HttpStatus updatePass(String userId,String oldPass, String token);

    JSONObject importFileUsers(String doerId, MultipartFile file, int[] sequence, boolean hasHeader) throws IOException;

    Dashboard retrieveDashboardData() throws IOException, ParseException, java.text.ParseException, InterruptedException;

    HttpStatus enable(String doerID,String uid);

    String createUrl(String userId, String token);

    HttpStatus disable(String doerID,String uid);

    HttpStatus unlock(String doerID,String uid);

    int requestToken(User user);

    HttpStatus massUpdate(String doerID,List<User> users);

    ListUsers retrieveUsersMainWithGroupId(String groupId, int page, int nRec);

    HttpStatus massUsersGroupUpdate(String doerID,String groupId, JSONObject gu);

    HttpStatus syncUsersDBs();

    List<String> addGroupToUsers(MultipartFile file, String ou) throws IOException;
}