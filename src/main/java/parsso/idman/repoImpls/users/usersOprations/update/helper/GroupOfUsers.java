package parsso.idman.repoImpls.users.usersOprations.update.helper;

import net.minidev.json.JSONObject;
import parsso.idman.models.users.User;
import parsso.idman.repos.UserRepo;
import parsso.idman.repos.UserRepo.UsersOp.Retrieve;

import java.util.LinkedList;
import java.util.List;
@SuppressWarnings("unchecked")
public class GroupOfUsers {
    UserRepo.UsersOp.Retrieve retrieveOp;
    UpdateUser updateUser;

    public GroupOfUsers(Retrieve userOpRetrieve,
            parsso.idman.repoImpls.users.usersOprations.update.helper.UpdateUser updateUser2) {
        this.retrieveOp = userOpRetrieve;
        this.updateUser = updateUser2;
    }

    public JSONObject massUsersGroupUpdate(String doerID, String groupId, JSONObject gu) {
        List<String> add = (List<String>) gu.get("add");
        List<String> remove = (List<String>) gu.get("remove");
        List<String> addProblems = new LinkedList<>();
        List<String> removeProblems = new LinkedList<>();
        List<String> groups = new LinkedList<>();
        if (add != null)
            for (String uid : add) {
                try {
                    User user = retrieveOp.retrieveUsers(uid);
                    if (user.getMemberOf() != null) {
                        if (!user.getMemberOf().contains(groupId))
                            user.getMemberOf().add(groupId);
                    } else {
                        groups.add(groupId);
                        user.setMemberOf(groups);
                    }

                    updateUser.update(doerID, uid, user);
                } catch (Exception e) {
                    addProblems.add(uid);
                }
            }

        if (remove != null)
            for (String uid : remove) {
                try {
                    User user = retrieveOp.retrieveUsers(uid);
                    if (user.getMemberOf().contains(groupId)) {
                        user.getMemberOf().remove(groupId);
                        updateUser.update(doerID, uid, user);
                    }
                } catch (Exception e) {
                    removeProblems.add(uid);
                }
            }

            if(addProblems.isEmpty()&&removeProblems.isEmpty())
            return null;

            JSONObject exceptions = new JSONObject();
            exceptions.put("add", addProblems);
            exceptions.put("remove", removeProblems);
            
            JSONObject result = new JSONObject();
            result.put("exceptions", exceptions);

        return result;
    }
}
