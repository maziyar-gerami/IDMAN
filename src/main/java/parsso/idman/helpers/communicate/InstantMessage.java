package parsso.idman.helpers.communicate;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.UserAttributeMapper;
import parsso.idman.models.users.User;
import parsso.idman.repos.UserRepo;
import parsso.idman.utils.captcha.models.CAPTCHA;
import parsso.idman.utils.sms.kaveNegar.KavenegarApi;
import parsso.idman.utils.sms.kaveNegar.excepctions.ApiException;
import parsso.idman.utils.sms.kaveNegar.excepctions.HttpException;
import parsso.idman.utils.sms.magfa.Texts;
import java.util.LinkedList;
import java.util.List;

@Configuration
public class InstantMessage {

    @Autowired
    public InstantMessage(MongoTemplate mongoTemplate, LdapTemplate ldapTemplate, UserRepo userRepo, Token tokenClass, UserAttributeMapper userAttributeMapper){
        this.mongoTemplate = mongoTemplate;
        this.ldapTemplate = ldapTemplate;
        this.tokenClass = tokenClass;
        this.userAttributeMapper = userAttributeMapper;
        this.userRepo = userRepo;
    }
    private final MongoTemplate mongoTemplate;
    private final UserRepo userRepo;
    private final LdapTemplate ldapTemplate;
    private final Token tokenClass;
    private final UserAttributeMapper userAttributeMapper;

    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;

    public int sendMessage(String mobile, String cid, String answer) {

        if (SMS_SDK().equalsIgnoreCase("KaveNegar"))
            return sendMessageKaveNegar(mobile, cid, answer);
        else if (SMS_SDK().equalsIgnoreCase("Magfa"))
            return sendMessageMagfa(mobile, cid, answer);
        return 0;
    }

    public int sendMessage(User user, String cid, String answer) {

        if (SMS_SDK().equalsIgnoreCase("KaveNegar"))
            return sendMessageKaveNegar(user, cid, answer);
        else if (SMS_SDK().equalsIgnoreCase("Magfa"))
            return sendMessageMagfa(user, cid, answer);
        return 0;
    }

    public int sendMessage(User user) {
        if (SMS_SDK().equalsIgnoreCase("KaveNegar"))
            return sendMessageKaveNegar(user);
        else if (SMS_SDK().equalsIgnoreCase("Magfa"))
            return sendMessageMagfa(user);
        return 0;
    }

    public int sendMessage(String mobile, String uid, String cid, String answer) {
        if (SMS_SDK().equalsIgnoreCase("KaveNegar"))
            return sendMessageKaveNegar(mobile, uid, cid, answer);
        else if (SMS_SDK().equalsIgnoreCase("Magfa"))
            return sendMessageMagfa(mobile, uid, cid, answer);
        return 0;
    }

    private int sendMessageKaveNegar(String mobile, String cid, String answer) {

        Query query = new Query(Criteria.where("_id").is(cid));
        CAPTCHA captcha = mongoTemplate.findOne(query, CAPTCHA.class, Variables.col_captchas);
        if (captcha == null)
            return -1;

        if (!(answer.equalsIgnoreCase(captcha.getPhrase())))
            mongoTemplate.remove(query, Variables.col_captchas);

        User user;

        List<JSONObject> checked = checkMobile(mobile);

        if (checked.size() == 0)
            return -3;

        else if (checked.size() > 1)
            return -2;

        else user = userRepo.retrieveUsers(checkMobile(mobile).get(0).getAsString("userId"));

        if (tokenClass.insertMobileToken(user)) {
            return keveNegar_insertTokenAndSend(mobile, query, user);
        }

        return 0;


    }

    private int keveNegar_insertTokenAndSend(String mobile, Query query, User user) {
        try {

            String message = user.getUsersExtraInfo().getResetPassToken().substring(0, Integer.parseInt(new Settings(mongoTemplate).retrieve(Variables.SMS_VALIDATION_DIGITS).getValue().toString()));
            KavenegarApi api = new KavenegarApi(new Settings(mongoTemplate).retrieve(Variables.KAVENEGAR_API_KEY).getValue().toString());
            api.verifyLookup(mobile, message, "", "", "mfa");
            mongoTemplate.remove(query, Variables.col_captchas);
            return Integer.parseInt(new Settings(mongoTemplate).retrieve(Variables.TOKEN_VALID_SMS).getValue().toString());

        } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.

            System.out.print("HttpException  : " + ex.getMessage());
            return 0;
        } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.

            System.out.print("ApiException : " + ex.getMessage());
            return 0;
        }
    }

    private int sendMessageKaveNegar(User user, String cid, String answer) {

        Query query = new Query(Criteria.where("_id").is(cid));
        CAPTCHA captcha = mongoTemplate.findOne(query, CAPTCHA.class, Variables.col_captchas);
        if (captcha == null)
            return -1;

        if (!(answer.equalsIgnoreCase(captcha.getPhrase())))
            mongoTemplate.remove(query, Variables.col_captchas);

        if (user == null || user.getUserId() == null)
            return -3;

        if (tokenClass.insertMobileToken(user)) {
            try {

                String receptor = user.getMobile();
                String message = user.getUsersExtraInfo().getResetPassToken().substring(0, Integer.parseInt(new Settings(mongoTemplate).retrieve(Variables.SMS_VALIDATION_DIGITS).getValue().toString()));
                KavenegarApi api = new KavenegarApi(new Settings(mongoTemplate).retrieve(Variables.KAVENEGAR_API_KEY).getValue().toString());
                api.verifyLookup(receptor, message, "", "", "mfa");
                mongoTemplate.remove(query, Variables.col_captchas);
                return Integer.parseInt(new Settings(mongoTemplate).retrieve(Variables.TOKEN_VALID_SMS).getValue().toString());

            } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.

                System.out.print("HttpException  : " + ex.getMessage());
                return 0;
            } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.

                System.out.print("ApiException : " + ex.getMessage());
                return 0;
            }
        }

        return 0;

    }

    private int sendMessageMagfa(User user, String cid, String answer) {

        Query query = new Query(Criteria.where("_id").is(cid));
        CAPTCHA captcha = mongoTemplate.findOne(query, CAPTCHA.class, Variables.col_captchas);
        if (captcha == null)
            return -1;

        if (!(answer.equalsIgnoreCase(captcha.getPhrase()))) {
            mongoTemplate.remove(query, Variables.col_captchas);
            return -1;
        }

        if (user == null || user.getUserId() == null)
            return -3;

        if (tokenClass.insertMobileToken(user)) {

            try {

                Texts texts = new Texts();
                texts.authorizeMessage(user.getUsersExtraInfo().getResetPassToken().substring(0, Integer.parseInt(new Settings(mongoTemplate).retrieve(Variables.SMS_VALIDATION_DIGITS).getValue().toString())));
                new MagfaSMS(mongoTemplate,texts.getMainMessage()).SendMessage(user.getMobile(), 1L);

                mongoTemplate.remove(query, Variables.col_captchas);
                return Integer.parseInt(new Settings(mongoTemplate).retrieve(Variables.TOKEN_VALID_SMS).getValue().toString());

            } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
                System.out.print("HttpException  : " + ex.getMessage());
                return 0;
            } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
                System.out.print("ApiException : " + ex.getMessage());
                return 0;
            }
        } else
            return 0;

    }

    private int sendMessageMagfa(String mobile, String cid, String answer) {

        Query query = new Query(Criteria.where("_id").is(cid));
        CAPTCHA captcha = mongoTemplate.findOne(query, CAPTCHA.class, Variables.col_captchas);
        if (captcha == null)
            return -1;

        if (!(answer.equalsIgnoreCase(captcha.getPhrase()))) {
            mongoTemplate.remove(query, Variables.col_captchas);
            return -1;
        }

        User user = new User();

        if (checkMobile(mobile).size() == 0)
            return -3;

        else if (checkMobile(mobile).size() > 1)
            return -2;

        else if (checkMobile(mobile).size() == 1)
            user = userRepo.retrieveUsers(checkMobile(mobile).get(0).getAsString("userId"));

        if (tokenClass.insertMobileToken(user)) {

            try {

                Texts texts = new Texts();
                texts.authorizeMessage(user.getUsersExtraInfo().getResetPassToken().substring(0, Integer.parseInt(new Settings(mongoTemplate).retrieve(Variables.SMS_VALIDATION_DIGITS).getValue().toString())));
                new MagfaSMS(mongoTemplate,texts.getMainMessage()).SendMessage( user.getMobile(), 1L);

                mongoTemplate.remove(query, Variables.col_captchas);
                return Integer.parseInt(new Settings(mongoTemplate).retrieve(Variables.TOKEN_VALID_SMS).getValue().toString());

            } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
                System.out.print("HttpException  : " + ex.getMessage());
                return 0;
            } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
                System.out.print("ApiException : " + ex.getMessage());
                return 0;
            }
        } else
            return 0;

    }

    private int sendMessageKaveNegar(User user) {

        if (checkMobile(user.getMobile()).size() > 0) {
            if (tokenClass.insertMobileToken(user)) {
                try {
                    String receptor = user.getMobile();
                    String message = user.getUsersExtraInfo().getResetPassToken().substring(0, Integer.parseInt(new Settings(mongoTemplate).retrieve(Variables.SMS_VALIDATION_DIGITS).getValue().toString()));
                    KavenegarApi api = new KavenegarApi(new Settings(mongoTemplate).retrieve(Variables.KAVENEGAR_API_KEY).getValue().toString());
                    api.verifyLookup(receptor, message, "", "", "mfa");
                    return Integer.parseInt(new Settings(mongoTemplate).retrieve(Variables.TOKEN_VALID_SMS).getValue().toString());

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

    private int sendMessageMagfa(User user) {

        if (checkMobile(user.getMobile()).size() > 0) {
            if (tokenClass.insertMobileToken(user)) {
                try {
                    Texts texts = new Texts();
                    texts.authorizeMessage(user.getUsersExtraInfo().getResetPassToken().substring(0, Integer.parseInt(new Settings(mongoTemplate).retrieve(Variables.TOKEN_VALID_SMS).getValue().toString())));
                    new MagfaSMS(mongoTemplate,texts.getMainMessage()).SendMessage(user.getMobile(), 1L);

                    return Integer.parseInt(new Settings(mongoTemplate).retrieve(Variables.TOKEN_VALID_SMS).getValue().toString());

                } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
                    System.out.print("HttpException  : " + ex.getMessage());
                    return 0;
                } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
                    System.out.print("ApiException : " + ex.getMessage());
                    return 0;
                }
            } else
                return 0;

        }
        return 0;
    }

    public List<JSONObject> checkMobile(String mobile) {

        List<User> people = ldapTemplate.search(BASE_DN, new EqualsFilter("mobile", mobile).encode(), userAttributeMapper);
        List<JSONObject> jsonArray = new LinkedList<>();
        JSONObject jsonObject;
        for (User user : people) {
            jsonObject = new JSONObject();
            jsonObject.put("userId", user.getUserId());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private int sendMessageKaveNegar(String mobile, String uId, String cid, String answer) {

        Query query = new Query(Criteria.where("_id").is(cid));
        CAPTCHA captcha = mongoTemplate.findOne(query, CAPTCHA.class, Variables.col_captchas);
        if (captcha == null)
            return -1;

        if (!(answer.equalsIgnoreCase(captcha.getPhrase()))) {
            mongoTemplate.remove(query, Variables.col_captchas);
            return -1;
        }

        if (checkMobile(mobile).size() == 0)
            return -3;

        User user = userRepo.retrieveUsers(uId);

        if (user != null && user.getUserId() != null && tokenClass.insertMobileToken(user)) {
            List<JSONObject> ids = checkMobile(mobile);
            List<User> people = new LinkedList<>();
            for (JSONObject id : ids) people.add(userRepo.retrieveUsers(id.getAsString("userId")));

            for (User p : people)
                if (p.getUserId().equals(user.getUserId()))
                    if (tokenClass.insertMobileToken(user))
                        return keveNegar_insertTokenAndSend(mobile, query, user);
        }
        return 0;
    }

    private int sendMessageMagfa(String mobile, String uId, String cid, String answer) {
        Query query = new Query(Criteria.where("_id").is(cid));
        CAPTCHA captcha = mongoTemplate.findOne(query, CAPTCHA.class, Variables.col_captchas);
        if (captcha == null)
            return -1;
        if (!(answer.equalsIgnoreCase(captcha.getPhrase()))) {
            mongoTemplate.remove(query, Variables.col_captchas);
            return -1;
        }

        User user;

        if (checkMobile(mobile).size() == 0) {

            return -3;
        }
        user = userRepo.retrieveUsers(uId);

        if (user == null)
            return 0;

        if (checkMobile(mobile) != null && userRepo.retrieveUsers(uId).getUserId() != null && tokenClass.insertMobileToken(user)) {

            List<JSONObject> ids = checkMobile(mobile);
            List<User> people = new LinkedList<>();
            user = userRepo.retrieveUsers(uId);
            for (JSONObject id : ids) people.add(userRepo.retrieveUsers(id.getAsString("userId")));

            for (User p : people) {
                if (p.getUserId().equals(user.getUserId())) {

                    if (tokenClass.insertMobileToken(user)) {

                        try {
                            Texts texts = new Texts();
                            texts.authorizeMessage(user.getUsersExtraInfo().getResetPassToken().substring(0, Integer.parseInt(new Settings(mongoTemplate).retrieve(Variables.SMS_VALIDATION_DIGITS).getValue().toString())));
                            new MagfaSMS(mongoTemplate,texts.getMainMessage()).SendMessage(user.getMobile(), 1L);
                            mongoTemplate.remove(query, Variables.col_captchas);
                            return Integer.parseInt(new Settings(mongoTemplate).retrieve(Variables.TOKEN_VALID_SMS).getValue().toString());
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

        } else {
            return 0;

        }

        return 0;
    }

    private String SMS_SDK(){
        return new Settings(mongoTemplate).retrieve(Variables.SMS_SDK).getValue().toString();
    }
}