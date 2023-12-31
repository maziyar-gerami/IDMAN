package parsso.idman.helpers.communicate;

import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.UserAttributeMapper;
import parsso.idman.models.users.User;
import parsso.idman.repos.UserRepo;
import parsso.idman.utils.captcha.repo.CAPTCHARepo;
import parsso.idman.utils.sms.kaveNegar.KavenegarApi;
import parsso.idman.utils.sms.kaveNegar.excepctions.ApiException;
import parsso.idman.utils.sms.kaveNegar.excepctions.HttpException;
import parsso.idman.utils.sms.magfa.Texts;

@Service
public class InstantMessage {

  @Autowired
  public InstantMessage(MongoTemplate mongoTemplate, LdapTemplate ldapTemplate,
      UserRepo.UsersOp.Retrieve usersOpRetrieve, Token tokenClass, CAPTCHARepo captchaRepo) {
    this.mongoTemplate = mongoTemplate;
    this.ldapTemplate = ldapTemplate;
    this.tokenClass = tokenClass;
    this.usersOpRetrieve = usersOpRetrieve;
    this.captchaRepo = captchaRepo;
  }

  private final MongoTemplate mongoTemplate;
  private final UserRepo.UsersOp.Retrieve usersOpRetrieve;
  private final LdapTemplate ldapTemplate;
  private final Token tokenClass;
  private final CAPTCHARepo captchaRepo;

  @Value("${spring.ldap.base.dn}")
  private String basedn;

  public int sendMessage(String mobile, String cid, String answer) {

    if (getSdk().equalsIgnoreCase("KaveNegar")) {
      return sendMessageKaveNegar(mobile, cid, answer);
    } else if (getSdk().equalsIgnoreCase("Magfa")) {
      return sendMessageMagfa(mobile, cid, answer);
    }
    return 0;
  }

  public int sendMessage(User user, String cid, String answer) {

    if (getSdk().equalsIgnoreCase("KaveNegar")) {
      return sendMessageKaveNegar(user, cid, answer);
    } else if (getSdk().equalsIgnoreCase("Magfa")) {
      return sendMessageMagfa(user, cid, answer);
    }
    return 0;
  }

  public int sendMessage(User user) {
    if (getSdk().equalsIgnoreCase("KaveNegar")) {
      return sendMessageKaveNegar(user);
    } else if (getSdk().equalsIgnoreCase("Magfa")) {
      return sendMessageMagfa(user);
    }
    return 0;
  }

  public int sendMessage(String mobile, String uid, String cid, String answer) {
    if (getSdk().equalsIgnoreCase("KaveNegar")) {
      return sendMessageKaveNegar(mobile, uid, cid, answer);
    } else if (getSdk().equalsIgnoreCase("Magfa")) {
      return sendMessageMagfa(mobile, uid, cid, answer);
    }
    return 0;
  }

  private int sendMessageKaveNegar(String mobile, String cid, String answer) {

    Query query = new Query(Criteria.where("_id").is(cid));

    if (!captchaRepo.check(cid, answer)) {
      return -1;
    }

    User user;

    List<String> checked = checkMobile(mobile);

    if (checked.size() == 0) {
      return -3;
    } else if (checked.size() > 1) {
      return -2;
    } else {
      user = usersOpRetrieve.retrieveUsers(checkMobile(mobile).get(0));
    }

    if (tokenClass.insertMobileToken(user)) {
      return keveNegar_insertTokenAndSend(mobile, query, user);
    }

    return 0;
  }

  private int keveNegar_insertTokenAndSend(String mobile, Query query, User user) {
    try {

      String message = user.getUsersExtraInfo().getResetPassToken().substring(0, Integer.parseInt(
          new Settings(mongoTemplate).retrieve(Variables.SMS_VALIDATION_DIGITS)
              .getValue().toString()));
      KavenegarApi api = new KavenegarApi(
          new Settings(mongoTemplate).retrieve(Variables.KAVENEGAR_API_KEY).getValue().toString());
      api.verifyLookup(mobile, message, "", "", "mfa");
      mongoTemplate.remove(query, Variables.col_captchas);
      return Integer
          .parseInt(new Settings(mongoTemplate).retrieve(Variables.TOKEN_VALID_SMS)
              .getValue().toString());

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

    if (!captchaRepo.check(cid, answer)) {
      return -1;
    }

    if (user == null || user.get_id() == null) {
      return -3;
    }

    if (tokenClass.insertMobileToken(user)) {
      try {

        String receptor = user.getMobile();
        String message = user.getUsersExtraInfo().getResetPassToken().substring(0, Integer.parseInt(
            new Settings(mongoTemplate).retrieve(Variables.SMS_VALIDATION_DIGITS)
                .getValue().toString()));
        KavenegarApi api = new KavenegarApi(
            new Settings(mongoTemplate).retrieve(Variables.KAVENEGAR_API_KEY)
                .getValue().toString());
        api.verifyLookup(receptor, message, "", "", "mfa");
        mongoTemplate.remove(query, Variables.col_captchas);
        return Integer.parseInt(
            new Settings(mongoTemplate).retrieve(Variables.TOKEN_VALID_SMS).getValue().toString());

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

    if (!captchaRepo.check(cid, answer)) {
      return -1;
    }

    if (user == null || user.get_id() == null) {
      return -3;
    }

    if (tokenClass.insertMobileToken(user)) {

      try {

        Texts texts = new Texts();
        texts.authorizeMessage(user.getUsersExtraInfo().getResetPassToken().substring(
            0, Integer.parseInt(new Settings(mongoTemplate).retrieve(
                Variables.SMS_VALIDATION_DIGITS).getValue().toString())));
        new MagfaInstantMessage(mongoTemplate, texts.getMainMessage()).SendMessage(user.getMobile(), 1L);

        mongoTemplate.remove(query, Variables.col_captchas);
        return Integer.parseInt(
            new Settings(mongoTemplate).retrieve(Variables.TOKEN_VALID_SMS).getValue().toString());

      } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
        System.out.print("HttpException  : " + ex.getMessage());
        return 0;
      } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
        System.out.print("ApiException : " + ex.getMessage());
        return 0;
      }
    } else {
      return 0;
    }

  }

  private int sendMessageMagfa(String mobile, String cid, String answer) {

    Query query = new Query(Criteria.where("_id").is(cid));

    if (!captchaRepo.check(cid, answer)) {
      return -1;
    }

    User user = new User();
    int check = checkMobile(mobile).size();

    if (check == 0) {
      return -3;
    } else if (check > 1) {
      return -2;
    } else if (check == 1) {
      user = usersOpRetrieve.retrieveUsers(checkMobile(mobile).get(0));
    }

    if (tokenClass.insertMobileToken(user)) {

      try {

        Texts texts = new Texts();
        texts.authorizeMessage(user.getUsersExtraInfo().getResetPassToken().substring(0, Integer
            .parseInt(new Settings(mongoTemplate).retrieve(
                Variables.SMS_VALIDATION_DIGITS).getValue())));
        new MagfaInstantMessage(mongoTemplate, texts.getMainMessage()).SendMessage(user.getMobile(), 1L);

        mongoTemplate.remove(query, Variables.col_captchas);
        return Integer.parseInt(
            new Settings(mongoTemplate).retrieve(Variables.TOKEN_VALID_SMS).getValue().toString());

      } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
        System.out.print("HttpException  : " + ex.getMessage());
        return 0;
      } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
        System.out.print("ApiException : " + ex.getMessage());
        return 0;
      }
    } else {
      return 0;
    }

  }

  private int sendMessageKaveNegar(User user) {

    if (checkMobile(user.getMobile()).size() > 0) {
      if (tokenClass.insertMobileToken(user)) {
        try {
          String receptor = user.getMobile();
          String message = user.getUsersExtraInfo().getResetPassToken().substring(0,
              Integer.parseInt(new Settings(mongoTemplate).retrieve(Variables.SMS_VALIDATION_DIGITS)
                  .getValue().toString()));
          KavenegarApi api = new KavenegarApi(
              new Settings(mongoTemplate).retrieve(
                  Variables.KAVENEGAR_API_KEY).getValue().toString());
          api.verifyLookup(receptor, message, "", "", "mfa");
          return Integer.parseInt(
              new Settings(mongoTemplate).retrieve(
                  Variables.TOKEN_VALID_SMS).getValue().toString());

        } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
          System.out.print("HttpException  : " + ex.getMessage());
          return 0;
        } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
          System.out.print("ApiException : " + ex.getMessage());
          return 0;
        }
      } else {
        return 0;
      }

    } else {
      return 0;
    }
  }

  private int sendMessageMagfa(User user) {

    if (checkMobile(user.getMobile()).size() > 0) {
      if (tokenClass.insertMobileToken(user)) {
        try {
          Texts texts = new Texts();
          texts.authorizeMessage(user.getUsersExtraInfo().getResetPassToken().substring(
              0, Integer.parseInt(new Settings(mongoTemplate).retrieve(
                  Variables.TOKEN_VALID_SMS).getValue().toString())));
          new MagfaInstantMessage(mongoTemplate, texts.getMainMessage()).SendMessage(user.getMobile(), 1L);

          return Integer.parseInt(
              new Settings(mongoTemplate).retrieve(
                  Variables.TOKEN_VALID_SMS).getValue().toString());

        } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
          System.out.print("HttpException  : " + ex.getMessage());
          return 0;
        } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
          System.out.print("ApiException : " + ex.getMessage());
          return 0;
        }
      } else {
        return 0;
      }

    }
    return 0;
  }

  public LinkedList<String> checkMobile(String mobile) {

    List<User> people = ldapTemplate.search(basedn, new EqualsFilter("mobile", mobile).encode(),
        new UserAttributeMapper(mongoTemplate));
    LinkedList<String> names = new LinkedList<String>();
    for (User user : people) {
      names.add(user.get_id().toString());
    }
    return names;
  }

  private int sendMessageKaveNegar(String mobile, String uid, String cid, String answer) {

    Query query = new Query(Criteria.where("_id").is(cid));
    if (!captchaRepo.check(cid, answer)) {
      return -1;
    }

    if (checkMobile(mobile).size() == 0) {
      return -3;
    }

    User user = usersOpRetrieve.retrieveUsers(uid);

    if (user != null && user.get_id() != null && tokenClass.insertMobileToken(user)) {
      LinkedList<String> ids = checkMobile(mobile);
      List<User> people = new LinkedList<>();
      for (String id : ids) {
        people.add(usersOpRetrieve.retrieveUsers(id));
      }
      for (User p : people) {
        if (p.get_id().equals(user.get_id())) {
          if (tokenClass.insertMobileToken(user)) {
            return keveNegar_insertTokenAndSend(mobile, query, user);
          }
        }
      }
    }
    return 0;
  }

  private int sendMessageMagfa(String mobile, String uid, String cid, String answer) {

    Query query = new Query(Criteria.where("_id").is(cid));

    if (!captchaRepo.check(cid, answer)) {
      return -1;
    }

    User user;
    LinkedList<String> check = checkMobile(mobile);

    if (check.size() == 0) {

      return -3;
    }
    user = usersOpRetrieve.retrieveUsers(uid);

    if (user == null) {
      return 0;
    }

    if (check != null && usersOpRetrieve.retrieveUsers(uid).get_id() != null
        && tokenClass.insertMobileToken(user)) {

      List<String> ids = checkMobile(mobile);
      List<User> people = new LinkedList<>();
      user = usersOpRetrieve.retrieveUsers(uid);
      for (String id : ids) {
        people.add(usersOpRetrieve.retrieveUsers(id));
      }

      for (User p : people) {
        if (p.get_id().equals(user.get_id())) {

          if (tokenClass.insertMobileToken(user)) {

            try {
              Texts texts = new Texts();
              texts.authorizeMessage(user.getUsersExtraInfo().getResetPassToken().substring(0,
                  Integer.parseInt(new Settings(mongoTemplate)
                      .retrieve(Variables.SMS_VALIDATION_DIGITS).getValue().toString())));
              new MagfaInstantMessage(mongoTemplate, texts.getMainMessage()).SendMessage(user.getMobile(), 1L);
              mongoTemplate.remove(query, Variables.col_captchas);
              return Integer.parseInt(
                new Settings(mongoTemplate).retrieve(Variables.TOKEN_VALID_SMS)
                .getValue().toString());
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

  private String getSdk() {
    return new Settings(mongoTemplate).retrieve(Variables.SMS_SDK).getValue().toString();
  }
}