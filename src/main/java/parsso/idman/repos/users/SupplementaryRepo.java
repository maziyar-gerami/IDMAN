package parsso.idman.repos.users;

import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;

public interface SupplementaryRepo {
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
  
