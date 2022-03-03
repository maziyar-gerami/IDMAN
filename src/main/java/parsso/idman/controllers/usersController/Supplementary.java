package parsso.idman.controllers.usersController;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.communicate.InstantMessage;
import parsso.idman.helpers.communicate.Token;
import parsso.idman.models.other.SkyRoom;
import parsso.idman.models.response.Response;
import parsso.idman.models.users.User;
import parsso.idman.repoImpls.users.supplementary.Authenticate;
import parsso.idman.repos.EmailService;
import parsso.idman.repos.SkyroomRepo;
import parsso.idman.repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
public class Supplementary extends UsersOps{
    final SkyroomRepo skyroomRepo;
    EmailService emailService;
    InstantMessage instantMessage;
    UserRepo.Supplementary supplementary;
    Authenticate authenticate;

    @Autowired
    public Supplementary(Token tokenClass, LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, UserRepo.UsersOp.Retrieve usersOpRetrieve,
                         SkyroomRepo skyroomRepo, EmailService emailService,Authenticate authenticate, InstantMessage instantMessage, UserRepo.Supplementary supplementary) {
        super(tokenClass, ldapTemplate, mongoTemplate, usersOpRetrieve);
        this.skyroomRepo = skyroomRepo;
        this.emailService = emailService;
        this.instantMessage = instantMessage;
        this.authenticate = authenticate;
        this.supplementary = supplementary;
    }

    @Value("${cas.authn.passwordless.tokens.expireInSeconds}")
    private String counter;


    @GetMapping("/api/skyroom")
    public ResponseEntity<SkyRoom> skyroom(HttpServletRequest request) throws IOException {

        return new ResponseEntity<>(skyroomRepo.Run(usersOpRetrieve.retrieveUsers(request.getUserPrincipal().getName())), HttpStatus.OK);
    }
    @GetMapping("/api/public/getName/{uid}/{token}")
    public ResponseEntity<User> getName(@PathVariable("uid") String uid, @PathVariable("token") String token) {
        User user = supplementary.getName(uid, token);
        HttpStatus httpStatus = (user != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(user, httpStatus);
    }

    @GetMapping("/api/public/checkMail/{email}")
    public HttpEntity<List<JSONObject>> checkMail(@PathVariable("email") String email) {
        return new ResponseEntity<>(emailService.checkMail(email), HttpStatus.OK);
    }

    @GetMapping("/api/public/checkMobile/{mobile}")
    public HttpEntity<List<JSONObject>> checkMobile(@PathVariable("mobile") String mobile) {
        return new ResponseEntity<>(instantMessage.checkMobile(mobile), HttpStatus.OK);
    }


    @GetMapping("/api/public/sendMail")
    public ResponseEntity<Integer> sendMail(@RequestParam("email") String email,
                                            @RequestParam(value = "uid", defaultValue = "") String uid,
                                            @RequestParam("cid") String cid,
                                            @RequestParam("answer") String answer) {
        int time = uid.equals("") ? emailService.sendMail(email, null, cid, answer) : emailService.sendMail(email, uid, cid, answer);
        return getIntegerResponseEntity(time);
    }

    private ResponseEntity<Integer> getIntegerResponseEntity(int time) {
        if (time > 0) {
            return new ResponseEntity<>(time, HttpStatus.OK);
        } else if (time == -1)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        else if (time == -2)
            return new ResponseEntity<>(HttpStatus.FOUND);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/api/public/sendSMS")
    public ResponseEntity<JSONObject> sendMessage(@RequestParam("mobile") String mobile,
                                                  @RequestParam("cid") String cid,
                                                  @RequestParam(value = "uid", defaultValue = "") String uid,
                                                  @RequestParam("answer") String answer) {

        int time = uid.equals("") ? instantMessage.sendMessage(mobile, cid, answer) : instantMessage.sendMessage(mobile, uid, cid, answer);

        if (time > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("time", time);
            if (uid.equals(""))
                jsonObject.put("userId", supplementary.getByMobile(mobile));
            else
                jsonObject.put("userId", uid);

            return new ResponseEntity<>(jsonObject, HttpStatus.OK);
        } else if (time == -1)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        else if (time == -2)
            return new ResponseEntity<>(HttpStatus.FOUND);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/api/public/sendTokenUser/{uid}/{cid}/{answer}")
    public ResponseEntity<JSONObject> sendMessageUser(
            @PathVariable("uid") String uid,
            @PathVariable("cid") String cid,
            @PathVariable("answer") String answer) {
        User user = usersOpRetrieve.retrieveUsers(uid);
        if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        int time = instantMessage.sendMessage(user, cid, answer);

        if (time > 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("time", time);
            jsonObject.put("userId", uid);
            return new ResponseEntity<>(jsonObject, HttpStatus.OK);
        } else if (time == -1)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        else if (time == -2)
            return new ResponseEntity<>(HttpStatus.FOUND);
        else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/api/users/sendMail")
    public ResponseEntity<HttpStatus> sendMultipleMailByAdmin(@RequestBody JSONObject jsonObject) {
        return new ResponseEntity<>(emailService.sendMail(jsonObject), HttpStatus.OK);
    }




    @GetMapping("/api/public/validateMessageToken/{uId}/{token}")
    public ResponseEntity<HttpStatus> resetPassMessage(@PathVariable("uId") String uId, @PathVariable("token") String token) {
        return new ResponseEntity<>(tokenClass.checkToken(uId, token));
    }

    @GetMapping("/api/public/validateEmailToken/{uId}/{token}")
    public RedirectView resetPass(@PathVariable("uId") String uId, @PathVariable("token") String token, RedirectAttributes attributes) {
        HttpStatus httpStatus = tokenClass.checkToken(uId, token);

        if (httpStatus == HttpStatus.OK) {
            attributes.addAttribute("uid", uId);
            attributes.addAttribute("token", token);
            return new RedirectView("/newpassword");
        }
        return null;
    }

    @PostMapping("/api/public/authenticate")
    public ResponseEntity<Response> logIn(@RequestBody JSONObject jsonObject, @RequestParam(name = "lang", defaultValue = Variables.DEFAULT_LANG) String lang) throws Exception {
        String password;
        String userId;
        try {
            userId = jsonObject.getAsString("userId");
            password = jsonObject.getAsString("password");
        } catch (Exception e) {
            return new ResponseEntity<>(new Response(Variables.MODEL_USER, Variables.MODEL_USER,HttpStatus.BAD_REQUEST.value(), lang), HttpStatus.OK);
        }

        int result = authenticate.authenticate(userId, password);
        return new ResponseEntity<>(new Response(result,Variables.MODEL_USER,HttpStatus.OK.value(), lang), HttpStatus.OK);
    }

    @GetMapping("/api/public/counter")
    public ResponseEntity<Response> getCounter(@RequestParam(name = "lang", defaultValue = Variables.DEFAULT_LANG) String lang) throws NoSuchFieldException, IllegalAccessException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("counter", counter);
        return new ResponseEntity<>(new Response(jsonObject,Variables.MODEL_USER,HttpStatus.OK.value(), lang), HttpStatus.OK);
    }

}
