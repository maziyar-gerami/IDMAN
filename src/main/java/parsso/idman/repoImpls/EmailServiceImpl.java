package parsso.idman.repoImpls;


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
import parsso.idman.helpers.communicate.Token;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.user.UserAttributeMapper;
import parsso.idman.helpers.Variables;
import parsso.idman.models.users.User;
import parsso.idman.utils.captcha.Models.CAPTCHA;
import parsso.idman.repos.EmailService;
import parsso.idman.repos.UserRepo;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.naming.directory.SearchControls;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("rawtypes")
@Service
public class EmailServiceImpl implements EmailService {
    private final String collection = Variables.col_captchas;
    @Value("${spring.mail.username}")
    String from;
    @Autowired
    LdapTemplate ldapTemplate;
    @Autowired
    UniformLogger uniformLogger;
    @Autowired
    UserAttributeMapper userAttributeMapper;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    Token tokenClass;
    @Autowired
    UserRepo userRepo;
    @Autowired
    MailProperties mailProperties;
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;
    @Value("${token.valid.email}")
    private String EMAIL_VALID_TIME;
    @Autowired
    private TemplateEngine templateEngine;

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
        String s = Variables.template(user, url);
        helper.setText(Variables.template(user, url), true);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendMail(String email) {
        if (checkMail(email) != null) {
            User user = userRepo.retrieveUsers(checkMail(email).get(0).getAsString("userId"));

            tokenClass.insertEmailToken(user);

            String fullUrl = userRepo.createUrl(user.getUserId(), user.getUsersExtraInfo().getResetPassToken().substring(0, 36));

            Thread thread = new Thread(() -> {
                try {
                    sendHtmlMessage(user, Variables.email_recoverySubject, "\n" + fullUrl);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });

            thread.start();

        }
    }

    @Override
    public HttpStatus sendMail(JSONObject jsonObject) {
        if (jsonObject.size() == 0) {
            List<User> users = userRepo.retrieveUsersFull();

            for (User user : users)
                if (!user.getUsersExtraInfo().getRole().equals("SUPERUSER") && user.getMail() != null && user.getMail() != null && !user.getMail().equals("") && !user.getMail().equals(" "))
                    sendMail(user.getMail());

        } else {
            ArrayList jsonArray = (ArrayList) jsonObject.get("names");
            for (Object temp : jsonArray) {

                User user = userRepo.retrieveUsers(temp.toString());
                {
                    Thread thread = new Thread(() -> sendMail(user.getMail()));
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

        User user;

        if (checkMail(email).size() == 0)
            return -3;

        else if (checkMail(email).size() > 1)
            return -2;

        if (checkMail(email).size() == 1) {
            user = userRepo.retrieveUsers(checkMail(email).get(0).getAsString("userId"));

            tokenClass.insertEmailToken(user);

            String fullUrl = userRepo.createUrl(user.getUserId(), user.getUsersExtraInfo().getResetPassToken().substring(0, 36));

            try {
                sendHtmlMessage(user, Variables.email_recoverySubject, fullUrl);


            } catch (Exception e) {
                return 0;
            }

            mongoTemplate.remove(query, collection);
            return Integer.parseInt(EMAIL_VALID_TIME);
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

        String text;
        try {
            text = user.getDisplayName().substring(0, user.getDisplayName().indexOf(' ')) + start + day + middle + end;
            sendHtmlMessage(user, subject, text);
        }catch (StringIndexOutOfBoundsException ignored){}
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

        if (checkMail(email) != null && userRepo.retrieveUsers(uid) != null && userRepo.retrieveUsers(uid).getUserId() != null) {
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

                    } catch (Exception e) {
                        return 0;
                    }

                    mongoTemplate.remove(query, collection);
                    return Integer.parseInt(EMAIL_VALID_TIME);

                }
            }

            return -2;

        } else
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
