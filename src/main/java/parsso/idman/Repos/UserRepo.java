package parsso.idman.Repos;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.SimpleUser;
import parsso.idman.Models.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface UserRepo {

    HttpStatus changePassword(String uId, String newPassword, String token);

    HttpStatus showProfilePic(HttpServletResponse response, User user);

    HttpStatus uploadProfilePic(MultipartFile file, String name);

    List<SimpleUser> retrieveUsersMain();

    User getName(String uid, String token);

    List<User> retrieveUsersFull();

    JSONObject create(User p);

    JSONObject createUserImport(User p);

    HttpStatus update(String uid, User p);

    String remove(String userId);

    String remove();

    User retrieveUser(String userId);

    List<JSONObject> checkMail(String token);

    int sendEmail(String email, String cid, String answer);

    int sendEmail(String email, String cid, String uid, String answer);

    HttpStatus updatePass(String userId, String pass, String token);

    JSONArray importFileUsers(MultipartFile file, int[] sequence, boolean hasHeader) throws IOException;

    org.json.simple.JSONObject retrieveDashboardData() throws IOException, ParseException, java.text.ParseException;

    HttpStatus enable(String uid);

    HttpStatus disable(String uid);

    HttpStatus unlock(String uid);

    HttpStatus requestToken(User user);

}