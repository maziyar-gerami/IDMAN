package parsso.idman.controllers.usersController;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.bind.annotation.*;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.communicate.InstantMessage;
import parsso.idman.helpers.communicate.Token;
import parsso.idman.helpers.reloadConfigs.PwdAttributeMapper;
import parsso.idman.models.other.Notification;
import parsso.idman.models.other.PWD;
import parsso.idman.models.response.Response;
import parsso.idman.repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RestController
public class PasswordOp extends UsersOps{
    final UserRepo.PasswordOp passwordOp;
    final InstantMessage instantMessage;

    @Value("${base.url}")
    private String BASE_URL;

    @Autowired
    public PasswordOp(Token tokenClass, LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, UserRepo.UsersOp.Retrieve usersOpRetrieve, UserRepo.PasswordOp passwordOp,InstantMessage instantMessage) {
        super(tokenClass, ldapTemplate, mongoTemplate,usersOpRetrieve);
        this.passwordOp = passwordOp;
        this.instantMessage = instantMessage;
    }


    @PutMapping("/api/users/password/expire")
    public ResponseEntity<List<String>> expirePassword(HttpServletRequest request,
                                                       @RequestBody JSONObject jsonObject) {
        List<String> preventedUsers = passwordOp.expire(request.getUserPrincipal().getName(), jsonObject);

        if (preventedUsers == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else if (preventedUsers.size() == 0)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(preventedUsers, HttpStatus.PARTIAL_CONTENT);

    }


    @PutMapping("/api/public/resetPass/{uid}/{token}")
    public ResponseEntity<Response> rebindLdapUser(@RequestParam("newPassword") String newPassword,
                                                   @RequestParam(value = "lang", defaultValue = "fa")  String lang, @PathVariable("token") String token,
                                                   @PathVariable("uid") String uid) throws NoSuchFieldException, IllegalAccessException {
        JSONObject objectResult = new JSONObject();
        String dn = "cn=DefaultPPolicy,ou=Policies," + BASE_DN;
        PWD pwd = this.ldapTemplate.lookup(dn, new PwdAttributeMapper());
        int pwdin = Integer.parseInt(pwd.getPwdInHistory().replaceAll("[^0-9]", ""));
        objectResult.put("pwdInHistory", pwdin);

        HttpStatus httpStatus = passwordOp.reset(uid, newPassword, token, pwdin);

        if(httpStatus == HttpStatus.OK)
            return new ResponseEntity<>(new Response(null, Variables.MODEL_USER,HttpStatus.OK.value(), lang),HttpStatus.OK);

        return new ResponseEntity<>(new Response(null,Variables.MODEL_USER,httpStatus.value(),lang),HttpStatus.OK);

    }

    @PutMapping("/api/user/password")
    public ResponseEntity<Response> changePassword(HttpServletRequest request, @RequestParam(value = "lang", defaultValue = "fa") String lang,
                                                   @RequestBody JSONObject jsonObject) throws NoSuchFieldException, IllegalAccessException {
        JSONObject objectResult = new JSONObject();
        String newPassword = jsonObject.getAsString("newPassword");
        String token = jsonObject.getAsString("token");
        if (jsonObject.getAsString("token") != null) token = jsonObject.getAsString("token");

        HttpStatus httpStatus = passwordOp.change(request.getUserPrincipal().getName(), newPassword, token);

        String pwdInHistory = "";
        int pwdin = -1;

        if (httpStatus == HttpStatus.FOUND) {
            try {
                String dn = "cn=DefaultPPolicy,ou=Policies," + BASE_DN;
                PWD pwd = this.ldapTemplate.lookup(dn, new PwdAttributeMapper());
                pwdin = Integer.parseInt(pwd.getPwdInHistory().replaceAll("[^0-9]", ""));
                objectResult.put("pwdInHistory", pwdin);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        try {
            pwdin = Integer.parseInt(pwdInHistory);
        } catch (Exception ignored) {
        }
        objectResult.put("pwdInHistory", pwdin);

        if(httpStatus == HttpStatus.OK)
            return new ResponseEntity<>(new Response(objectResult,Variables.MODEL_USER,HttpStatus.OK.value(), lang),HttpStatus.OK);

        return new ResponseEntity<>(new Response(objectResult,Variables.MODEL_USER,httpStatus.value(),lang),HttpStatus.OK);

    }


    @PutMapping("/api/public/changePassword")
    public ResponseEntity<Response> changePasswordWithoutToken(@RequestBody JSONObject jsonObject, @RequestParam(value = "lang", defaultValue = "fa") String lang) throws NoSuchFieldException, IllegalAccessException {
        String currentPassword = jsonObject.getAsString("currentPassword");
        String newPassword = jsonObject.getAsString("newPassword");
        String userId = jsonObject.getAsString("userId");
        HttpStatus httpStatus = passwordOp.changePublic(userId, currentPassword, newPassword);

        if (Boolean.parseBoolean(new Settings(mongoTemplate).retrieve(Variables.PASSWORD_CHANGE_NOTIFICATION).getValue()) && httpStatus == HttpStatus.OK)
            new Notification(mongoTemplate).sendPasswordChangeNotify(usersOpRetrieve.retrieveUsers(userId),BASE_URL);


        if(httpStatus == HttpStatus.OK)
            return new ResponseEntity<>(new Response(null,Variables.MODEL_USER,HttpStatus.OK.value(), lang),HttpStatus.OK);

        return new ResponseEntity<>(new Response(null,Variables.MODEL_USER,httpStatus.value(),lang),HttpStatus.OK);

    }

    @GetMapping("/api/user/password/request")
    public ResponseEntity<Integer> requestSMS(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        parsso.idman.models.users.User user = usersOpRetrieve.retrieveUsers(principal.getName());
        int status = instantMessage.sendMessage(user);

        if (status > 0)
            return new ResponseEntity<>(status, HttpStatus.OK);
        else
            return new ResponseEntity<>(status, HttpStatus.FORBIDDEN);
    }



}
