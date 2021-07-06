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
import parsso.idman.Repos.UserRepo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
public class Token {

    public static String collection = Variables.col_usersExtraInfo;
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

                if ((cTimeStamp - Long.valueOf(timeStamp)) < (60000 * EMAIL_VALID_TIME))
                    return HttpStatus.OK;

                else
                    return HttpStatus.REQUEST_TIMEOUT;
            } else {
                String timeStamp = mainDbToken.substring(SMS_VALIDATION_DIGITS);
                if ((cTimeStamp - Long.valueOf(timeStamp)) < (60000 * SMS_VALID_TIME)) {
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

    public HttpStatus insertEmailToken(User user) {

        Query query = new Query(Criteria.where("userId").is(user.getUserId()));

        String token = passwordResetToken(user.getUserId());
        UsersExtraInfo usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class, collection);
        usersExtraInfo.setResetPassToken(token);

        if (user.getUsersExtraInfo() != null)
            user.getUsersExtraInfo().setResetPassToken(token);
        else {

            user.setUsersExtraInfo(usersExtraInfo);
        }

        try {
            mongoTemplate.save(usersExtraInfo, collection);
            return HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return HttpStatus.BAD_REQUEST;
        }

    }

    public int createRandomNum() {
        Random rnd = new Random();
        return (int) (Math.pow(10, (SMS_VALIDATION_DIGITS - 1)) + rnd.nextInt((int) (Math.pow(10, SMS_VALIDATION_DIGITS - 1) - 1)));
    }

    public boolean insertMobileToken(User user) {

        Query query = new Query(Criteria.where("userId").is(user.getUserId()));

        UsersExtraInfo usersExtraInfo = mongoTemplate.findOne(query, UsersExtraInfo.class, collection);

        int token = createRandomNum();

        usersExtraInfo.setResetPassToken(String.valueOf(token) + new Date().getTime());
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
            userRepo.update(user.getUserId(), user.getUserId(), user);
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
