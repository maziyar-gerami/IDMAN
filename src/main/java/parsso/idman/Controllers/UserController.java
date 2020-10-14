package parsso.idman.Controllers;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import parsso.idman.Models.SimpleUser;
import parsso.idman.Models.User;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
public class UserController {


    /**
     * The Storage service.
     */


    @Value("${api.get.users}")
    private final static String apiAddress = null;
    // default sequence of variables which can be changed using frontend
    private final int[] defaultSequence = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    @Autowired
    private UserRepo userRepo;
    @Value("${administrator.ou.id}")
    private String adminOu;
    @Value("${profile.photo.path}")
    private String uploadedFilesPath;

    @Value("${pass.api.containg.token}")
    private String tokenExist;


    //*************************************** User Section ***************************************

    /**
     * Retrieve logged-in user
     *
     * @return user object of current user which logged in
     */
    @GetMapping("/api/user")
    public ResponseEntity<User> retrieveUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(userRepo.retrieveUser(principal.getName()), HttpStatus.OK);
    }

    /**
     * Update logged-in user
     *
     * @return http error code
     */
    @PutMapping("/api/user")
    public ResponseEntity<HttpStatus> updateUser(HttpServletRequest request, @RequestBody User user) {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(userRepo.update(principal.getName(), user));
    }

    /**
     * Specify that logged-in user is admin or not
     *
     * @return whether logged-in user is admin or not
     */
    @GetMapping("/api/user/isAdmin")
    public boolean isAdmin(HttpServletRequest request) {
        try {
            Principal principal = request.getUserPrincipal();
            User user = userRepo.retrieveUser(principal.getName());
            List<String> memberOf = user.getMemberOf();


            for (String group : memberOf) {
                if (group.equals(adminOu))
                    return true;
            }

        } catch (Exception e) {
            return false;
        }
        return false;
    }


    /**
     * Get logged-in user photo
     *
     * @return the photo of logged in user
     */
    @GetMapping("/api/user/photo")
    public ResponseEntity<HttpStatus> getImage(HttpServletResponse response, HttpServletRequest request) throws IOException {
        Principal principal = request.getUserPrincipal();
        User user = userRepo.retrieveUser(principal.getName());
        return new ResponseEntity<>(userRepo.showProfilePic(response, user));
    }


    /**
     * Post photo for logged-in user
     *
     * @return the response entity
     */
    @PostMapping("/api/user/photo")
    public RedirectView uploadProfilePic(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
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
        String currentPassword = jsonObject.getAsString("currentPassword");
        String newPassword = jsonObject.getAsString("newPassword");
        String token = jsonObject.getAsString("token");
        if (jsonObject.getAsString("token")!=null) token = jsonObject.getAsString("token");
        return new ResponseEntity<>(userRepo.changePassword(principal.getName(), currentPassword, newPassword, token));

    }

    @GetMapping("/api/user/password/request")
    public HttpStatus requestSMS(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepo.retrieveUser(principal.getName());

        return userRepo.requestToken(user);

    }
    //*************************************** Users Section ***************************************

    /**
     * Retrieve user with provided uId
     *
     * @param userId the user uId
     * @return the the user object with provided uId
     */
    @GetMapping("/api/users/u/{uid}")
    public ResponseEntity<User> retrieveUser(@PathVariable("uid") String userId) {
        if (userRepo.retrieveUser(userId) == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(userRepo.retrieveUser(userId), HttpStatus.OK);
    }

    /**
     * Retrieve all users user only with main attributes
     * main attributes are as following: userId, displayName, OU
     *
     * @return the list of simpleUser object
     */
    @GetMapping("/api/users")
    public ResponseEntity<List<SimpleUser>> retrieveUsersMain() {
        if (userRepo.retrieveUsersFull().size() == 0) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(userRepo.retrieveUsersMain(), HttpStatus.OK);
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
        if (userRepo.create(user).size()==0)
            return new ResponseEntity<>(null, HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.FOUND);

    }

    /**
     * Update user with provided uId and User object
     *
     * @param uid  the uid
     * @param user the user
     * @return the response entity
     */
    @PutMapping("/api/users/u/{uId}")
    public ResponseEntity<String> rebindLdapUser(@PathVariable("uId") String uid, @RequestBody User user) {
        return new ResponseEntity<>(userRepo.update(uid, user));
    }


    /**
     * Delete a User with provided uId.
     *
     * @param userId the user id
     * @return the response entity
     */
    @DeleteMapping("/api/users/u/{id}")
    public ResponseEntity<String> unbindLdapUser(@PathVariable("id") String userId) {
        return new ResponseEntity<>(userRepo.remove(userId), HttpStatus.OK);
    }

    /**
     * Delete all users
     *
     * @return the response entity
     */
    @DeleteMapping("/api/users")
    public ResponseEntity<String> unbindAllLdapUser() {
        return new ResponseEntity<>(userRepo.remove(), HttpStatus.OK);
    }

    /**
     * Enable users
     *
     * @return the response entity
     */
    @PutMapping("/api/users/enable/u/{id}")
    public ResponseEntity<HttpStatus> enable(@PathVariable("id") String uid) {
        return new ResponseEntity<>(userRepo.enable(uid));
    }

    /**
     * Disable users
     *
     * @return the response entity
     */
    @PutMapping("/api/users/disable/u/{id}")
    public ResponseEntity<HttpStatus> disable(@PathVariable("id") String uid) {
        return new ResponseEntity<>(userRepo.disable(uid));
    }




    /**
     * lock/unlock users
     *
     * @return the response entity
     */
    @PutMapping("/api/users/unlock/u/{id}")
    public ResponseEntity<HttpStatus> lockUnlock(@PathVariable("id") String uid) {
        return new ResponseEntity<>(userRepo.unlock(uid));
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
    public ResponseEntity<JSONArray> uploadFile(@RequestParam("file") MultipartFile file
                                                //@RequestParam(value= "sequence", defaultValue = defaultSequence) int[] sequence,
                                                //@RequestParam("hasHeader") boolean hasHeader
    ) throws IOException {

        JSONArray jsonArray = userRepo.importFileUsers(file, defaultSequence, true);
        if (jsonArray.get(0).equals(new JSONObject())) return new ResponseEntity<>(jsonArray, HttpStatus.OK);
        else return new ResponseEntity<>(jsonArray, HttpStatus.FOUND);
    }



    /**
     * get the information for dashboard
     *
     * @return a json file containing tha data
     */
    @GetMapping("/api/dashboard")
    public ResponseEntity<JSONObject> retrieveDashboardData() throws ParseException, java.text.ParseException, IOException {
        return new ResponseEntity<>(userRepo.retrieveDashboardData(), HttpStatus.OK);
    }


    //*************************************** Public Controllers ***************************************
    /**
     * send Email for reset password
     *
     * @param email
     * @return the http status code
     */
    @GetMapping("/api/public/sendMail/{email}")
    public ResponseEntity<HttpStatus> sendMail(@PathVariable("email") String email) {
        return new ResponseEntity<>(userRepo.sendEmail(email), HttpStatus.OK);
    }

    /**
     * send SMS for reset password
     *
     * @param mobile
     * @return the http status code
     */
    @GetMapping("/api/public/sendSMS/{mobile}")
    public ResponseEntity<HttpStatus> sendMessage(@PathVariable("mobile") String mobile) {
        return new ResponseEntity<>(null, userRepo.sendMessage(mobile));
    }

    /**
     * send SMS for reset password
     *
     * @param mobile and userId
     * @return the http status code
     */
    @GetMapping("/api/public/sendSMS/{mobile}/{uid}")
    public ResponseEntity<HttpStatus> sendMessage(@PathVariable("mobile") String mobile, @PathVariable("uid") String uid) {
        return new ResponseEntity<>(null, userRepo.sendMessage(mobile, uid));
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
        return new ResponseEntity<List<JSONObject>>(userRepo.checkMobile(mobile), HttpStatus.OK);
    }

    /**
     * Gets the token and corresponds it with provided userID
     *
     * @param token and userId
     * @return the user object if exists, or null if not exists
     */
    @PutMapping("/api/public/resetPass/{uid}/{token}")
    public ResponseEntity<HttpStatus> rebindLdapUser(@RequestParam("newPassword") String newPassword, @PathVariable("token") String token, @PathVariable("uid") String uid) {
        return new ResponseEntity<>(userRepo.updatePass(uid, newPassword, token));
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
     * sends email to specified user
     *
     * @param email and userId
     * @return if token is correspond to provided email, returns httpStatus=ok
     */
    @GetMapping("/api/public/sendMail/{email}/{uid}")
    public ResponseEntity<HttpStatus> sendMail(@PathVariable("email") String email, @PathVariable("uid") String uid) {
        return new ResponseEntity<>(userRepo.sendEmail(email, uid), HttpStatus.OK);
    }

    /**
     * validate the token send via email provided for specified userId
     *
     * @param  uId and email
     * @return if token is correspond to provided email, redirects to resetPassword page
     */
    @GetMapping("/api/public/validateEmailToken/{uId}/{token}")
    public RedirectView resetPass(@PathVariable("uId") String uId, @PathVariable("token") String token, RedirectAttributes attributes) {
        HttpStatus httpStatus = userRepo.checkToken(uId, token);

        if (httpStatus == HttpStatus.OK) {
            attributes.addAttribute("uid", uId);
            attributes.addAttribute("token", token);

            return new RedirectView("/resetPassword");
        }
        return null;
    }

    /**
     * validate the token provided for spcified userId
     *
     * @param  uId and email
     * @return if token is correspond to provided userId, returns httpStatus=ok
     */
    @GetMapping("/api/public/validateMessageToken/{uId}/{token}")
    public ResponseEntity<HttpStatus> resetPass(@PathVariable("uId") String uId, @PathVariable("token") String token) {
        return new ResponseEntity<>(userRepo.checkToken(uId, token));

    }
}
