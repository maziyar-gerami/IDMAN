package parsso.idman.repos.users.oprations.sub;

import java.util.List;

import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.models.users.User.ListUsers;

public interface UsersRetrieveRepo {

  ListUsers mainAttributes(int page, int number, String sort, String role, String UserId, String displayName);

  List<User> fullAttributes();

  User retrieveUsers(String userId);

  User retrieveUsersWithLicensed(String userId);

  UsersExtraInfo retrieveUserMain(String userId);

  List<UsersExtraInfo> retrieveUsersGroup(String groupId);

  ListUsers retrieveUsersMainWithGroupId(String groupId, int page, int nRec);

  ListUsers mainAttributes(int page, int number, String sortType, String groupFilter, String searchUid,
      String searchDisplayName, String mobile, String userStatus);

  int retrieveUsersLDAPSize();

}
