package parsso.idman.repos.users.oprations.sub;

import net.minidev.json.JSONObject;
import parsso.idman.models.users.User;

public interface UsersCreateRepo {
  JSONObject create(String doerID, User p);

  JSONObject createUserImport(String doerId, User p);
}
