package parsso.idman.helpers.communicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Service
public class Token {
  public static final String collection = Variables.col_usersExtraInfo;

  private final MongoTemplate mongoTemplate;
  private final UsersRetrieveRepo usersOpRetrieve;

  @Autowired
  public Token(MongoTemplate mongoTemplate, UsersRetrieveRepo userOp) {
    this.mongoTemplate = mongoTemplate;
    this.usersOpRetrieve = userOp;
  }

  public HttpStatus checkToken(String userId, String token) {

    User user = usersOpRetrieve.retrieveUsers(userId);
    String mainDbToken = user.getUsersExtraInfo().getResetPassToken();
    String mainPartToken;
    int SMS_VALIDATION_DIGITS = Integer
        .parseInt(new Settings(mongoTemplate).retrieve(Variables.SMS_VALIDATION_DIGITS).getValue().toString());

    if (token.length() > 30)
      mainPartToken = mainDbToken.substring(0, 36);
    else
      mainPartToken = mainDbToken.substring(0, SMS_VALIDATION_DIGITS);

    if (token.equals(mainPartToken)) {

      Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
      long cTimeStamp = currentTimestamp.getTime();

      if (mainPartToken.length() > 30) {

        String timeStamp = user.getUsersExtraInfo().getResetPassToken()
            .substring(mainDbToken.indexOf(user.get_id().toString()) + user.get_id().toString().length());

        if ((cTimeStamp - Long.parseLong(timeStamp)) < (60000L
            * Long.parseLong(new Settings(mongoTemplate).retrieve(Variables.TOKEN_VALID_EMAIL).getValue().toString())))
          return HttpStatus.OK;

        else
          return HttpStatus.REQUEST_TIMEOUT;
      } else {
        String timeStamp = mainDbToken.substring(SMS_VALIDATION_DIGITS);
        if ((cTimeStamp - Long.parseLong(timeStamp)) < (60000L * Integer.parseInt(
            new Settings(mongoTemplate).retrieve(Variables.TOKEN_VALID_SMS).getValue().toString()))) {
          return HttpStatus.OK;
        } else
          return HttpStatus.REQUEST_TIMEOUT;

      }

    } else
      return HttpStatus.FORBIDDEN;

  }

  private String passwordResetToken(String userId) {

    Date date = new Date();

    return UUID.randomUUID().toString().toUpperCase()
        + userId
        + date.getTime();
  }

  public void insertEmailToken(User user) {

    Query query = new Query(Criteria.where("_id").is(user.get_id()));

    String token = passwordResetToken(user.get_id().toString());
    UsersExtraInfo usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class, collection);
    Objects.requireNonNull(usersExtraInfo).setResetPassToken(token);

    if (user.getUsersExtraInfo() != null)
      user.getUsersExtraInfo().setResetPassToken(token);
    else {

      user.setUsersExtraInfo(usersExtraInfo);
    }

    try {
      mongoTemplate.save(usersExtraInfo, collection);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public int createRandomNum() {
    int SMS_VALIDATION_DIGITS = Integer
        .parseInt(new Settings(mongoTemplate).retrieve(Variables.SMS_VALIDATION_DIGITS).getValue().toString());
    Random rnd = new Random();
    int max = (int) (Math.pow(10, (SMS_VALIDATION_DIGITS)));
    int min = (int) (Math.pow(10, (SMS_VALIDATION_DIGITS - 1))) + 1;
    return min + rnd.nextInt(max - min);
  }

  public boolean insertMobileToken(User user) {

    Query query = new Query(Criteria.where("_id").is(user.get_id()));

    UsersExtraInfo usersExtraInfo;

    try {
      usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class, collection);
      if (usersExtraInfo == null)
        usersExtraInfo = new UsersExtraInfo(user);
    } catch (NullPointerException e) {
      usersExtraInfo = new UsersExtraInfo(user);
    }

    int token = createRandomNum();

    Objects.requireNonNull(usersExtraInfo).setResetPassToken(String.valueOf(token) + new Date().getTime());
    usersExtraInfo.setUserId(user.get_id().toString());

    if (user.getUsersExtraInfo() != null)
      user.getUsersExtraInfo().setResetPassToken(String.valueOf(token));
    else {
      usersExtraInfo.setResetPassToken(String.valueOf(token));
      user.setUsersExtraInfo(usersExtraInfo);
    }
    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
    long cTimeStamp = currentTimestamp.getTime();
    user.getUsersExtraInfo().setResetPassToken(String.valueOf(token) + cTimeStamp);

    try {
      mongoTemplate.save(usersExtraInfo, collection);
    } catch (Exception e) {
      return false;
    }

    return true;
  }
}
