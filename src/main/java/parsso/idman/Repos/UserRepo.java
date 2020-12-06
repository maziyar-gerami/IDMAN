package parsso.idman.Repos;

import com.google.gson.JsonObject;
import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.ListUsers;
import parsso.idman.Models.SimpleUser;
import parsso.idman.Models.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface UserRepo {

    HttpStatus remove(JSONObject jsonObject);

    HttpStatus changePassword(String uId, String newPassword, String token);

    String showProfilePic(HttpServletResponse response, User user);

    byte[] showProfilePic(User user);

    HttpStatus uploadProfilePic(MultipartFile file, String name);

    List<SimpleUser> retrieveUsersMain();

    List<SimpleUser> retrieveUsersMain(String sortType,String groupFilter,String searchuUid, String searchUid,String userStatus);

    User getName(String uid, String token);

    List<User> retrieveUsersFull();

    JsonObject create(User p);

    JSONObject createUserImport(User p);

    HttpStatus update(String uid, User p);

    User retrieveUser(String userId);

    List<JSONObject> checkMail(String token);

    HttpStatus sendEmail(JSONObject jsonObject);

    ListUsers retrieveUsersMain(int page, int number, String sortType, String groupFilter, String searchUid, String searchDisplayName, String userStatus);

    int sendEmail(String email, String cid, String answer);

    int sendEmail(String email, String uid, String cid, String answer);

    HttpStatus updatePass(String userId, String pass, String token);

    JSONObject importFileUsers(MultipartFile file, int[] sequence, boolean hasHeader) throws IOException;

    org.json.simple.JSONObject retrieveDashboardData() throws IOException, ParseException, java.text.ParseException;

    HttpStatus enable(String uid);

    String createUrl(String userId, String token);

    HttpStatus disable(String uid);

    HttpStatus unlock(String uid);

    int requestToken(User user);

}