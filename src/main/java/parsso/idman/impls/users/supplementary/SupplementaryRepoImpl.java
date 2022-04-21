package parsso.idman.impls.users.supplementary;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.communicate.Token;
import parsso.idman.models.users.ChangePassword;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.UserRepo;



@Service
public class SupplementaryRepoImpl implements UserRepo.Supplementary {
  final MongoTemplate mongoTemplate;
  final LdapTemplate ldapTemplate;
  final UserRepo.UsersOp.Retrieve usersOpRetrieve;

  @Value("${spring.ldap.base.dn}")
  private String BASE_DN;

  @Value("${base.url}")
  private String BASE_URL;

  @Autowired
  SupplementaryRepoImpl(MongoTemplate mongoTemplate, LdapTemplate ldapTemplate,
      UserRepo.UsersOp.Retrieve usersOpRetrieve) {
    this.mongoTemplate = mongoTemplate;
    this.ldapTemplate = ldapTemplate;
    this.usersOpRetrieve = usersOpRetrieve;
  }

  @Override
  public boolean accessChangePassword(User user) {

    if (new Settings(mongoTemplate).retrieve(
          Variables.PASSWORD_CHANGE_LIMIT).getValue().equals("on")) {
      if (sameDayPasswordChanges(new Date().getTime(),
           user.getUsersExtraInfo().getChangePassword().getTime())) {
        if (user.getUsersExtraInfo().getChangePassword().getN() >= Integer
            .parseInt(new Settings(mongoTemplate).retrieve(Variables.PASSWORD_CHANGE_LIMIT_NUMBER)
                .getValue())) {
          return false;
        }
      }
    }
    return true;

  }

  @Override
  public void setIfLoggedIn() {

  }

  @Override
  public User setRole(User user) {
    return null;
  }

  @Override
  public String getByMobile(String mobile) {
    return null;
  }

  @Override
  public int sendEmail(String email, String uid, String cid, String answer) {
    return 0;
  }

  @Override
  public String createUrl(String userId, String token) {
    return BASE_URL + /* "" + */ "/api/public/validateEmailToken/" + userId + "/" + token;
  }

  @Override
  public Boolean SAtoSU() {
    return null;
  }

  @Override
  public int authenticate(String userId, String password) {
    return new Authenticate(ldapTemplate, usersOpRetrieve).authenticate(userId, password);
  }

  @Override
  public void removeCurrentEndTime(String uid) {

  }

  @Override
  public UsersExtraInfo getName(String uid, String token) {
    if (new Token(mongoTemplate, usersOpRetrieve).checkToken(uid, token) == HttpStatus.OK) {
      return usersOpRetrieve.retrieveUserMain(uid);
    }
    return null;
  }

  @Override
  public boolean increaseSameDayPasswordChanges(User user) {
    UsersExtraInfo usersExtraInfo = user.getUsersExtraInfo();
    if (user.getUsersExtraInfo().getChangePassword() == null) {
      usersExtraInfo.setChangePassword(new ChangePassword(new Date().getTime(), 1));
    }
    if (accessChangePassword(user)) {
      usersExtraInfo.setChangePassword(
          new ChangePassword(System.currentTimeMillis(), usersExtraInfo.getChangePassword().getN() + 1));
    } else {
      return false;
    }

    mongoTemplate.save(usersExtraInfo, Variables.col_usersExtraInfo);
    return true;
  }

  private boolean sameDayPasswordChanges(long last, long current) {
    Date lastDateChange = new Date(last);
    Date currentDate = new Date(current);

    LocalDate localDateLastChange 
        = lastDateChange.toInstant().atZone(ZoneId.of(Variables.ZONE)).toLocalDate();

    LocalDate localDateCurrentChange 
        = currentDate.toInstant().atZone(ZoneId.of(Variables.ZONE)).toLocalDate();

    return localDateCurrentChange.isEqual(localDateLastChange);
  }
}
