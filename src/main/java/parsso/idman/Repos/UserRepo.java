package parsso.idman.Repos;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.User;
import parsso.idman.Models.SimpleUser;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface UserRepo {

    public HttpStatus changePassword(String uId, String currentPassword, String newPassword);

    HttpStatus showProfilePic(HttpServletResponse response, User user);

    HttpStatus uploadProfilePic(MultipartFile file, String name);

    public List<SimpleUser> retrieveUsersMain();
    public User getName (String uid,String token);
    public List<User> retrieveUsersFull();
    public JSONObject create(User p);

    JSONObject createUserImport(User p);

    public HttpStatus update(String uid, User p);
    public String remove(String userId);
    public String remove();
    public User retrieveUser(String userId);
    public List<JSONObject> checkMail(String token);
    public HttpStatus sendEmail(String email);
    public HttpStatus sendEmail(String email, String uid);
    public HttpStatus checkToken(String userId, String token);
    public String updatePass(String userId, String pass, String token);
    public JSONArray importFileUsers(MultipartFile file , int[] sequence, boolean hasHeader) throws IOException;
    public HttpStatus sendMessage(String mobile);
    public HttpStatus sendMessage(String mobile,String uId);
    public List<JSONObject> checkMobile(String mobile);
    public JSONObject retrieveDashboardData() throws IOException, ParseException, java.text.ParseException;
}