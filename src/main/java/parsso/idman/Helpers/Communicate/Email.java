package parsso.idman.Helpers.Communicate;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Captcha.Models.CAPTCHA;
import parsso.idman.Models.User;
import parsso.idman.Helpers.User.UserAttributeMapper;
import parsso.idman.RepoImpls.UserRepoImpl;
import parsso.idman.Repos.UserRepo;
import parsso.idman.utils.Email.EmailSend;

import javax.naming.directory.SearchControls;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class Email {

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

    private final String collection = "IDMAN_Captchas";

    @Value("${token.valid.email}")
    private String EMAIL_VALID_TIME;

    public List<JSONObject> checkMail(String email) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<JSONObject> jsonArray = new LinkedList<>();
        List<User> people = ldapTemplate.search(query().where("mail").is(email), userAttributeMapper);
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
            User user = userRepo.retrieveUser(checkMail(email).get(0).getAsString("userId"));

            tokenClass.insertEmailToken(user);
            EmailSend emailSend = new EmailSend();

            String fullUrl = userRepo.createUrl(user.getUserId(), user.getTokens().getResetPassToken().substring(0, 36));

            emailSend.sendMail(email, user.getUserId(), user.getDisplayName(), "\n" + fullUrl);
            return HttpStatus.OK;
        } else
            return HttpStatus.FORBIDDEN;
    }

    public int sendEmail(String email, String cid, String answer) {

        Query query = new Query(Criteria.where("_id").is(cid));
        CAPTCHA captcha = mongoTemplate.findOne(query, CAPTCHA.class , collection);
        if((captcha)==null)
            return -1;
        if (!(answer.equalsIgnoreCase(captcha.getPhrase()))) {
            mongoTemplate.remove(query,collection);
            return -1;
        }

        if (checkMail(email) != null) {
            User user = userRepo.retrieveUser(checkMail(email).get(0).getAsString("userId"));

            tokenClass.insertEmailToken(user);
            EmailSend emailSend = new EmailSend();

            String fullUrl = userRepo.createUrl(user.getUserId(), user.getTokens().getResetPassToken().substring(0, 36));

            Thread thread = new Thread(){
                public void run(){
                    emailSend.sendMail(email, user.getUserId(), user.getDisplayName(), "\n" + fullUrl);

                }
            };
            thread.start();
            mongoTemplate.remove(query,collection);
            return Integer.valueOf(EMAIL_VALID_TIME);
        } else
            return 0;
    }

    public int sendEmail(String email, String uid,String cid, String answer) {

        Query query = new Query(Criteria.where("_id").is(cid));
        CAPTCHA captcha = mongoTemplate.findOne(query, CAPTCHA.class , collection);
        if((captcha)==null)
            return -1;
        if (!(answer.equalsIgnoreCase(captcha.getPhrase()))) {
            mongoTemplate.remove(query,collection);
            return -1;
        }

        if (checkMail(email) != null & userRepo.retrieveUser(uid).getUserId() != null) {
            List<JSONObject> ids = checkMail(email);
            List<User> people = new LinkedList<>();
            User user = userRepo.retrieveUser(uid);
            for (JSONObject id : ids) people.add(userRepo.retrieveUser(id.getAsString("userId")));

            for (User p : people) {

                if (user.equals(p)) {

                    tokenClass.insertEmailToken(user);

                    String fullUrl = userRepoImp.createUrl(user.getUserId(), user.getTokens().getResetPassToken().substring(0, 36));
                    Thread thread = new Thread(){
                        public void run(){
                            emailSend.sendMail(email, user.getUserId(), user.getDisplayName(), "\n" + fullUrl);
                        }
                    };

                    thread.start();
                    mongoTemplate.remove(query,collection);
                    return Integer.valueOf(EMAIL_VALID_TIME);


                }
            }

        } else
            return 0;

        return 0;
    }

    public HttpStatus sendEmail(String email, String uid) {

        if (checkMail(email) != null & userRepo.retrieveUser(uid).getUserId() != null) {
            List<JSONObject> ids = checkMail(email);
            List<User> people = new LinkedList<>();
            User user = userRepo.retrieveUser(uid);
            for (JSONObject id : ids) people.add(userRepo.retrieveUser(id.getAsString("userId")));

            for (User p : people) {

                if (user.equals(p)) {

                    tokenClass.insertEmailToken(user);

                    String fullUrl = userRepoImp.createUrl(user.getUserId(), user.getTokens().getResetPassToken().substring(0, 36));
                    Thread thread = new Thread(){
                        public void run(){
                            emailSend.sendMail(email, user.getUserId(), user.getDisplayName(), "\n" + fullUrl);
                        }
                    };

                    thread.start();
                    return HttpStatus.OK;


                }
            }

        } else
            return HttpStatus.FORBIDDEN;

        return HttpStatus.BAD_REQUEST;
    }


}
