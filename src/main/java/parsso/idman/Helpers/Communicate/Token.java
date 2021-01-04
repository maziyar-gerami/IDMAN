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
import parsso.idman.Helpers.User.BuildDn;
import parsso.idman.Models.Tokens;
import parsso.idman.Models.User;
import parsso.idman.Repos.UserRepo;

import javax.naming.Name;
import javax.naming.directory.SearchControls;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
public class Token {

    public static String collection = "IDMAN_Tokens";
    @Autowired
    private UserRepo userRepo;
    @Value("${token.valid.email}")
    private int EMAIL_VALID_TIME;
    @Value("${token.valid.SMS}")
    private int SMS_VALID_TIME;
    @Value("${sms.validation.digits}")
    private int SMS_VALIDATION_DIGITS;
    @Autowired
    BuildAttributes buildAttributes;
    @Autowired
    LdapTemplate ldapTemplate;
    @Autowired
    Email emailClass;
    @Autowired
    Message message;
    @Autowired
    BuildDn buildDn;
    @Autowired
    MongoTemplate mongoTemplate;

    public HttpStatus checkToken(String userId, String token) {
        // return OK or code 200: token is valid and time is ok
        // return requestTimeOut or error 408: token is valid but time is not ok
        // return forbidden or error code 403: token is not valid
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        User user = userRepo.retrieveUser(userId);

        String mainDbToken = user.getTokens().getResetPassToken();
        String mainPartToken;

        if (token.length() > 30)
            mainPartToken = mainDbToken.substring(0, 36);
        else
            mainPartToken = mainDbToken.substring(0, SMS_VALIDATION_DIGITS);


        if (token.equals(mainPartToken)) {

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            long cTimeStamp = currentTimestamp.getTime();

            if (mainPartToken.length() > 30) {

                String timeStamp = user.getTokens().getResetPassToken().substring(mainDbToken.indexOf(user.getUserId()) + user.getUserId().length());

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
        Tokens tokens = mongoTemplate.findOne(query, Tokens.class,collection);
        tokens.setResetPassToken(token);

        if(user.getTokens()!=null)
            user.getTokens().setResetPassToken(token);
        else {

            user.setTokens(tokens);
        }
        Name dn = buildDn.buildDn(user.getUserId());
        // context = buildAttributes.buildAttributes(user.getUserId(), user, dn);

        try {
            //ldapTemplate.modifyAttributes((DirContextOperations) context);
            mongoTemplate.save(tokens, collection);
            return HttpStatus.OK;
        }catch (Exception e){
            e.printStackTrace();
            return  HttpStatus.BAD_REQUEST;
        }

    }

    public int createRandomNum(){
        Random rnd = new Random();
        return  (int) (Math.pow(10, (SMS_VALIDATION_DIGITS - 1)) + rnd.nextInt((int) (Math.pow(10, SMS_VALIDATION_DIGITS - 1) - 1)));
    }

    public boolean insertMobileToken(User user) {

        Query query = new Query(Criteria.where("userId").is(user.getUserId()));

        Tokens tokens = mongoTemplate.findOne(query, Tokens.class,collection);

        int token = createRandomNum();

        tokens.setResetPassToken(String.valueOf(token)+new Date().getTime());
        tokens.setUserId(user.getUserId());

        if(user.getTokens()!=null)
            user.getTokens().setResetPassToken(String.valueOf(token));
        else {
            tokens.setResetPassToken(String.valueOf(token));
            user.setTokens(tokens);
        }
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        long cTimeStamp = currentTimestamp.getTime();
        user.getTokens().setResetPassToken(String.valueOf(token) + cTimeStamp);


        try {
            //userRepo.update(user.getUserId(), user);
            mongoTemplate.save(tokens, collection);

        } catch (Exception e) {
            return false;
        }

        return true;
    }



    public int requestToken(User user) {
        return message.sendMessage(user.getMobile());
    }
}
