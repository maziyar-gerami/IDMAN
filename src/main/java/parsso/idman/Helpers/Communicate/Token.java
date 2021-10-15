package parsso.idman.Helpers.Communicate;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.User.BuildAttributes;
import parsso.idman.Helpers.User.BuildDnUser;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Users.User;
import parsso.idman.Models.Users.UsersExtraInfo;
import parsso.idman.repos.UserRepo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Service
public class Token {
    public static final String collection = Variables.col_usersExtraInfo;
    @Autowired
    BuildAttributes buildAttributes;
    @Autowired
    LdapTemplate ldapTemplate;
    @Autowired
    InstantMessage instantMessage;
    @Autowired
    BuildDnUser buildDnUser;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    private UserRepo userRepo;
    @Value("${token.valid.email}")
    private int EMAIL_VALID_TIME;
    @Value("${token.valid.SMS}")
    private int SMS_VALID_TIME;
    @Value("${sms.validation.digits}")
    private int SMS_VALIDATION_DIGITS;

    public HttpStatus checkToken(String userId, String token) {
        // return OK or code 200: token is valid and time is ok
        // return requestTimeOut or error 408: token is valid but time is not ok
        // return forbidden or error code 403: token is not valid

        User user = userRepo.retrieveUsers(userId);

        String mainDbToken = user.getUsersExtraInfo().getResetPassToken();
        String mainPartToken;

        if (token.length() > 30)
            mainPartToken = mainDbToken.substring(0, 36);
        else
            mainPartToken = mainDbToken.substring(0, SMS_VALIDATION_DIGITS);

        if (token.equals(mainPartToken)) {

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            long cTimeStamp = currentTimestamp.getTime();

            if (mainPartToken.length() > 30) {

                String timeStamp = user.getUsersExtraInfo().getResetPassToken().substring(mainDbToken.indexOf(user.getUserId()) + user.getUserId().length());

                if ((cTimeStamp - Long.parseLong(timeStamp)) < (60000L * EMAIL_VALID_TIME))
                    return HttpStatus.OK;

                else
                    return HttpStatus.REQUEST_TIMEOUT;
            } else {
                String timeStamp = mainDbToken.substring(SMS_VALIDATION_DIGITS);
                if ((cTimeStamp - Long.parseLong(timeStamp)) < (60000L * SMS_VALID_TIME)) {
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

        Query query = new Query(Criteria.where("userId").is(user.getUserId()));

        String token = passwordResetToken(user.getUserId());
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
        Random rnd = new Random();
        int max = (int) (Math.pow(10, (SMS_VALIDATION_DIGITS)));
        int min = (int) (Math.pow(10, (SMS_VALIDATION_DIGITS - 1))) + 1;
        return min + rnd.nextInt(max - min);
    }

    public boolean insertMobileToken(User user) {

        Query query = new Query(Criteria.where("userId").is(user.getUserId()));

        UsersExtraInfo usersExtraInfo;

        try {
            usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class, collection);
        } catch (NullPointerException e) {
            usersExtraInfo = new UsersExtraInfo(user);
        }

        int token = createRandomNum();

        Objects.requireNonNull(usersExtraInfo).setResetPassToken(String.valueOf(token) + new Date().getTime());
        usersExtraInfo.setUserId(user.getUserId());

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

    public int requestToken(User user) {
        return instantMessage.sendMessage(user);
    }
}
