package parsso.idman.RepoImpls;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.Communicate.InstantMessage;
import parsso.idman.Helpers.ReloadConfigs.PasswordSettings;
import parsso.idman.Models.Users.User;
import parsso.idman.repos.ConfigRepo;
import parsso.idman.repos.EmailService;
import parsso.idman.repos.SettingsRepo;
import parsso.idman.repos.UserRepo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SettingsRepoImpl implements SettingsRepo {
	@Value("${interval.check.pass.hours}")
	private static long intervalCheckPassTime;
	final int millis = 3600000;
	@Autowired
	PasswordSettings passwordSettings;
	@Autowired
	UserRepo userRepo;
	@Autowired
	InstantMessage instantMessage;
	@Autowired
	ConfigRepo configRepo;
	@Autowired
	EmailService emailService;
	private SettingsRepo settingsRepo;
	@Value("${max.pwd.lifetime.hours}")
	private long maxPwdLifetime;
	@Value("${expire.pwd.message.hours}")
	private long expirePwdMessageTime;

	@Override
	public void emailNotification() {
		try {
			startNotification("email");
        } catch (Exception e) {
        }
	}

	@Override
	public void instantMessageNotification() {
		try {
			startNotification("instantMessage");
        } catch (Exception e) {
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
