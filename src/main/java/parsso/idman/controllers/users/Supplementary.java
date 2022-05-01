package parsso.idman.controllers.users;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import parsso.idman.controllers.users.oprations.UsersOps;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.communicate.InstantMessage;
import parsso.idman.helpers.communicate.Token;
import parsso.idman.impls.users.supplementary.Authenticate;
import parsso.idman.models.response.Response;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.EmailService;
import parsso.idman.repos.SkyroomRepo;
import parsso.idman.repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class Supplementary extends UsersOps {
  final SkyroomRepo skyroomRepo;
  EmailService emailService;
  InstantMessage instantMessage;
  UserRepo.Supplementary supplementary;
  Authenticate authenticate;

  @Autowired
  public Supplementary(Token tokenClass, LdapTemplate ldapTemplate, MongoTemplate mongoTemplate,
      UserRepo.UsersOp.Retrieve usersOpRetrieve,
      SkyroomRepo skyroomRepo, EmailService emailService, Authenticate authenticate,
      InstantMessage instantMessage, UserRepo.Supplementary supplementary) {
    super(tokenClass, ldapTemplate, mongoTemplate, usersOpRetrieve);
    this.skyroomRepo = skyroomRepo;
    this.emailService = emailService;
    this.instantMessage = instantMessage;
    this.authenticate = authenticate;
    this.supplementary = supplementary;
    this.counter = "3";
  }

  //@Value("${cas.authn.passwordless.tokens.expireInSeconds}")
  private String counter;

  @GetMapping("/api/skyroom")
  public ResponseEntity<Response> skyroom(HttpServletRequest request,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws IOException, NoSuchFieldException, IllegalAccessException {

    return new ResponseEntity<>(
        new Response(skyroomRepo.run(usersOpRetrieve.retrieveUsers(request.getUserPrincipal().getName())),
            Variables.MODEL_USER, HttpStatus.OK.value(), lang),
        HttpStatus.OK);
  }

  @PostMapping("/api/public/getName")
  public ResponseEntity<Response> getName(@RequestBody JSONObject jsonObject,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    UsersExtraInfo user = supplementary.getName(jsonObject.getAsString("userId"), jsonObject.getAsString("token"));
    HttpStatus httpStatus = (user != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
    return new ResponseEntity<>(new Response((null != user) ? user.getDisplayName() : null, Variables.MODEL_USER,
        httpStatus.value(), lang), HttpStatus.OK);
  }

  @GetMapping("/api/public/sendMail")
  public ResponseEntity<Response> sendMail(@RequestParam("email") String email,
      @RequestParam(value = "uid", defaultValue = "") String uid,
      @RequestParam("cid") String cid,
      @RequestParam("answer") String answer,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    int time = uid.equals("") ? emailService.sendMail(email, null, cid, answer)
        : emailService.sendMail(email, uid, cid, answer);
    ResponseEntity<Integer> response = getIntegerResponseEntity(time);
    return new ResponseEntity<>(
        new Response(response.getBody(), Variables.MODEL_USER, response.getStatusCode().value(), lang),
        HttpStatus.OK);
  }

  private ResponseEntity<Integer> getIntegerResponseEntity(int time) {
    if (time > 0) {
      return new ResponseEntity<>(time, HttpStatus.OK);
    } else if (time == -1)
      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    else if (time == -2)
      return new ResponseEntity<>(HttpStatus.MULTIPLE_CHOICES);
    else
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @GetMapping("/api/public/sendSMS")
  public ResponseEntity<Response> sendMessage(@RequestParam("mobile") String mobile,
      @RequestParam("cid") String cid,
      @RequestParam(value = "uid", defaultValue = "") String uid,
      @RequestParam("answer") String answer,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {

    int time = uid.equals("") ? instantMessage.sendMessage(mobile, cid, answer)
        : instantMessage.sendMessage(mobile, uid, cid, answer);

    if (time > 0) {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("time", time);
      if (uid.equals(""))
        jsonObject.put("userId", supplementary.getByMobile(mobile));
      else
        jsonObject.put("userId", uid);
    }

    ResponseEntity<Integer> response = getIntegerResponseEntity(time);
    return new ResponseEntity<>(
        new Response(response.getBody(), Variables.MODEL_USER, response.getStatusCode().value(), lang),
        HttpStatus.OK);
  }

  @GetMapping("/api/public/sendTokenUser/{uid}/{cid}/{answer}")
  public ResponseEntity<Response> sendMessageUser(
      @PathVariable("uid") String uid,
      @PathVariable("cid") String cid,
      @PathVariable("answer") String answer,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    User user = usersOpRetrieve.retrieveUsers(uid);
    if (user == null)
      return new ResponseEntity<>(new Response(null, Variables.MODEL_USER, HttpStatus.NOT_FOUND.value(), lang),
          HttpStatus.OK);

    int time = instantMessage.sendMessage(user, cid, answer);

    if (time > 0) {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("time", time);
      jsonObject.put("userId", uid);
      return new ResponseEntity<>(new Response(jsonObject, Variables.MODEL_USER, HttpStatus.OK.value(), lang),
          HttpStatus.OK);
    } else
      return new ResponseEntity<>(
          new Response(null, Variables.MODEL_USER, HttpStatus.EXPECTATION_FAILED.value(), lang),
          HttpStatus.OK);

  }

  @PostMapping("/api/users/sendMail")
  public ResponseEntity<HttpStatus> sendMultipleMailByAdmin(@RequestBody JSONObject jsonObject) {
    return new ResponseEntity<>(emailService.sendMail(jsonObject), HttpStatus.OK);
  }

  @GetMapping("/api/public/validateMessageToken/{uId}/{token}")
  public ResponseEntity<Response> resetPassMessage(@PathVariable("uId") String uId,
      @PathVariable("token") String token,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    HttpStatus result = tokenClass.checkToken(uId, token);

    return new ResponseEntity<>(
        new Response(null, Variables.MODEL_CAPTCHA,
            (result == HttpStatus.OK) ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value(), lang),
        HttpStatus.OK);
  }

  @GetMapping("/api/public/validateEmailToken/{uId}/{token}")
  public Object resetPass(@PathVariable("uId") String uId, @PathVariable("token") String token,
      RedirectAttributes attributes, @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    HttpStatus httpStatus = tokenClass.checkToken(uId, token);

    if (httpStatus == HttpStatus.OK) {
      attributes.addAttribute("uid", uId);
      attributes.addAttribute("token", token);
      return new RedirectView("/newpassword");
    }
    return new ResponseEntity<>(new Response(null, Variables.MODEL_USER, HttpStatus.BAD_REQUEST.value(), lang),
        HttpStatus.OK);
  }

  @PostMapping("/api/public/authenticate")
  public ResponseEntity<Response> logIn(@RequestBody JSONObject jsonObject,
      @RequestParam(name = "lang", defaultValue = "fa") String lang) throws Exception {
    String password;
    String userId;
    try {
      userId = jsonObject.getAsString("userId");
      password = jsonObject.getAsString("password");
    } catch (Exception e) {
      return new ResponseEntity<>(
          new Response(Variables.MODEL_USER, Variables.MODEL_USER, HttpStatus.BAD_REQUEST.value(), lang),
          HttpStatus.OK);
    }

    int result = authenticate.authenticate(userId, password);
    return new ResponseEntity<>(new Response(result, Variables.MODEL_USER, HttpStatus.OK.value(), lang),
        HttpStatus.OK);
  }

  @GetMapping("/api/public/counter")
  public ResponseEntity<Response> getCounter(@RequestParam(name = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("counter", counter);
    return new ResponseEntity<>(new Response(jsonObject, Variables.MODEL_USER, HttpStatus.OK.value(), lang),
        HttpStatus.OK);
  }

}
