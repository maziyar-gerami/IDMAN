package parsso.idman.repoImpls.groups.helper;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import parsso.idman.helpers.Variables;
import parsso.idman.models.response.Response;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class ExpirePassword {
    private final UserRepo.PasswordOp passwordOp;

    final private UserRepo.UsersOp.Retrieve retrieveUsers;

    public ExpirePassword(UserRepo.PasswordOp passwordOp, UserRepo.UsersOp.Retrieve retrieveUsers) {
        this.passwordOp = passwordOp;
        this.retrieveUsers = retrieveUsers;
    }

    private Response expirePassword(String name, JSONObject jsonObject,String lang) throws NoSuchFieldException, IllegalAccessException {

        List<String> preventedUsers = passwordOp.expire(name, jsonObject);

        if (preventedUsers == null)
            return new Response(null,Variables.MODEL_GROUP,HttpStatus.BAD_REQUEST.value(),lang);
        else if (preventedUsers.size() == 0)
            return new Response(null,Variables.MODEL_GROUP, HttpStatus.OK.value(),lang);
        else
            return new Response(preventedUsers,Variables.MODEL_GROUP, HttpStatus.PARTIAL_CONTENT.value(),lang);
    }

    public Response expireUsersSpecGroupPassword(HttpServletRequest request, String gid,String lang) throws NoSuchFieldException, IllegalAccessException {

        ArrayList<String> temp = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        List<UsersExtraInfo> users = retrieveUsers.retrieveUsersGroup(gid);
        for (UsersExtraInfo usersExtraInfo : users) {
            temp.add(usersExtraInfo.get_id().toString());
        }
        jsonObject.put("names", temp);
        return expirePassword(request.getUserPrincipal().getName(), jsonObject,lang);
    }

}
