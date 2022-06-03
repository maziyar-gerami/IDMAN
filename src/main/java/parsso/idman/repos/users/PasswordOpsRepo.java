package parsso.idman.repos.users;


import org.springframework.http.HttpStatus;

import net.minidev.json.JSONObject;

public interface PasswordOpsRepo {
  HttpStatus change(String uId, String newPassword, String token);

  HttpStatus reset(String userId, String oldPass, String token);

  JSONObject expire(String name, JSONObject jsonObject);

  HttpStatus changePublic(String userId, String currentPassword, String newPassword);

  public JSONObject expireGroup(String doer, JSONObject jsonObject);
  
}
