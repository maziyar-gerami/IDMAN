package parsso.idman.Controllers;


import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import parsso.idman.Helpers.Communicate.InstantMessage;
import parsso.idman.Helpers.Communicate.Token;
import parsso.idman.Helpers.User.UsersExcelView;
import parsso.idman.Models.Users.ListUsers;
import parsso.idman.Models.Users.SimpleUser;
import parsso.idman.Models.Users.User;
import parsso.idman.RepoImpls.SystemRefreshRepoImpl;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@ComponentScan
public class UsersController {


    /**
     * The Storage service.
     */

    // default sequence of variables which can be changed using frontend
    private final int[] defaultSequence = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    @Autowired
    Token tokenClass;
    @Autowired
    UsersExcelView excelView;
    @Autowired
    SystemRefreshRepoImpl systemRefreshRepoImpl;
    @Autowired
    private InstantMessage instantMessage;
    @Autowired
    private UserRepo userRepo;
    @Value("${administrator.ou.id}")
    private String adminOu;
    @Value("${token.valid.email}")
    private String tokenValidEmail;



    //*************************************** APIs ***************************************

    //########### User Section ###########

    /**
     * Retrieve logged-in user
     *
     * @return user object of current user which logged in
     */
    @GetMapping("/api/user")
    public ResponseEntity<User> retrieveUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(userRepo.retrieveUsers(principal.getName()), HttpStatus.OK);
    }

    /**
     * Update logged-in user
     *
     * @return http error code
     */
    @PutMapping("/api/user")
    public ResponseEntity<HttpStatus> updateUser(HttpServletRequest request, @RequestBody User user) {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(userRepo.update(principal.getName(),principal.getName(), user));
    }

    /**
     * Specify that logged-in user is admin or not
     *
     * @return whether logged-in user is admin or not
     */
    @Deprecated
    @GetMapping("/api/user/isAdmin")
    public int isAdmin(HttpServletRequest request) {
        try {
            Principal principal = request.getUserPrincipal();
            User user = userRepo.retrieveUsers(principal.getName());
            List<String> memberOf = user.getMemberOf();

            if (user.getUsersExtraInfo().getRole().equals("SUPERADMIN"))
                return 0;

            else if (memberOf.contains(adminOu))
                return 1;

            else
                return 2;

        } catch (Exception e) {
            return 400;
        }
    }


    /**
     * Get logged-in user photo
     *
     * @return the photo of logged in user
     */
    @GetMapping("/api/user/photo")
    public ResponseEntity<String> getImage(HttpServletResponse response, HttpServletRequest request) throws IOException {
        Principal principal = request.getUserPrincipal();
        User user = userRepo.retrieveUsers(principal.getName());
        return new ResponseEntity<>(userRepo.showProfilePic(response, user), HttpStatus.OK);
    }

    /**
     * Post photo for logged-in user
     *
     * @return the response entity
     */
    @PostMapping("/api/user/photo")
    public RedirectView uploadProfilePic(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        userRepo.uploadProfilePic(file, request.getUserPrincipal().getName());
        return new RedirectView("/dashboard");
    }

    /**
     * change the password of current user
     *
     * @param jsonObject
     * @return the http status code
     */
    @PutMapping("/api/user/password")
    public ResponseEntity<HttpStatus> changePassword(HttpServletRequest request,
                                                     @RequestBody JSONObject jsonObject) {
        Principal principal = request.getUserPrincipal();
        String oldPassword = jsonObject.getAsString("currentPassword");
        String newPassword = jsonObject.getAsString("newPassword");
        String token = jsonObject.getAsString("token");
        if (jsonObject.getAsString("token") != null) token = jsonObject.getAsString("token");

        return new ResponseEntity<>(userRepo.changePassword(principal.getName(),oldPassword, newPassword, token));

    }

    @GetMapping("/api/user/password/request")
    public ResponseEntity<Integer> requestSMS(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepo.retrieveUsers(principal.getName());
        int status = userRepo.requestToken(user);

        if (status > 0)
            return new ResponseEntity<>(status, HttpStatus.OK);
        else
            return new ResponseEntity<>(status, HttpStatus.FORBIDDEN);
    }
    //########### Users Section ###########

    /**
     * Retrieve user with provided uId
     *
     * @param userId the user uId
     * @return the the user object with provided uId
     */
    @GetMapping("/api/users/u/{uid}")
    public ResponseEntity<User> retrieveUser(@PathVariable("uid") String userId) {
        if (userRepo.retrieveUsers(userId) == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(userRepo.retrieveUsers(userId), HttpStatus.OK);
    }

    /**
     * Retrieve user with main variables
     *
     * @return the the user object with provided uId
     */
    @GetMapping("/api/users")
    public ResponseEntity<List<SimpleUser>> retrieveUsersMain() {
        return new ResponseEntity<>(userRepo.retrieveUsersMain(-1,-1), HttpStatus.OK);
    }

    @GetMapping("/api/users/group/{groupId}")
    public ResponseEntity<ListUsers> retrieveUsersMainWithGroupId(@PathVariable(name = "groupId") String groupId,
                                                                         @RequestParam(name = "page", defaultValue = "1") int page,
                                                                         @RequestParam(name = "nRec", defaultValue = "90000") int nRec) {
        ListUsers users = userRepo.retrieveUsersMainWithGroupId(groupId, page,nRec);
        if (users == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/api/users/group/{groupId}")
    public ResponseEntity<HttpStatus> massUsersGroupUpdate(HttpServletRequest request,
                                                           @RequestBody JSONObject gu,
                                                           @PathVariable (name = "groupId") String groupId) {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(userRepo.massUsersGroupUpdate(principal.getName(),groupId, gu));
    }

    /**
     * Retrieve all users user only with main attributes
     * main attributes are as following: userId, displayName, OU
     *
     * @return the list of simpleUser object
     */
    @GetMapping("/api/users/{page}/{n}")
    public ResponseEntity<ListUsers> retrieveUsersMain(@PathVariable("page") int page, @PathVariable("n") int n,
                                                       @RequestParam(name = "sortType", defaultValue = "") String sortType,
                                                       @RequestParam(name = "groupFilter", defaultValue = "") String groupFilter,
                                                       @RequestParam(name = "searchUid", defaultValue = "") String searchUid,
                                                       @RequestParam(name = "userStatus", defaultValue = "") String userStatus,
                                                       @RequestParam(name = "searchDisplayName", defaultValue = "") String searchDisplayName) {
            return new ResponseEntity<>(userRepo.retrieveUsersMain(page, n, sortType, groupFilter, searchUid, searchDisplayName, userStatus), HttpStatus.OK);
    }

    /**
     * Retrieve all users with all attributes
     *
     * @return the list of user object
     */
    @GetMapping("/api/users/full")
    public ResponseEntity<List<User>> retrieveUsers() {
        if (userRepo.retrieveUsersFull().size() == 0) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(userRepo.retrieveUsersFull(), HttpStatus.OK);
    }

    /**
     * Create user
     *
     * @param user the user
     * @return the response entity
     */
    @PostMapping("/api/users")
    public ResponseEntity<JSONObject> bindLdapUser(@RequestBody User user) {
        JSONObject jsonObject = userRepo.create("maziyar", user);

        if (jsonObject==null || jsonObject.size() == 0)
            return new ResponseEntity<>(null, HttpStatus.OK);
        else return new ResponseEntity<>(jsonObject, HttpStatus.FOUND);

    }

    /**
     * Update user with provided uId and User object
     *
     * @param uid  the uid
     * @param user the user
     * @return the response entity
     */
    @PutMapping("/api/users/u/{uId}")
    public ResponseEntity<String> rebindLdapUser(HttpServletRequest request,@PathVariable("uId") String uid, @RequestBody User user) {

        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(userRepo.update(principal.getName(),uid, user));

    }

    /**
     * Delete users
     *
     * @return the response entity
     */
    @DeleteMapping("/api/users")
    public ResponseEntity<List<String>> unbindAllLdapUser(HttpServletRequest request,@RequestBody JSONObject jsonObject) {
        Principal principal = request.getUserPrincipal();
        List<String> names = userRepo.remove(principal.getName(),jsonObject);
        if (names.size() == 0)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(names, HttpStatus.PARTIAL_CONTENT);
    }

    /**
     * Enable users
     *
     * @return the response entity
     */
    @PutMapping("/api/users/enable/u/{id}")
    public ResponseEntity<HttpStatus> enable(HttpServletRequest request, @PathVariable("id") String uid) {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(userRepo.enable(principal.getName(),uid));
    }

    /**
     * Disable users
     *
     * @return the response entity
     */
    @PutMapping("/api/users/disable/u/{id}")
    public ResponseEntity<HttpStatus> disable(HttpServletRequest request, @PathVariable("id") String uid) {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(userRepo.disable(principal.getName(),uid));
    }


    /**
     * lock/unlock users
     *
     * @return the response entity
     */
    @PutMapping("/api/users/unlock/u/{id}")
    public ResponseEntity<HttpStatus> lockUnlock(HttpServletRequest request, @PathVariable("id") String uid) {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(userRepo.unlock(principal.getName(),uid));
    }

    /**
     * Upload file for importing users using following formats:
     * LDIF,xlsx,xls,csv
     *
     * @param file the file
     * @return the response entity
     * @throws IOException the io exception
     */
    @PostMapping("/api/users/import")
    public ResponseEntity<JSONObject> uploadFile(HttpServletRequest request,@RequestParam("file") MultipartFile file) throws IOException {

        JSONObject jsonObject = userRepo.importFileUsers(request.getUserPrincipal().getName(),file, defaultSequence, true);
        if (Integer.valueOf(jsonObject.getAsString("nUnSuccessful")) == 0)
            return new ResponseEntity<>(jsonObject, HttpStatus.OK);
        else return new ResponseEntity<>(jsonObject, HttpStatus.FOUND);
    }

    @PutMapping("/api/users/import/massUpdate")
    public ResponseEntity<JSONObject> updateConflicts(HttpServletRequest request,@RequestBody List<User> users) {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(userRepo.massUpdate(principal.getName(),users));
    }

    /**
     * send Email for reset password
     *
     * @param jsonObject
     * @return the http status code
     */
    @PostMapping("/api/users/sendMail")
    public ResponseEntity<HttpStatus> sendMultipleMailByAdmin(@RequestBody JSONObject jsonObject) {
        return new ResponseEntity<>(userRepo.sendEmail(jsonObject), HttpStatus.OK);
    }


    @GetMapping("/api/users/export")
    public ModelAndView downloadExcel() {

        return new ModelAndView(excelView, "listUsers", null);
    }

    @GetMapping("/api/users/sync")
    public ResponseEntity<HttpStatus> simpleUsersSync() {
        return new ResponseEntity<>(userRepo.syncUsersDBs());
    }

    @PutMapping("/api/users/ou/{ou}")
    public ResponseEntity<List<String>> addGroups(@RequestParam("file") MultipartFile file, @PathVariable("ou") String ou) throws IOException {
        List<String> notExist = userRepo.addGroupToUsers(file, ou);
        if (notExist==null)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (notExist.size()==0)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(notExist, HttpStatus.PARTIAL_CONTENT);
    }

    //########### Public Controllers ###########

    /**
     * sends email to specified user
     *
     * @param email and userId
     * @return if token is correspond to provided email, returns httpStatus=ok
     */
    @GetMapping("/api/public/sendMail/{email}/{uid}/{cid}/{answer}")
    public ResponseEntity<Integer> sendMail(@PathVariable("email") String email,
                                            @PathVariable("uid") String uid,
                                            @PathVariable("cid") String cid,
                                            @PathVariable("answer") String answer) {
        int time = userRepo.sendEmail(email, uid, cid, answer);
        if (time > 0)
            return new ResponseEntity<>(time, HttpStatus.OK);
        else if (time == -1)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        else
            return new ResponseEntity<>(time, HttpStatus.BAD_REQUEST);
    }


    /**
     * send Email for reset password
     *
     * @param email
     * @return the http status code
     */
    @GetMapping("/api/public/sendMail/{email}/{cid}/{answer}")
    public ResponseEntity<Integer> sendMail(@PathVariable("email") String email,
                                            @PathVariable("cid") String cid,
                                            @PathVariable("answer") String answer) {

        int time = userRepo.sendEmail(email,null, cid, answer);
        if (time > 0)
            return new ResponseEntity<>(time, HttpStatus.OK);
        else if (time == -1)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    /**
     * send SMS for reset password
     *
     * @param mobile
     * @return the http status code
     */
    @GetMapping("/api/public/sendSMS/{mobile}/{cid}/{answer}")
    public ResponseEntity<Integer> sendMessage(@PathVariable("mobile") String mobile,
                                               @PathVariable("cid") String cid,
                                               @PathVariable("answer") String answer) {
        int time = instantMessage.sendMessage(mobile, cid, answer);
        if (time > 0)
            return new ResponseEntity<>(time, HttpStatus.OK);
        else if (time == -1)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * send SMS for reset password
     *
     * @param mobile and userId
     * @return the http status code
     */
    @GetMapping("/api/public/sendSMS/{mobile}/{uid}/{cid}/{answer}")
    public ResponseEntity<Integer> sendMessage(@PathVariable("mobile") String mobile,
                                               @PathVariable("uid") String uid,
                                               @PathVariable("cid") String cid,
                                               @PathVariable("answer") String answer) {
        int time = instantMessage.sendMessage(mobile, uid, cid, answer);
        if (time > 0)
            return new ResponseEntity<>(time, HttpStatus.OK);
        else if (time == -1)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    /**
     * Gets the token and corresponds it with provided userID
     *
     * @param token and userId
     * @return the user object if exists, or null if not exists
     */
    @PutMapping("/api/public/resetpassword/{uid}/{token}")
    public ResponseEntity<HttpStatus> rebindLdapUser(@RequestParam("newPassword") String newPassword, @PathVariable("token") String token,
                                                     @PathVariable("uid") String uid) {
        return new ResponseEntity<>(userRepo.updatePass(uid, newPassword, token));
    }


    /**
     * check if an email exists in ldap
     *
     * @param email
     * @return the user object if exists, or null if not exists
     */
    @GetMapping("/api/public/checkMail/{email}")
    public HttpEntity<List<JSONObject>> checkMail(@PathVariable("email") String email) {
        return new ResponseEntity<List<JSONObject>>(userRepo.checkMail(email), HttpStatus.OK);
    }

    /**
     * check if a mobile exists in ldap
     *
     * @param mobile
     * @return the user object if exists, or null if not exists
     */
    @GetMapping("/api/public/checkMobile/{mobile}")
    public HttpEntity<List<JSONObject>> checkMobile(@PathVariable("mobile") String mobile) {
        return new ResponseEntity<List<JSONObject>>(instantMessage.checkMobile(mobile), HttpStatus.OK);
    }


    /**
     * Gets the name from userId for showing in the ressetPasseord page
     *
     * @param token and userId
     * @return if token is correspond to provided userID, returns the user; else returns null
     */
    @GetMapping("/api/public/getName/{uid}/{token}")
    public ResponseEntity<User> getName(@PathVariable("uid") String uid, @PathVariable("token") String token) {
        User user = userRepo.getName(uid, token);
        if (user != null)
            return new ResponseEntity<>(user, HttpStatus.OK);
        return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
    }


    /**
     * validate the token send via email provided for specified userId
     *
     * @param uId and email
     * @return if token is correspond to provided email, redirects to newpassword page
     */
    @GetMapping("/api/public/validateEmailToken/{uId}/{token}")
    public RedirectView resetPass(@PathVariable("uId") String uId, @PathVariable("token") String token, RedirectAttributes attributes) {
        HttpStatus httpStatus = tokenClass.checkToken(uId, token);

        if (httpStatus == HttpStatus.OK) {

            return new RedirectView("/newpassword");
        }
        return null;
    }


    /**
     * validate the token provided for spcified userId
     *
     * @param uId and email
     * @return if token is correspond to provided userId, returns httpStatus=ok
     */
    @GetMapping("/api/public/validateMessageToken/{uId}/{token}")
    public ResponseEntity<HttpStatus> resetPassMessage(@PathVariable("uId") String uId, @PathVariable("token") String token) {
        return new ResponseEntity<>(tokenClass.checkToken(uId, token));
    }


}
