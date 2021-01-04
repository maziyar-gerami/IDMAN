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
        List<User> people = ldapTemplate.search(query().where("mobile").is(mobile), new UserAttributeMapper(false));
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

    private class UserAttributeMapper implements AttributesMapper<User> {
        private final boolean showToken;

        private UserAttributeMapper(boolean showToken) {
            this.showToken = showToken;
        }


        @Override
        public User mapFromAttributes(Attributes attributes) throws NamingException {
            User user = new User();

            user.setUserId(null != attributes.get("uid") ? attributes.get("uid").get().toString() : null);
            user.setFirstName(null != attributes.get("givenName") ? attributes.get("givenName").get().toString() : null);
            user.setLastName(null != attributes.get("sn") ? attributes.get("sn").get().toString() : null);
            user.setDisplayName(null != attributes.get("displayName") ? attributes.get("displayName").get().toString() : null);
            user.setMobile(null != attributes.get("mobile") ? attributes.get("mobile").get().toString() : null);
            user.setMail(null != attributes.get("mail") ? attributes.get("mail").get().toString() : null);
            int nGroups = (null == attributes.get("ou") ? 0 : attributes.get("ou").size());
            List<String> ls = new LinkedList<>();
            for (int i = 0; i < nGroups; i++) ls.add(attributes.get("ou").get(i).toString());
            if (user.getTokens()!=null)
            user.getTokens().setResetPassToken(null != attributes.get("resetPassToken") ? attributes.get("resetPassToken").get().toString() : null);
            user.setMemberOf(null != attributes.get("ou") ? ls : null);
            user.setDescription(null != attributes.get("description") ? attributes.get("description").get().toString() : null);
            user.setPhotoName(null != attributes.get("photoName") && "" != attributes.get("photoName").toString() ? attributes.get("photoName").get().toString() : null);

            if (user.getTokens()!=null)
                user.getTokens().setMobileToken(null != attributes.get("mobileToken") ? attributes.get("mobileToken").get().toString() : null);

            user.setEndTime(null != attributes.get("pwdEndTime") ? Time.setEndTime(attributes.get("pwdEndTime").get().toString()) : null);


            if (null != attributes.get("pwdAccountLockedTime")) {

                if (attributes.get("pwdAccountLockedTime").get().toString().equals("40400404040404.950Z")) {
                    user.setEnabled(false);
                    user.setLocked(false);
                    user.setStatus("disabled");

                } else {
                    user.setEnabled(true);
                    user.setLocked(true);
                    user.setStatus("locked");

                }
            } else {
                user.setEnabled(true);
                user.setLocked(false);
                user.setStatus("active");

            }

            return user;
        }
    }

}
