package parsso.idman.repoImpls.users.usersOprations.update.helper;

import lombok.val;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import parsso.idman.models.users.User;
import parsso.idman.repos.UserRepo;

import java.util.LinkedList;
import java.util.List;

public class GroupOfUsers {
    final UserRepo.UsersOp.Retrieve retrieveOp;
    final UpdateUser updateUser;

    public GroupOfUsers(UserRepo.UsersOp.Retrieve retrieveOp, UpdateUser updateUser) {
        this.retrieveOp = retrieveOp;
        this.updateUser = updateUser;
    }

    public HttpStatus massUsersGroupUpdate(String doerID, String groupId, JSONObject gu) {
        val add = (List<String>) gu.get("add");
        List<String> remove;
        remove = (List<String>) gu.get("remove");
        List<String> groups = new LinkedList<>();
        for (String uid : add) {
            User user = retrieveOp.retrieveUsers(uid);
            if (user.getMemberOf() != null) {
                if (!user.getMemberOf().contains(groupId))
                    user.getMemberOf().add(groupId);
            } else {
                groups.add(groupId);
                user.setMemberOf(groups);
            }

            updateUser.update(doerID, uid, user);
        }
        for (String uid : remove) {
            User user = retrieveOp.retrieveUsers(uid);
            if (user.getMemberOf().contains(groupId)) {
                user.getMemberOf().remove(groupId);
                updateUser.update(doerID, uid, user);
            }
        }
        return HttpStatus.OK;
    }
}
