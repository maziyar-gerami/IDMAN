package parsso.idman.repos.users.oprations.sub;

import java.util.List;

import net.minidev.json.JSONObject;

public interface UsersDeleteRepo {
  List<String> remove(String doerID, JSONObject jsonObject);

  
}
