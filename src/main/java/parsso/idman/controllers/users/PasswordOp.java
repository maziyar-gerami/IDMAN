package parsso.idman.controllers.users;

import javax.servlet.http.HttpServletRequest;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.controllers.users.oprations.UsersOps;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.communicate.InstantMessage;
import parsso.idman.helpers.communicate.Token;
import parsso.idman.helpers.configs.PwdAttributeMapper;
import parsso.idman.helpers.user.Password;
import parsso.idman.models.other.Notification;
import parsso.idman.models.other.PWD;
import parsso.idman.models.response.Response;
import parsso.idman.repos.UserRepo;


@RestController
public class PasswordOp extends UsersOps {
  final UserRepo.PasswordOp passwordOp;
  final InstantMessage instantMessage;

  @Value("${base.url}")
  private String url;

  @Autowired
  public PasswordOp(Token tokenClass, LdapTemplate ldapTemplate, MongoTemplate mongoTemplate,
      UserRepo.UsersOp.Retrieve usersOpRetrieve,
      UserRepo.PasswordOp passwordOp, InstantMessage instantMessage) {
    super(tokenClass, ldapTemplate, mongoTemplate, usersOpRetrieve);
    this.passwordOp = passwordOp;
    this.instantMessage = instantMessage;
  }

  @PutMapping("/api/users/password/expire")
  public ResponseEntity<Response> expirePassword(HttpServletRequest request,
      @RequestBody JSONObject jsonObject,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    JSONObject preventedUsers = passwordOp.expire(request.getUserPrincipal().getName(), jsonObject);

    if (preventedUsers == null) {
      return new ResponseEntity<>(
          new Response(null, Variables.MODEL_USER, HttpStatus.BAD_REQUEST.value(), lang),
          HttpStatus.OK);
    } else if (preventedUsers.size() == 0) {
      return new ResponseEntity<>(
          new Response(null, Variables.MODEL_USER, HttpStatus.OK.value(), lang),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          new Response(preventedUsers, Variables.MODEL_USER, HttpStatus.MULTI_STATUS.value(), lang),
          HttpStatus.OK);
    }

  }

  @PutMapping("/api/public/resetPass")
  public ResponseEntity<Response> rebindLdapUser(@RequestBody JSONObject input,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    String uid = input.getAsString("userId");
    String newPassword = input.getAsString("newPassword");
    String token = input.getAsString("token");

    JSONObject objectResult = new JSONObject();
    String dn = "cn=DefaultPPolicy,ou=Policies," + basedn;
    PWD pwd = this.ldapTemplate.lookup(dn, new PwdAttributeMapper());
    int pwdin = Integer.parseInt(pwd.getPwdInHistory().replaceAll("[^0-9]", ""));
    objectResult.put("pwdInHistory", pwdin);

    if(!(new Password(mongoTemplate).check(newPassword))){
      return new ResponseEntity<>(new Response(
        null, Variables.MODEL_PASSWORD, HttpStatus.EXPECTATION_FAILED.value(), lang),
          HttpStatus.OK);
    }

    HttpStatus httpStatus = passwordOp.change(uid, newPassword, token);

    if (httpStatus == HttpStatus.OK) {
      return new ResponseEntity<>(new Response(
        null, Variables.MODEL_PASSWORD, HttpStatus.OK.value(), lang),
          HttpStatus.OK);
    }

    return new ResponseEntity<>(new Response(
      null, Variables.MODEL_PASSWORD, httpStatus.value(), lang),
        HttpStatus.OK);
  }

  @PutMapping("/api/user/password")
  public ResponseEntity<Response> changePassword(HttpServletRequest request,
      @RequestParam(value = "lang", defaultValue = "fa") String lang,
      @RequestBody JSONObject jsonObject) throws NoSuchFieldException, IllegalAccessException {
    JSONObject objectResult = new JSONObject();
    String token = jsonObject.getAsString("token");
    if (jsonObject.getAsString("token") != null) {
      token = jsonObject.getAsString("token");
    }
    

    String pwdInHistory = "";
    int pwdin = -1;

    try {
      String dn = "cn=DefaultPPolicy,ou=Policies," + basedn;
      PWD pwd = this.ldapTemplate.lookup(dn, new PwdAttributeMapper());
      pwdin = Integer.parseInt(pwd.getPwdInHistory().replaceAll("[^0-9]", ""));
      objectResult.put("pwdInHistory", pwdin);
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      pwdin = Integer.parseInt(pwdInHistory);
    } catch (Exception ignored) {
      ignored.printStackTrace();
    }
    objectResult.put("pwdInHistory", pwdin);
    String password = jsonObject.getAsString("newPassword");

    if(!(new Password(mongoTemplate).check(password))){
      return new ResponseEntity<>(new Response(
        null, Variables.MODEL_PASSWORD, HttpStatus.EXPECTATION_FAILED.value(), lang),
          HttpStatus.OK);
    }

    HttpStatus httpStatus = passwordOp.change(
        request.getUserPrincipal().getName(), password, token);
    return new ResponseEntity<>(new Response(
      objectResult, Variables.MODEL_PASSWORD, httpStatus.value(), lang),
        HttpStatus.OK);

  }

  @PutMapping("/api/public/changePassword")
  public ResponseEntity<Response> changePasswordWithoutToken(@RequestBody JSONObject jsonObject,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    String currentPassword = jsonObject.getAsString("currentPassword");
    String newPassword = jsonObject.getAsString("newPassword");
    String userId = jsonObject.getAsString("userId");
    HttpStatus httpStatus = passwordOp.changePublic(userId, currentPassword, newPassword);

    if(!(new Password(mongoTemplate).check(newPassword))){
      return new ResponseEntity<>(new Response(
        null, Variables.MODEL_PASSWORD, HttpStatus.EXPECTATION_FAILED.value(), lang),
          HttpStatus.OK);
    }

    if (Boolean
        .parseBoolean(new Settings(mongoTemplate)
          .retrieve(Variables.PASSWORD_CHANGE_NOTIFICATION).getValue())
        && httpStatus == HttpStatus.OK) {
      new Notification(mongoTemplate)
        .sendPasswordChangeNotify(usersOpRetrieve.retrieveUsers(userId), url);
    }

    if (httpStatus == HttpStatus.OK) {
      return new ResponseEntity<>(new Response(
        null, Variables.MODEL_PASSWORD, HttpStatus.OK.value(), lang),
          HttpStatus.OK);
    }

    return new ResponseEntity<>(new Response(null, 
      Variables.MODEL_PASSWORD, httpStatus.value(), lang),
        HttpStatus.OK);

  }

  @GetMapping("/api/user/password/request")
  public ResponseEntity<Response> requestMessage(HttpServletRequest request,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    parsso.idman.models.users.User user = 
        usersOpRetrieve.retrieveUsers(request.getUserPrincipal().getName());
    if (user == null) {
      return new ResponseEntity<>(
          new Response(null, Variables.MODEL_PASSWORD, HttpStatus.NOT_FOUND.value(), lang),
          HttpStatus.OK);
    }
    int status = instantMessage.sendMessage(user);

    if (status > 0 && user != null) {
      return new ResponseEntity<>(new Response(
        status, Variables.MODEL_PASSWORD, HttpStatus.OK.value(), lang),
          HttpStatus.OK);
    }

    return new ResponseEntity<>(new Response(
      null, Variables.MODEL_PASSWORD, HttpStatus.BAD_REQUEST.value(), lang),
        HttpStatus.OK);
  }

}
