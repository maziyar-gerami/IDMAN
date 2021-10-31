package parsso.idman.repos;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.models.users.ListUsers;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("SameReturnValue")
public interface UserRepo {
    List<String> remove(String doerID, JSONObject jsonObject);

    HttpStatus changePassword(String uId, String newPassword, String token) throws NamingException;

    String showProfilePic(HttpServletResponse response, User user);

    byte[] showProfilePic(User user);

    void uploadProfilePic(MultipartFile file, String name);

    List<UsersExtraInfo> retrieveUsersMain(int page, int number);


    User getName(String uid, String token);

    List<User> retrieveUsersFull();

    JSONObject create(String doerID, User p);

    JSONObject createUserImport(String doerId, User p);

    User update(String doer, String uid, User p);

    void updateUsersWithSpecificOU(String doerID, String old_ou, String new_ou);

    User retrieveUsers(String userId);

    User retrieveUsersWithLicensed(String userId);

    List<UsersExtraInfo> retrieveGroupsUsers(String groupId);

    String getByMobile(String mobile);

    ListUsers retrieveUsersMain(int page, int number, String sortType, String groupFilter, String searchUid, String searchDisplayName, String userStatus);

    int sendEmail(String email, String uid, String cid, String answer);

    HttpStatus resetPassword(String userId, String oldPass, String token);

    String createUrl(String userId, String token);

    int requestToken(User user);

    JSONObject massUpdate(String doerID, List<User> users);

    ListUsers retrieveUsersMainWithGroupId(String groupId, int page, int nRec);

    HttpStatus massUsersGroupUpdate(String doerID, String groupId, JSONObject gu);

    // --Commented out by Inspection (10/14/2021 9:10 PM):HttpStatus syncUsersDBs();

    List<String> addGroupToUsers(String doer, MultipartFile file, String ou) throws IOException;

    List<String> expirePassword(String name, JSONObject jsonObject);

    int retrieveUsersLDAPSize();

    Boolean SAtoSU();

    Boolean retrieveUsersDevice(String userName);
}