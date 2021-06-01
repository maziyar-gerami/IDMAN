package parsso.idman.Helpers.Communicate;


import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.User.UserAttributeMapper;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Users.User;
import parsso.idman.RepoImpls.UserRepoImpl;
import parsso.idman.Repos.UserRepo;
import parsso.idman.Utils.Captcha.Models.CAPTCHA;
import parsso.idman.Utils.Email.EmailSend;

import javax.naming.directory.SearchControls;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class Email {

    private final String collection = Variables.col_captchas;
    @Autowired
    LdapTemplate ldapTemplate;
    @Autowired
    UserAttributeMapper userAttributeMapper;
    @Autowired
    Token tokenClass;
    @Autowired
    UserRepo userRepo;
    @Autowired
    UserRepoImpl userRepoImp;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    EmailSend emailSend;
    @Value("${token.valid.email}")
    private String EMAIL_VALID_TIME;

    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;

    public List<JSONObject> checkMail(String email) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        List<JSONObject> jsonArray = new LinkedList<>();
        List<User> people = ldapTemplate.search("ou=People," + BASE_DN, new EqualsFilter("mail", email).encode(), userAttributeMapper);
        JSONObject jsonObject;
        for (User user : people) {
            jsonObject = new JSONObject();
            jsonObject.put("userId", user.getUserId());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public HttpStatus sendEmail(String email) {
        if (checkMail(email) != null) {
            User user = userRepo.retrieveUsers(checkMail(email).get(0).getAsString("userId"));

            tokenClass.insertEmailToken(user);

            String fullUrl = userRepo.createUrl(user.getUserId(), user.getUsersExtraInfo().getResetPassToken().substring(0, 36));


            Thread thread = new Thread() {
                public void run() {
                    emailSend.sendMail(email, user.getUserId(), user.getDisplayName(), "\n" + fullUrl);

                }
            };

            thread.start();

            return HttpStatus.OK;
        } else
            return HttpStatus.FORBIDDEN;
    }

    public HttpStatus sendEmail(JSONObject jsonObject) {
        if (jsonObject.size() == 0) {
            List<User> users = userRepo.retrieveUsersFull();

            for (User user : users)
                if (!user.getUsersExtraInfo().getRole().equals("SUPERADMIN") && user.getMail() != null && user.getMail() != null && user.getMail() != "" && user.getMail() != " ")
                    sendEmail(user.getMail());

        } else {
            ArrayList jsonArray = (ArrayList) jsonObject.get("names");
            for (Object temp : jsonArray) {

                User user = userRepo.retrieveUsers(temp.toString());
                {
                    Thread thread = new Thread() {
                        public void run() {
                            sendEmail(user.getMail());

                        }
                    };
                    if (checkMail(user.getMail()) != null)
                        thread.start();
                }
            }
        }


        return HttpStatus.OK;
    }

    public int sendEmail(String email, String cid, String answer) {

        Query query = new Query(Criteria.where("_id").is(cid));
        CAPTCHA captcha = mongoTemplate.findOne(query, CAPTCHA.class, collection);
        if ((captcha) == null)
            return -1;
        if (!(answer.equalsIgnoreCase(captcha.getPhrase()))) {
            mongoTemplate.remove(query, collection);
            return -1;
        }

        if (checkMail(email) != null) {
            User user = userRepo.retrieveUsers(checkMail(email).get(0).getAsString("userId"));

            tokenClass.insertEmailToken(user);

            String fullUrl = userRepo.createUrl(user.getUserId(), user.getUsersExtraInfo().getResetPassToken().substring(0, 36));

            Thread thread = new Thread() {
                public void run() {
                    emailSend.sendMail(email, user.getUserId(), user.getDisplayName(), "\n" + fullUrl);

                }
            };
            thread.start();
            mongoTemplate.remove(query, collection);
            return Integer.valueOf(EMAIL_VALID_TIME);
        } else
            return 0;
    }

    public int sendEmail(String email, String uid, String cid, String answer) {

        Query query = new Query(Criteria.where("_id").is(cid));
        CAPTCHA captcha = mongoTemplate.findOne(query, CAPTCHA.class, collection);
        if ((captcha) == null)
            return -1;
        if (!(answer.equalsIgnoreCase(captcha.getPhrase()))) {
            mongoTemplate.remove(query, collection);
            return -1;
        }

        if (checkMail(email) != null & userRepo.retrieveUsers(uid).getUserId() != null) {
            List<JSONObject> ids = checkMail(email);
            List<User> people = new LinkedList<>();
            User user = userRepo.retrieveUsers(uid);
            for (JSONObject id : ids) people.add(userRepo.retrieveUsers(id.getAsString("userId")));

            for (User p : people) {

                if (user.equals(p)) {

                    tokenClass.insertEmailToken(user);

                    String fullUrl = userRepoImp.createUrl(user.getUserId(), user.getUsersExtraInfo().getResetPassToken().substring(0, 36));
                    Thread thread = new Thread() {
                        public void run() {
                            emailSend.sendMail(email, user.getUserId(), user.getDisplayName(), "\n" + fullUrl);
                        }
                    };

                    thread.start();
                    mongoTemplate.remove(query, collection);
                    return Integer.valueOf(EMAIL_VALID_TIME);


                }
            }

        } else
            return 0;

        return 0;
    }


    public void sendWarnExpireMessage(User user, String day) {

        try {
            emailSend.sendMail(user, day);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
