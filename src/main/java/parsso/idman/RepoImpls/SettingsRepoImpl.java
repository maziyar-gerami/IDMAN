package parsso.idman.RepoImpls;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.Communicate.Email;
import parsso.idman.Helpers.Communicate.InstantMessage;
import parsso.idman.Helpers.ReloadConfigs.PasswordSettings;
import parsso.idman.Models.Logs.Setting;
import parsso.idman.Models.Users.User;
import parsso.idman.Repos.ConfigRepo;
import parsso.idman.Repos.SettingsRepo;
import parsso.idman.Repos.UserRepo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SettingsRepoImpl implements SettingsRepo {

    @Value("${interval.check.pass.hours}")
    private static long intervalCheckPassTime;
    int millis = 3600000;
    @Autowired
    PasswordSettings passwordSettings;
    @Autowired
    UserRepo userRepo;
    @Autowired
    InstantMessage instantMessage;
    @Autowired
    Email email;
    @Autowired
    ConfigRepo configRepo;
    @Autowired
    private SettingsRepo settingsRepo;
    @Value("${max.pwd.lifetime.hours}")
    private long maxPwdLifetime;
    @Value("${expire.pwd.message.hours}")
    private long expirePwdMessageTime;

    @Override
    public HttpStatus emailNotification() {
        try {
            startNotification("email");
            return HttpStatus.OK;
        } catch (Exception e) {
            return HttpStatus.FORBIDDEN;
        }
    }

    @Override
    public HttpStatus messageNotification() {
        try {
            startNotification("instantMessage");
            return HttpStatus.OK;
        } catch (Exception e) {
            return HttpStatus.FORBIDDEN;
        }
    }

    @Override
    public List<Setting> retrieveTFSetting() throws IOException {
        return configRepo.retrieveTFSetting();
    }

    private HttpStatus startNotification(String method) {
        long deadline = maxPwdLifetime * millis;
        long messageTime = expirePwdMessageTime * millis;

        List<User> users = userRepo.retrieveUsersFull();

        for (User user : users) {

            Date pwdChangedTime = null;
            try {
                pwdChangedTime = new SimpleDateFormat("yyyyMMddHHmmss").parse(String.valueOf(user.getPasswordChangedTime()));
                if ((deadline / millis - ((new Date().getTime() - pwdChangedTime.getTime()) / millis)) <= (messageTime / millis)) {
                    if (method.equalsIgnoreCase("instantMessage"))
                        instantMessage.sendWarnExpireMessage(user, String.valueOf(deadline / millis - ((new Date().getTime() - pwdChangedTime.getTime()) / millis)));
                    else if (method.equalsIgnoreCase("email"))
                        email.sendWarnExpireMessage(user, String.valueOf(deadline / millis - ((new Date().getTime() - pwdChangedTime.getTime()) / millis)));

                }
            } catch (java.text.ParseException e) {
                e.printStackTrace();
                return HttpStatus.BAD_REQUEST;
            }

        }
        return HttpStatus.OK;
    }
}
