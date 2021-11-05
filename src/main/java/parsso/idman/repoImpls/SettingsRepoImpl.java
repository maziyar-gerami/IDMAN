package parsso.idman.repoImpls;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.communicate.InstantMessage;
import parsso.idman.helpers.reloadConfigs.PasswordSettings;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.logs.Setting;
import parsso.idman.models.users.User;
import parsso.idman.repos.ConfigRepo;
import parsso.idman.repos.EmailService;
import parsso.idman.repos.SettingsRepo;
import parsso.idman.repos.UserRepo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SettingsRepoImpl implements SettingsRepo {
    final int millis = 3600000;
    PasswordSettings passwordSettings;
    UserRepo userRepo;
    InstantMessage instantMessage;
    ConfigRepo configRepo;
    EmailService emailService;
    MongoTemplate mongoTemplate;
    final
    UniformLogger uniformLogger;

    @Value("${max.pwd.lifetime.hours}")
    private long maxPwdLifetime;
    @Value("${expire.pwd.message.hours}")
    private long expirePwdMessageTime;



    @Autowired
    public SettingsRepoImpl(MongoTemplate mongoTemplate, UniformLogger uniformLogger,EmailService emailService,
                            ConfigRepo configRepo , InstantMessage instantMessage, UserRepo userRepo, PasswordSettings passwordSettings) {
        this.mongoTemplate = mongoTemplate;
        this.uniformLogger = uniformLogger;
        this.emailService = emailService;
        this.configRepo = configRepo;
        this.instantMessage = instantMessage;
        this.userRepo = userRepo;
        this.passwordSettings = passwordSettings;
    }


    @Override
    public Setting retrieve(String settingName) {
        Setting s= null;
        try {
            s = mongoTemplate.findOne(new Query(Criteria.where("_id").is(settingName)), Setting.class, Variables.col_properties);
        }catch (Exception e){
            e.printStackTrace();
            uniformLogger.error("System",new ReportMessage(Variables.MODEL_SETTINGS, settingName, Variables.ACTION_GET,Variables.RESULT_FAILED ));
        }
        return s;
    }

    @Override
    public void emailNotification() {
        try {
            startNotification("email");
        } catch (Exception ignored) {
        }
    }

    @Override
    public void instantMessageNotification() {
        try {
            startNotification("instantMessage");
        } catch (Exception ignored) {
        }
    }


    private void startNotification(String method) {
        long deadline = maxPwdLifetime * millis;
        long messageTime = expirePwdMessageTime * millis;

        List<User> users = userRepo.retrieveUsersFull();

        for (User user : users) {

            Date pwdChangedTime;
            try {
                pwdChangedTime = new SimpleDateFormat("yyyyMMddHHmmss").parse(String.valueOf(user.getPasswordChangedTime()));
                if ((deadline / millis - ((new Date().getTime() - pwdChangedTime.getTime()) / millis)) <= (messageTime / millis)) {
                    if (method.equalsIgnoreCase("instantMessage"))
                        instantMessage.sendWarnExpireMessage(user, String.valueOf(deadline / millis - ((new Date().getTime() - pwdChangedTime.getTime()) / millis)));
                    else if (method.equalsIgnoreCase("email"))
                        sendWarnExpireMessage(user, String.valueOf(deadline / millis - ((new Date().getTime() - pwdChangedTime.getTime()) / millis)));

                }
            } catch (java.text.ParseException e) {
                e.printStackTrace();
                return;
            }

        }
    }

    public void sendWarnExpireMessage(User user, String day) {

        try {
            emailService.sendMail(user, day);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
