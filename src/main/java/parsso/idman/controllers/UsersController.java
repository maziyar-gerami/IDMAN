package parsso.idman.controllers;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.communicate.InstantMessage;
import parsso.idman.helpers.communicate.Token;
import parsso.idman.helpers.reloadConfigs.PwdAttributeMapper;
import parsso.idman.helpers.user.ImportUsers;
import parsso.idman.helpers.user.Operations;
import parsso.idman.helpers.user.UsersExcelView;
import parsso.idman.models.other.Notification;
import parsso.idman.models.other.PWD;
import parsso.idman.models.other.SkyRoom;
import parsso.idman.models.response.Response;
import parsso.idman.models.users.ListUsers;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.EmailService;
import parsso.idman.repos.SkyroomRepo;
import parsso.idman.repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@ControllerAdvice
public class UsersController {
    final ImportUsers importUsers;
    final Operations operations;
    final EmailService emailService;
    // default sequence of variables which can be changed using frontend
    private final int[] defaultSequence = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    private final UserRepo userRepo;
    private final UsersExcelView excelView;
    private final Token tokenClass;
    private final InstantMessage instantMessage;
    private final SkyroomRepo skyroomRepo;
    private final LdapTemplate ldapTemplate;
    private final PwdAttributeMapper pwdAttributeMapper;
    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;
    @Value("${base.url}")
    private String BASE_URL;
    @Value("${cas.authn.passwordless.tokens.expireInSeconds}")
    private String counter;
    final String model = Variables.MODEL_USER;
    private final MongoTemplate mongoTemplate;


    @Autowired
    public UsersController(UserRepo userRepo, EmailService emailService, Operations operations, UsersExcelView excelView,
                           ImportUsers importUsers, Token tokenClass, InstantMessage instantMessage, SkyroomRepo skyroomRepo, LdapTemplate ldapTemplate, PwdAttributeMapper pwdAttributeMapper, MongoTemplate mongoTemplate) {
        this.userRepo = userRepo;
        this.emailService = emailService;
        this.operations = operations;
        this.importUsers = importUsers;
        this.excelView = excelView;
        this.tokenClass = tokenClass;
        this.instantMessage = instantMessage;
        this.skyroomRepo = skyroomRepo;
        this.ldapTemplate = ldapTemplate;
        this.pwdAttributeMapper = pwdAttributeMapper;
        this.mongoTemplate = mongoTemplate;
    }


    @GetMapping("/api/skyroom")
    public ResponseEntity<SkyRoom> skyroom(HttpServletRequest request) throws IOException {

        return new ResponseEntity<>(skyroomRepo.Run(userRepo.retrieveUsers(request.getUserPrincipal().getName())), HttpStatus.OK);
    }


    @PutMapping("/api/users/password/expire")
    public ResponseEntity<List<String>> expirePassword(HttpServletRequest request,
                                                       @RequestBody JSONObject jsonObject) {
        List<String> preventedUsers = userRepo.expirePassword("request.getUserPrincipal().getName()", jsonObject);

        if (preventedUsers == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else if (preventedUsers.size() == 0)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(preventedUsers, HttpStatus.PARTIAL_CONTENT);

    }

    @GetMapping("/api/users/u/{uid}")
    public ResponseEntity<User> retrieveUser(@PathVariable("uid") String userId) {
        User user = userRepo.retrieveUsers(userId);
        if (user == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/api/users/license/u/{uid}")
    public ResponseEntity<User> retrieveUserLicense(@PathVariable("uid") String userId) {
        User user = userRepo.retrieveUsersWithLicensed(userId);
        if (user == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<UsersExtraInfo>> retrieveUsersMain() {
        return new ResponseEntity<>(userRepo.retrieveUsersMain(-1, -1), HttpStatus.OK);
    }

    @GetMapping("/api/users/{page}/{n}")
    public ResponseEntity<ListUsers> retrieveUsersMain(@PathVariable("page") int page, @PathVariable("n") int n,
                                                       @RequestParam(name = "sortType", defaultValue = "") String sortType,
                                                       @RequestParam(name = "groupFilter", defaultValue = "") String groupFilter,
                                                       @RequestParam(name = "searchUid", defaultValue = "") String searchUid,
                                                       @RequestParam(name = "userStatus", defaultValue = "") String userStatus,
                                                       @RequestParam(name = "mobile", defaultValue = "") String mobile,
                                                       @RequestParam(name = "searchDisplayName", defaultValue = "") String searchDisplayName) {
        return new ResponseEntity<>(userRepo.retrieveUsersMain(page, n, sortType, groupFilter, searchUid, searchDisplayName,mobile, userStatus), HttpStatus.OK);
    }

    @GetMapping("/api/users/group/{groupId}")
    public ResponseEntity<ListUsers> retrieveUsersMainWithGroupId(@PathVariable(name = "groupId") String groupId,
                                                                  @RequestParam(name = "page", defaultValue = "1") int page,
                                                                  @RequestParam(name = "nRec", defaultValue = "90000") int nRec) {
        ListUsers users = userRepo.retrieveUsersMainWithGroupId(groupId, page, nRec);
        if (users == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/api/users/group/{groupId}")
    public ResponseEntity<HttpStatus> massUsersGroupUpdate(HttpServletRequest request,
                                                           @RequestBody JSONObject gu,
                                                           @PathVariable(name = "groupId") String groupId) {
        return new ResponseEntity<>(userRepo.massUsersGroupUpdate(request.getUserPrincipal().getName(), groupId, gu));
    }

    @PostMapping("/api/users")
    public ResponseEntity<JSONObject> bindLdapUser(HttpServletRequest request, @RequestBody User user) {
        JSONObject jsonObject = userRepo.create(request.getUserPrincipal().getName(), user);

        if (jsonObject == null || jsonObject.size() == 0)
            return new ResponseEntity<>(null, HttpStatus.OK);
        else return new ResponseEntity<>(jsonObject, HttpStatus.FOUND);

    }

    @PutMapping("/api/users/u/{uId}")
    public ResponseEntity<JSONObject> rebindLdapUser(HttpServletRequest request, @PathVariable("uId") String uid, @RequestBody User user) {
        JSONObject objectResult = new JSONObject();
        String dn = "cn=DefaultPPolicy,ou=Policies," + BASE_DN;
        PWD pwd = this.ldapTemplate.lookup(dn, pwdAttributeMapper);
        int pwdin = Integer.parseInt(pwd.getPwdInHistory().replaceAll("[^0-9]", ""));
        objectResult.put("pwdInHistory", pwdin);
        return new ResponseEntity<>(objectResult, userRepo.update(request.getUserPrincipal().getName(), uid, user));
    }

    @DeleteMapping("/api/users")
    public ResponseEntity<List<String>> unbindAllLdapUser(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        Principal principal = request.getUserPrincipal();
        List<String> names = userRepo.remove(principal.getName(), jsonObject);
        if (names.size() == 0)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(names, HttpStatus.PARTIAL_CONTENT);
    }

    @PutMapping("/api/users/operation")
    public ResponseEntity<HttpStatus> operation(HttpServletRequest request,
                                                @RequestParam("id") String uid,
                                                @RequestParam("operation") String operation) {
        switch (operation) {
            case "unlock":
                return new ResponseEntity<>(operations.unlock(request.getUserPrincipal().getName(), uid));
            case "enable":
                return new ResponseEntity<>(operations.enable(request.getUserPrincipal().getName(), uid));
            case "disable":
                return new ResponseEntity<>(operations.disable(request.getUserPrincipal().getName(), uid));
            default:
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        }

    }

    @PostMapping("/api/users/sendMail")
    public ResponseEntity<HttpStatus> sendMultipleMailByAdmin(@RequestBody JSONObject jsonObject) {
        return new ResponseEntity<>(emailService.sendMail(jsonObject), HttpStatus.OK);
    }

    @PostMapping("/api/users/import")
    public ResponseEntity<JSONObject> uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {

        JSONObject jsonObject = importUsers.importFileUsers(request.getUserPrincipal().getName(), file, defaultSequence, true);
        if (Integer.parseInt(jsonObject.getAsString("nUnSuccessful")) == 0)
            return new ResponseEntity<>(jsonObject, HttpStatus.OK);
        else return new ResponseEntity<>(jsonObject, HttpStatus.FOUND);
    }

    @PutMapping("/api/users/import/massUpdate")
    public ResponseEntity<JSONObject> updateConflicts(HttpServletRequest request, @RequestBody List<User> users) {
        return new ResponseEntity<>(userRepo.massUpdate(request.getUserPrincipal().getName(), users), HttpStatus.OK);
    }

    @GetMapping("/api/users/export")
    public ModelAndView downloadExcel() {

        return new ModelAndView(excelView, "listUsers", Object.class);
    }

    @PutMapping("/api/users/ou/{ou}")
    public ResponseEntity<List<String>> addGroups(HttpServletRequest request, @RequestParam("file") MultipartFile file, @PathVariable("ou") String ou) throws IOException {
        List<String> notExist = userRepo.addGroupToUsers(request.getUserPrincipal().getName(), file, ou);
        if (ou.equals("none"))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (notExist == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        if (notExist.size() == 0)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(notExist, HttpStatus.PARTIAL_CONTENT);
    }


    @GetMapping("/api/user")
    public ResponseEntity<parsso.idman.models.users.User> retrieveUser(HttpServletRequest request) {

        return new ResponseEntity<>(userRepo.retrieveUsers(request.getUserPrincipal().getName()), HttpStatus.OK);
    }

    @PutMapping("/api/user")
    public ResponseEntity<HttpStatus> updateUser(HttpServletRequest request, @RequestBody parsso.idman.models.users.User user) {
        return new ResponseEntity<>(userRepo.update(request.getUserPrincipal().getName(), request.getUserPrincipal().getName(), user));
    }

    @GetMapping("/api/user/photo")
    public ResponseEntity<String> getImage(HttpServletResponse response, HttpServletRequest request) {
        parsso.idman.models.users.User user = userRepo.retrieveUsers(request.getUserPrincipal().getName());
        return new ResponseEntity<>(userRepo.showProfilePic(response, user), HttpStatus.OK);
    }

    @PostMapping("/api/user/photo")
    public ResponseEntity<Response> uploadProfilePic(@RequestParam("file") MultipartFile file, HttpServletRequest request, @RequestParam(name = "lang", defaultValue = "fa") String lang) throws NoSuchFieldException, IllegalAccessException {
        if (userRepo.uploadProfilePic(file, request.getUserPrincipal().getName()))
            return new ResponseEntity<>(new Response(true, lang), HttpStatus.OK);
        return new ResponseEntity<>(new Response(lang, model, false, 400), HttpStatus.OK);
    }

    @DeleteMapping("/api/user/photo")
    public ResponseEntity<Response> deleteImage(HttpServletRequest request, @RequestParam(name = "lang", defaultValue = "fa") String lang) throws NoSuchFieldException, IllegalAccessException {
        parsso.idman.models.users.User user = userRepo.retrieveUsers(request.getUserPrincipal().getName());
        if (userRepo.deleteProfilePic(user))
            return new ResponseEntity<>(new Response(true, lang), HttpStatus.OK);
        return new ResponseEntity<>(new Response(lang, model, false, 400), HttpStatus.OK);
    }

    @PutMapping("/api/user/password")
    public ResponseEntity<Response> changePassword(HttpServletRequest request, @RequestParam(value = "lang", defaultValue = "fa") String lang,
                                                     @RequestBody JSONObject jsonObject) throws NoSuchFieldException, IllegalAccessException {
        JSONObject objectResult = new JSONObject();
        String newPassword = jsonObject.getAsString("newPassword");
        String token = jsonObject.getAsString("token");
        if (jsonObject.getAsString("token") != null) token = jsonObject.getAsString("token");

        HttpStatus httpStatus = userRepo.changePassword(request.getUserPrincipal().getName(), newPassword, token);

        String pwdInHistory = "";
        int pwdin = -1;

        if (httpStatus == HttpStatus.FOUND) {
            try {
                String dn = "cn=DefaultPPolicy,ou=Policies," + BASE_DN;
                PWD pwd = this.ldapTemplate.lookup(dn, pwdAttributeMapper);
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
            return new ResponseEntity<>(new Response(objectResult,Variables.MODEL_USER, lang),HttpStatus.OK);

        return new ResponseEntity<>(new Response(lang,Variables.MODEL_USER, objectResult,httpStatus.value()),HttpStatus.OK);

    }


    @PutMapping("/api/public/changePassword")
    public ResponseEntity<Response> changePasswordWithoutToken(@RequestBody JSONObject jsonObject, @RequestParam(value = "lang", defaultValue = "fa") String lang) throws NoSuchFieldException, IllegalAccessException {
        String currentPassword = jsonObject.getAsString("currentPassword");
        String newPassword = jsonObject.getAsString("newPassword");
        String userId = jsonObject.getAsString("userId");
        HttpStatus httpStatus = userRepo.changePasswordPublic(userId, currentPassword, newPassword);

        if (Boolean.parseBoolean(new Settings(mongoTemplate).retrieve(Variables.PASSWORD_CHANGE_NOTIFICATION).getValue()) && httpStatus == HttpStatus.OK)
            new Notification(mongoTemplate).sendPasswordChangeNotify(userRepo.retrieveUsers(userId),BASE_URL);


        if(httpStatus == HttpStatus.OK)
            return new ResponseEntity<>(new Response(Variables.MODEL_USER, lang),HttpStatus.OK);

        return new ResponseEntity<>(new Response(lang,Variables.MODEL_USER,httpStatus.value()),HttpStatus.OK);

    }

    @GetMapping("/api/user/password/request")
    public ResponseEntity<Integer> requestSMS(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        parsso.idman.models.users.User user = userRepo.retrieveUsers(principal.getName());
        int status = instantMessage.sendMessage(user);

        if (status > 0)
            return new ResponseEntity<>(status, HttpStatus.OK);
        else
            return new ResponseEntity<>(status, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/api/public/sendMail")
    public ResponseEntity<Integer> sendMail(@RequestParam("email") String email,
                                            @RequestParam(value = "uid", defaultValue = "") String uid,
                                            @RequestParam("cid") String cid,
                                            @RequestParam("answer") String answer) {
        int time = uid.equals("") ? userRepo.sendEmail(email, null, cid, answer) : userRepo.sendEmail(email, uid, cid, answer);
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
                jsonObject.put("userId", userRepo.getByMobile(mobile));
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
        User user = userRepo.retrieveUsers(uid);
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

    @PutMapping("/api/public/resetPass/{uid}/{token}")
    public ResponseEntity<Response> rebindLdapUser(@RequestParam("newPassword") String newPassword,
    @RequestParam(value = "lang", defaultValue = "fa")  String lang, @PathVariable("token") String token,
                                                     @PathVariable("uid") String uid) throws NoSuchFieldException, IllegalAccessException {
        JSONObject objectResult = new JSONObject();
        String dn = "cn=DefaultPPolicy,ou=Policies," + BASE_DN;
        PWD pwd = this.ldapTemplate.lookup(dn, pwdAttributeMapper);
        int pwdin = Integer.parseInt(pwd.getPwdInHistory().replaceAll("[^0-9]", ""));
        objectResult.put("pwdInHistory", pwdin);

        HttpStatus httpStatus = userRepo.resetPassword(uid, newPassword, token, pwdin);

        if(httpStatus == HttpStatus.OK)
            return new ResponseEntity<>(new Response(Variables.MODEL_USER, lang),HttpStatus.OK);

        return new ResponseEntity<>(new Response(lang,Variables.MODEL_USER,httpStatus.value()),HttpStatus.OK);

    }

    @GetMapping("/api/public/checkMail/{email}")
    public HttpEntity<List<JSONObject>> checkMail(@PathVariable("email") String email) {
        return new ResponseEntity<>(emailService.checkMail(email), HttpStatus.OK);
    }

    @GetMapping("/api/public/checkMobile/{mobile}")
    public HttpEntity<List<JSONObject>> checkMobile(@PathVariable("mobile") String mobile) {
        return new ResponseEntity<>(instantMessage.checkMobile(mobile), HttpStatus.OK);
    }

    @GetMapping("/api/public/getName/{uid}/{token}")
    public ResponseEntity<User> getName(@PathVariable("uid") String uid, @PathVariable("token") String token) {
        User user = userRepo.getName(uid, token);
        HttpStatus httpStatus = (user != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(user, httpStatus);
    }


    @GetMapping("/api/public/counter")
    public ResponseEntity<Response> getCounter(@RequestParam(name = "lang", defaultValue = "fa") String lang) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("counter", counter);
        return new ResponseEntity<>(new Response(jsonObject, lang), HttpStatus.OK);
    }

    @PostMapping("/api/public/authenticate")
    public ResponseEntity<Response> logIn(@RequestBody JSONObject jsonObject, @RequestParam(name = "lang", defaultValue = "fa") String lang) throws Exception {
        String password;
        String userId;
        try {
            userId = jsonObject.getAsString("userId");
            password = jsonObject.getAsString("password");
        } catch (Exception e) {
            return new ResponseEntity<>(new Response(lang, model, 400), HttpStatus.OK);
        }

        int result = userRepo.authenticate(userId, password);
        return new ResponseEntity<>(new Response(result, lang), HttpStatus.OK);
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

    @GetMapping("/api/public/validateMessageToken/{uId}/{token}")
    public ResponseEntity<HttpStatus> resetPassMessage(@PathVariable("uId") String uId, @PathVariable("token") String token) {
        return new ResponseEntity<>(tokenClass.checkToken(uId, token));
    }


}
