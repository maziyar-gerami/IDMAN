package parsso.idman.repos;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.models.users.User.ListUsers;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("ALL")
public class UserRepo {


    public interface UsersOp{

        public interface Create{
            JSONObject create(String doerID, User p);

            JSONObject createUserImport(String doerId, User p);
        }

        public interface Retrieve{
            List<UsersExtraInfo> mainAttributes(int page, int number);

            List<User> fullAttributes();

            User retrieveUsers(String userId);

            User retrieveUsersWithLicensed(String userId);

            UsersExtraInfo retrieveUserMain(String userId);

            List<UsersExtraInfo> retrieveUsersGroup(String groupId);

            ListUsers retrieveUsersMainWithGroupId(String groupId, int page, int nRec);

            ListUsers mainAttributes(int page, int number, String sortType, String groupFilter, String searchUid, String searchDisplayName, String mobile, String userStatus);

        }

        public interface Update{

            HttpStatus update(String doer, String uid, User p);

            JSONObject groupOfUsers(String doerID, String groupId, JSONObject gu);

            JSONObject mass(String doerID, List<User> users);

            void usersWithSpecificOU(String doerID, String old_ou, String new_ou);

            List<String> addGroupToUsers(String doer, MultipartFile file, String ou) throws IOException;

        }

        public interface Delete{

            List<String> remove(String doerID, JSONObject jsonObject);
        }

        int retrieveUsersLDAPSize();

    }


    public interface ProfilePic{

        String retrieve(HttpServletResponse response, User user);

        byte[] retrieve(User user);

        boolean upload(MultipartFile file, String name);

        boolean delete(User user);
    }

    public  interface PasswordOp{
        HttpStatus change(String uId, String newPassword, String token);

        HttpStatus reset(String userId, String oldPass, String token);

        JSONObject expire(String name, JSONObject jsonObject);

        HttpStatus changePublic(String userId, String currentPassword, String newPassword);

        public JSONObject expireGroup(String doer, JSONObject jsonObject);
    }

    public  interface Supplementary{

        UsersExtraInfo getName(String uid, String token);

        boolean increaseSameDayPasswordChanges(User user);

        boolean accessChangePassword(User user);

        void setIfLoggedIn();

        User setRole(User user);

        String getByMobile(String mobile);

        int sendEmail(String email, String uid, String cid, String answer);

        String createUrl(String userId, String token);

        Boolean SAtoSU();

        int authenticate(String userId, String password);

        void removeCurrentEndTime(String uid);
    }

}