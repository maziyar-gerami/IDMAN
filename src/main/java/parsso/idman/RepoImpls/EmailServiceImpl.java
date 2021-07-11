package parsso.idman.RepoImpls;


import lombok.SneakyThrows;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import parsso.idman.Helpers.Communicate.Token;
import parsso.idman.Helpers.User.UserAttributeMapper;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Users.User;
import parsso.idman.Repos.EmailService;
import parsso.idman.Repos.UserRepo;
import parsso.idman.Utils.Captcha.Models.CAPTCHA;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.naming.directory.SearchControls;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    String from;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    LdapTemplate ldapTemplate;

    @Autowired
    UserAttributeMapper userAttributeMapper;

    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;

    @Value("${token.valid.email}")
    private String EMAIL_VALID_TIME;


    private final String collection = Variables.col_captchas;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    Token tokenClass;

    @Autowired
    UserRepo userRepo;

    @Autowired
    private TemplateEngine templateEngine;


    @Autowired
    MailProperties mailProperties;


    public void sendSimpleMessage(User user, String subject, String url) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(user.getMail());
        message.setSubject(subject);
        Variables.template(user, url);
        message.setText(Variables.template(user, url));
        mailSender.send(message);
    }

    public void sendHtmlMessage(User user, String subject, String url) throws javax.mail.MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(from);
        helper.setTo(user.getMail());
        helper.setSubject(subject);
        String s = Variables.template(user,url);
        helper.setText(Variables.template(user, url),true);

        mailSender.send(message);
    }



    public HttpStatus sendMail(String email) {
        if (checkMail(email) != null) {
            User user = userRepo.retrieveUsers(checkMail(email).get(0).getAsString("userId"));

            tokenClass.insertEmailToken(user);

            String fullUrl = userRepo.createUrl(user.getUserId(), user.getUsersExtraInfo().getResetPassToken().substring(0, 36));


            Thread thread = new Thread() {
                @SneakyThrows
                public void run() {
                    sendHtmlMessage(user, Variables.email_recoverySubject, "\n" + fullUrl);

                }
            };

            thread.start();

            return HttpStatus.OK;
        } else
            return HttpStatus.FORBIDDEN;
    }

    @Override
    public HttpStatus sendMail(JSONObject jsonObject) {
        if (jsonObject.size() == 0) {
            List<User> users = userRepo.retrieveUsersFull();

            for (User user : users)
                if (!user.getUsersExtraInfo().getRole().equals("SUPERADMIN") && user.getMail() != null && user.getMail() != null && user.getMail() != "" && user.getMail() != " ")
                    sendMail(user.getMail());

        } else {
            ArrayList jsonArray = (ArrayList) jsonObject.get("names");
            for (Object temp : jsonArray) {

                User user = userRepo.retrieveUsers(temp.toString());
                {
                    Thread thread = new Thread() {
                        public void run() {
                            sendMail(user.getMail());

                        }
                    };
                    if (checkMail(user.getMail()) != null)
                        thread.start();
                }
            }
        }


        return HttpStatus.OK;
    }

    @Override
    public int sendMail(String email, String cid, String answer) {

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

            try {
                sendHtmlMessage(user, Variables.email_recoverySubject, fullUrl);

            }catch (Exception e){
                return 0;
            }

            mongoTemplate.remove(query, collection);
            return Integer.valueOf(EMAIL_VALID_TIME);
        } else
            return 0;
    }

    @Override
    public void sendMail(User user, String day) throws MessagingException {

        String subject = "اخطار انقضای رمز عبور پارسو";
        String start = " عزیز \n" +
                "رمز عبور شما کمتر از ";
        String middle = " روز دیگر منقضی می شود.\n";
        String end = "\n\nبرای جلوگیری از غیرفعال شدن حساب کاربری، هرچه زودتر به تغییر رمز عبور خود از طریق پارسو اقدام فرمایید.";

        String text = user.getDisplayName().substring(0, user.getDisplayName().indexOf(' ')) + start + day + middle + end;

        sendHtmlMessage(user,subject,text);

    }

    @Override
    public int sendMail(String email, String uid, String cid, String answer) {

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

                    String fullUrl = userRepo.createUrl(user.getUserId(), user.getUsersExtraInfo().getResetPassToken().substring(0, 36));
                    try {
                        sendHtmlMessage(user, Variables.email_recoverySubject, fullUrl);
                    } catch (Exception e){ return 0;}

                    mongoTemplate.remove(query, collection);
                    return Integer.valueOf(EMAIL_VALID_TIME);

                }
            }

        } else
            return 0;

        return 0;
    }

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

}