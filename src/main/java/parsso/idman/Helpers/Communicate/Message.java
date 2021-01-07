package parsso.idman.Helpers.Communicate;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import parsso.idman.Captcha.Models.CAPTCHA;
import parsso.idman.Helpers.User.UserAttributeMapper;
import parsso.idman.Models.Time;
import parsso.idman.Models.User;
import parsso.idman.Repos.UserRepo;
import parsso.idman.Utils.SMS.sdk.KavenegarApi;
import parsso.idman.Utils.SMS.sdk.excepctions.ApiException;
import parsso.idman.Utils.SMS.sdk.excepctions.HttpException;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Configuration
public class Message {

    @Autowired
    private UserRepo userRepo;
    @Value("${token.valid.SMS}")
    private String SMS_VALID_TIME;
    @Value("${sms.validation.digits}")
    private String SMS_VALIDATION_DIGITS;
    @Value("${sms.api.key}")
    private String SMS_API_KEY;

    @Autowired
    private LdapTemplate ldapTemplate;

    @Autowired
    private Token tokenClass;

    @Autowired
    MongoTemplate mongoTemplate;

    private final String collection = "IDMAN_Captchas";

    @Autowired
    private parsso.idman.Helpers.User.UserAttributeMapper userAttributeMapper;



    public int sendMessage(String mobile,String cid, String answer) {
        Query query = new Query(Criteria.where("_id").is(cid));
        CAPTCHA captcha = mongoTemplate.findOne(query, CAPTCHA.class , collection);
        if(captcha==null)
            return -1;
        if (!(answer.equalsIgnoreCase(captcha.getPhrase()))) {
            mongoTemplate.remove(query,collection);
            return -1;
        }
        if (checkMobile(mobile).size() > 0) {
            User user = userRepo.retrieveUser(checkMobile(mobile).get(0).getAsString("userId"));
            if (tokenClass.insertMobileToken(user)) {
                try {
                    String receptor = mobile;
                    String message = user.getTokens().getResetPassToken().substring(0, Integer.valueOf(SMS_VALIDATION_DIGITS));
                    KavenegarApi api = new KavenegarApi(SMS_API_KEY);
                    api.verifyLookup(receptor, message, "", "", "mfa");
                    mongoTemplate.remove(query,collection);
                    return Integer.valueOf(SMS_VALID_TIME);

                } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
                    System.out.print("HttpException  : " + ex.getMessage());
                    return 0;
                } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
                    System.out.print("ApiException : " + ex.getMessage());
                    return 0;
                }
            } else
                return 0;

        } else
            return 0;
    }

    public int sendMessage(String mobile) {

        if (checkMobile(mobile).size() > 0) {
            User user = userRepo.retrieveUser(checkMobile(mobile).get(0).getAsString("userId"));
            if (tokenClass.insertMobileToken(user)) {
                try {
                    String receptor = mobile;
                    String message = user.getTokens().getResetPassToken().substring(0, Integer.valueOf(SMS_VALIDATION_DIGITS));
                    KavenegarApi api = new KavenegarApi(SMS_API_KEY);
                    api.verifyLookup(receptor, message, "", "", "mfa");
                    return Integer.valueOf(SMS_VALID_TIME);

                } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
                    System.out.print("HttpException  : " + ex.getMessage());
                    return 0;
                } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
                    System.out.print("ApiException : " + ex.getMessage());
                    return 0;
                }
            } else
                return 0;

        } else
            return 0;
    }



    public List<JSONObject> checkMobile(String mobile) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<User> people = ldapTemplate.search(query().where("mobile").is(mobile), userAttributeMapper);
        List<JSONObject> jsonArray = new LinkedList<>();
        JSONObject jsonObject;
        for (User user : people) {
            jsonObject = new JSONObject();
            jsonObject.put("userId", user.getUserId());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }



    public int sendMessage(String mobile, String uId, String cid, String answer) {
        Query query = new Query(Criteria.where("_id").is(cid));
        CAPTCHA captcha = mongoTemplate.findOne(query, CAPTCHA.class , collection);
        if(captcha==null)
            return -1;
        if (!(answer.equalsIgnoreCase(captcha.getPhrase()))) {
            mongoTemplate.remove(query,collection);
            return -1;
        }

        if (checkMobile(mobile) != null & userRepo.retrieveUser(uId).getUserId() != null) {
            List<JSONObject> ids = checkMobile(mobile);
            List<User> people = new LinkedList<>();
            User user = userRepo.retrieveUser(uId);
            for (JSONObject id : ids) people.add(userRepo.retrieveUser(id.getAsString("userId")));

            for (User p : people) {
                if (p.getUserId().equals(user.getUserId())) {

                    if (tokenClass.insertMobileToken(user)) {

                        try {
                            String receptor = mobile;
                            String message = user.getTokens().getResetPassToken().substring(0, Integer.valueOf(SMS_VALIDATION_DIGITS));
                            KavenegarApi api = new KavenegarApi(SMS_API_KEY);
                            api.verifyLookup(receptor, message, "", "", "mfa");
                            mongoTemplate.remove(query,collection);
                            return Integer.valueOf(SMS_VALID_TIME);
                        } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
                            System.out.print("HttpException  : " + ex.getMessage());
                            return 0;

                        } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
                            System.out.print("ApiException : " + ex.getMessage());
                            return 0;

                        }

                    }


                }
            }

        } else
            return 0;

        return 0;

    }


}
