package parsso.idman.impls.users.oprations.create.helper;

import net.minidev.json.JSONObject;
import parsso.idman.models.users.User;

import java.util.LinkedList;
import java.util.List;

public class UsersCompare {
  public JSONObject compareUsers(User oldUser, User newUser) {

    List<String> conflicts = new LinkedList<>();

    if (oldUser.get_id().equals(newUser.get_id())) {
      conflicts.add("userId");
    }
    if (oldUser.getFirstName().equals(newUser.getFirstName())) {
      conflicts.add("firsName");
    }
    if (oldUser.getLastName().equals(newUser.getLastName())) {
      conflicts.add("lastName");
    }
    if (oldUser.getDisplayName().equals(newUser.getDisplayName())) {
      conflicts.add("displayName");
    }
    if (oldUser.getMail().equals(newUser.getMail())) {
      conflicts.add("mail");
    }
    if (oldUser.getMobile().equals(newUser.getMobile())) {
      conflicts.add("mobile");
    }
    if (oldUser.getDescription() != null && oldUser.getDescription().equals(newUser.getDescription())) {
      conflicts.add("description");
    }
    if (oldUser.isEnabled() == (newUser.isEnabled())) {
      conflicts.add("status");
    }

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("old", oldUser);
    jsonObject.put("new", newUser);
    jsonObject.put("conflicts", conflicts);

    return jsonObject;
  }
}
