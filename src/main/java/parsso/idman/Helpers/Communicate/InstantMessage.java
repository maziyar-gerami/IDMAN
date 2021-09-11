package parsso.idman.Helpers.Communicate;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Users.User;
import parsso.idman.Repos.MagfaSMSSendRepo;
import parsso.idman.Repos.UserRepo;
import parsso.idman.Utils.Captcha.Models.CAPTCHA;
import parsso.idman.Utils.SMS.KaveNegar.KavenegarApi;
import parsso.idman.Utils.SMS.KaveNegar.excepctions.ApiException;
import parsso.idman.Utils.SMS.KaveNegar.excepctions.HttpException;
import parsso.idman.Utils.SMS.Magfa.Texts;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;

@Configuration
public class InstantMessage {
	private final String collection = Variables.col_captchas;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	private UserRepo userRepo;
	@Value("${token.valid.SMS}")
	private String SMS_VALID_TIME;
	@Value("${sms.validation.digits}")
	private String SMS_VALIDATION_DIGITS;
	@Value("${sms.api.key}")
	private String SMS_API_KEY;
	@Value("${base.url}")
	private String baseurl;
	@Value("${spring.ldap.base.dn}")
	private String BASE_DN;
	@Value("${SMS.SDK}")
	private String SMS_sdk;
	@Autowired
	private LdapTemplate ldapTemplate;
	@Autowired
	private Token tokenClass;
	@Autowired
	private parsso.idman.Helpers.User.UserAttributeMapper userAttributeMapper;
	@Autowired
	private MagfaSMSSendRepo magfaSMSSendRepo;

	public int sendMessage(String mobile, String cid, String answer) throws IOException, ParseException {

		if (SMS_sdk.equalsIgnoreCase("KaveNegar"))
			return sendMessageKaveNegar(mobile, cid, answer);
		else if (SMS_sdk.equalsIgnoreCase("Magfa"))
			return sendMessageMagfa(mobile, cid, answer);
		return 0;
	}

	public int sendMessage(User user, String cid, String answer) {

		if (SMS_sdk.equalsIgnoreCase("KaveNegar"))
			return sendMessageKaveNegar(user, cid, answer);
		else if (SMS_sdk.equalsIgnoreCase("Magfa"))
			return sendMessageMagfa(user, cid, answer);
		return 0;
	}

	public int sendMessage(User user) {
		if (SMS_sdk.equalsIgnoreCase("KaveNegar"))
			return sendMessageKaveNegar(user);
		else if (SMS_sdk.equalsIgnoreCase("Magfa"))
			return sendMessageMagfa(user);
		return 0;
	}

	public int sendMessage(String mobile, String uid, String cid, String answer) throws IOException, ParseException {
		if (SMS_sdk.equalsIgnoreCase("KaveNegar"))
			return sendMessageKaveNegar(mobile, uid, cid, answer);
		else if (SMS_sdk.equalsIgnoreCase("Magfa"))
			return sendMessageMagfa(mobile, uid, cid, answer);
		return 0;
	}

	private int sendMessageKaveNegar(String mobile, String cid, String answer) throws IOException, ParseException {

		Query query = new Query(Criteria.where("_id").is(cid));
		CAPTCHA captcha = mongoTemplate.findOne(query, CAPTCHA.class, collection);
		if (captcha == null)
			return -1;

		if (!(answer.equalsIgnoreCase(captcha.getPhrase())))
			mongoTemplate.remove(query, collection);

		User user = new User();

		List<JSONObject> checked = checkMobile(mobile);

		if (checked.size() == 0)
			return -3;

		else if (checked.size() > 1)
			return -2;

		else if (checked.size() == 1)
			user = userRepo.retrieveUsers(checkMobile(mobile).get(0).getAsString("userId"));

		if (tokenClass.insertMobileToken(user)) {
			try {

				String receptor = mobile;
				String message = user.getUsersExtraInfo().getResetPassToken().substring(0, Integer.valueOf(SMS_VALIDATION_DIGITS));
				KavenegarApi api = new KavenegarApi(SMS_API_KEY);
				api.verifyLookup(receptor, message, "", "", "mfa");
				mongoTemplate.remove(query, collection);
				return Integer.valueOf(SMS_VALID_TIME);

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

	private int sendMessageKaveNegar(User user, String cid, String answer) {

		Query query = new Query(Criteria.where("_id").is(cid));
		CAPTCHA captcha = mongoTemplate.findOne(query, CAPTCHA.class, collection);
		if (captcha == null)
			return -1;

		if (!(answer.equalsIgnoreCase(captcha.getPhrase())))
			mongoTemplate.remove(query, collection);

		if (user == null || user.getUserId() == null)
			return -3;

		if (tokenClass.insertMobileToken(user)) {
			try {

				String receptor = user.getMobile();
				String message = user.getUsersExtraInfo().getResetPassToken().substring(0, Integer.valueOf(SMS_VALIDATION_DIGITS));
				KavenegarApi api = new KavenegarApi(SMS_API_KEY);
				api.verifyLookup(receptor, message, "", "", "mfa");
				mongoTemplate.remove(query, collection);
				return Integer.valueOf(SMS_VALID_TIME);

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
		CAPTCHA captcha = mongoTemplate.findOne(query, CAPTCHA.class, collection);
		if (captcha == null)
			return -1;

		if (!(answer.equalsIgnoreCase(captcha.getPhrase()))) {
			mongoTemplate.remove(query, collection);
			return -1;
		}

		if (user == null || user.getUserId() == null)
			return -3;

		if (tokenClass.insertMobileToken(user)) {

			try {

				Texts texts = new Texts();
				texts.setMainMessage(user.getUsersExtraInfo().getResetPassToken().substring(0, Integer.valueOf(SMS_VALIDATION_DIGITS)));
				magfaSMSSendRepo.SendMessage(texts.getMainMessage(), user.getMobile(), 1L);

				mongoTemplate.remove(query, collection);
				return Integer.valueOf(SMS_VALID_TIME);

			} catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
				System.out.print("HttpException  : " + ex.getMessage());
				return 0;
			} catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
				System.out.print("ApiException : " + ex.getMessage());
				return 0;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else
			return 0;

		return 0;
	}

	private int sendMessageMagfa(String mobile, String cid, String answer) throws IOException, ParseException {

		Query query = new Query(Criteria.where("_id").is(cid));
		CAPTCHA captcha = mongoTemplate.findOne(query, CAPTCHA.class, collection);
		if (captcha == null)
			return -1;

		if (!(answer.equalsIgnoreCase(captcha.getPhrase()))) {
			mongoTemplate.remove(query, collection);
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
				texts.setMainMessage(user.getUsersExtraInfo().getResetPassToken().substring(0, Integer.valueOf(SMS_VALIDATION_DIGITS)));
				magfaSMSSendRepo.SendMessage(texts.getMainMessage(), user.getMobile(), 1L);

				mongoTemplate.remove(query, collection);
				return Integer.valueOf(SMS_VALID_TIME);

			} catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
				System.out.print("HttpException  : " + ex.getMessage());
				return 0;
			} catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
				System.out.print("ApiException : " + ex.getMessage());
				return 0;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else
			return 0;

		return 0;
	}

	private int sendMessageKaveNegar(User user) {

		if (checkMobile(user.getMobile()).size() > 0) {
			if (tokenClass.insertMobileToken(user)) {
				try {
					String receptor = user.getMobile();
					String message = user.getUsersExtraInfo().getResetPassToken().substring(0, Integer.valueOf(SMS_VALIDATION_DIGITS));
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

	private int sendMessageMagfa(User user) {

		if (checkMobile(user.getMobile()).size() > 0) {
			if (tokenClass.insertMobileToken(user)) {
				try {
					Texts texts = new Texts();
					texts.setMainMessage(user.getUsersExtraInfo().getResetPassToken().substring(0, Integer.valueOf(SMS_VALIDATION_DIGITS)));
					magfaSMSSendRepo.SendMessage(texts.getMainMessage(), user.getMobile(), 1L);

					return Integer.valueOf(SMS_VALID_TIME);

				} catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
					System.out.print("HttpException  : " + ex.getMessage());
					return 0;
				} catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
					System.out.print("ApiException : " + ex.getMessage());
					return 0;
				} catch (MalformedURLException e) {
					e.printStackTrace();
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

	private int sendMessageKaveNegar(String mobile, String uId, String cid, String answer) throws IOException, ParseException {

		Query query = new Query(Criteria.where("_id").is(cid));
		CAPTCHA captcha = mongoTemplate.findOne(query, CAPTCHA.class, collection);
		if (captcha == null) {

			return -1;

		}
		if (!(answer.equalsIgnoreCase(captcha.getPhrase()))) {
			mongoTemplate.remove(query, collection);
			return -1;
		}

		if (checkMobile(mobile).size() == 0) {
			return -3;
		}

		User user = userRepo.retrieveUsers(uId);

		if (user != null && user.getUserId() != null && tokenClass.insertMobileToken(user)) {
			List<JSONObject> ids = checkMobile(mobile);
			List<User> people = new LinkedList<>();
			for (JSONObject id : ids) people.add(userRepo.retrieveUsers(id.getAsString("userId")));

			for (User p : people) {
				if (p.getUserId().equals(user.getUserId())) {

					if (tokenClass.insertMobileToken(user)) {

						try {
							String receptor = mobile;
							String message = user.getUsersExtraInfo().getResetPassToken().substring(0, Integer.valueOf(SMS_VALIDATION_DIGITS));
							KavenegarApi api = new KavenegarApi(SMS_API_KEY);
							api.verifyLookup(receptor, message, "", "", "mfa");
							mongoTemplate.remove(query, collection);
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


		} else {

			return 0;
		}

		return 0;
	}

	private int sendMessageMagfa(String mobile, String uId, String cid, String answer) throws IOException, ParseException {
		Query query = new Query(Criteria.where("_id").is(cid));
		CAPTCHA captcha = mongoTemplate.findOne(query, CAPTCHA.class, collection);
		if (captcha == null)
			return -1;
		if (!(answer.equalsIgnoreCase(captcha.getPhrase()))) {
			mongoTemplate.remove(query, collection);
			return -1;
		}

		User user = new User();

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
							texts.setMainMessage(user.getUsersExtraInfo().getResetPassToken().substring(0, Integer.valueOf(SMS_VALIDATION_DIGITS)));
							magfaSMSSendRepo.SendMessage(texts.getMainMessage(), user.getMobile(), 1L);
							mongoTemplate.remove(query, collection);
							return Integer.valueOf(SMS_VALID_TIME);
						} catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.

							System.out.print("HttpException  : " + ex.getMessage());
							return 0;

						} catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.

							System.out.print("ApiException : " + ex.getMessage());
							return 0;

						} catch (MalformedURLException e) {
							e.printStackTrace();
						}

					}


				}
			}

		} else {
			return 0;

		}

		return 0;
	}

	public void sendWarnExpireMessage(User user, String day) {

		try {
			String receptor = user.getMobile();
			KavenegarApi api = new KavenegarApi(SMS_API_KEY);
			if (Integer.valueOf(day) >= 0)
				api.verifyLookup(receptor, user.getDisplayName().substring(0, user.getDisplayName().indexOf(' ')), day, baseurl + "/resetPass", "expirePassReminder");
			else
				api.verifyLookup(receptor, user.getDisplayName().substring(0, user.getDisplayName().indexOf(' ')), baseurl + "/resetPass", "", "expirePassNotify");

		} catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
			System.out.print("HttpException  : " + ex.getMessage());

		} catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
			System.out.print("ApiException : " + ex.getMessage());

		}

	}
}