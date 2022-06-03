package parsso.idman.impls.users.oprations.retrieve;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import parsso.idman.impls.Parameters;
import parsso.idman.impls.services.RetrieveService;
import parsso.idman.impls.users.oprations.retrieve.sub.Conditional;
import parsso.idman.impls.users.oprations.retrieve.sub.FullAttributes;
import parsso.idman.impls.users.oprations.retrieve.sub.MainAttributes;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.models.users.User.ListUsers;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;

import java.util.List;

@Service
public class RetrieveUser extends Parameters implements UsersRetrieveRepo {

  @Autowired
  public RetrieveUser(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate) {
    super(ldapTemplate, mongoTemplate);
  }

  @Override
  public ListUsers mainAttributes(int page, int number, String sort, String role, String userId, String displayName) {
    return new MainAttributes(mongoTemplate, ldapTemplate).get(page, number, sort, role,userId,displayName);
  }

  @Override
  public List<User> fullAttributes() {
    return new FullAttributes(ldapTemplate, mongoTemplate).get();
  }

  @Override
  public User retrieveUsers(String userId) {
    return new FullAttributes(ldapTemplate, mongoTemplate).get(userId);
  }

  @Override
  public User retrieveUsersWithLicensed(String userId) {
    return new Conditional(this, mongoTemplate, new RetrieveService(mongoTemplate)).licensed(userId);
  }

  @Override
  public UsersExtraInfo retrieveUserMain(String userId) {
    return new MainAttributes(mongoTemplate, ldapTemplate).get(userId);
  }

  @Override
  public List<UsersExtraInfo> retrieveUsersGroup(String groupId) {
    return new Conditional(ldapTemplate).group(groupId);
  }

  @Override
  public User.ListUsers retrieveUsersMainWithGroupId(String groupId, int page, int nRec) {
    return new Conditional(ldapTemplate).group(groupId, page, nRec);
  }

  @Override
  public User.ListUsers mainAttributes(int page, int number, String sortType, String groupFilter, String searchUid,
      String searchDisplayName, String mobile, String userStatus) { 
    return new MainAttributes(mongoTemplate).get(page, number, sortType, groupFilter,
        searchUid, searchDisplayName, mobile, userStatus);
  }

  @Override
  public int retrieveUsersLDAPSize() {
    // TODO Auto-generated method stub
    return 0;
  }
}
