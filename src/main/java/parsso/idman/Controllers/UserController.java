package parsso.idman.Controllers;

import net.bytebuddy.asm.Advice;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.compress.utils.IOUtils;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import parsso.idman.Models.Group;
import parsso.idman.Models.SimpleUser;
import parsso.idman.Models.User;
import parsso.idman.Repos.FilesStorageService;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class UserController {



    @Autowired
    FilesStorageService storageService;
    @Autowired
    private UserRepo userRepo;


    @Value("${administrator.ou.id}")
    private String adminOu;

    @Value("${profile.photo.path}")
    private String uploadedFilesPath;

    // default sequence of variables which can be changed using frontend
    private final int[] defaultSequence = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    /**
     * The Storage service.
     */


    @Value("${api.get.users}")
    private final  static String  apiAddress = null;


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


            for (String group:memberOf) {
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

        File file = new File(uploadedFilesPath+user.getPhotoName());


        if (file.exists()) {
            try {
                String contentType = "image/png";
                response.setContentType(contentType);
                OutputStream out = response.getOutputStream();
                FileInputStream in = new FileInputStream(file);
                // copy from in to out
                IOUtils.copy(in, out);
                out.close();
                in.close();
                return new ResponseEntity<HttpStatus>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);

            }
        }

        return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);


    }


    /**
     * Post photo for logged-in user
     *
     * @return the response entity
     */
    public RedirectView uploaduProfilePic(@RequestParam("file") MultipartFile file,
                                         HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();


        String message;
        try {

            Date date = new Date();

            long timeStamp = date.getTime();
            String StTime = String.valueOf(timeStamp);


            storageService.save(file, StTime + file.getOriginalFilename());

            User user = userRepo.retrieveUser(principal.getName());




            user.setPhotoName(StTime + file.getOriginalFilename());

            userRepo.update(user.getUserId(), user);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();

        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";

        }
        System.out.println(message);
        return new RedirectView("/dashboard");
    }

    @PostMapping("/api/user/photo")
    public RedirectView uploadProfilePic(@RequestParam("file") MultipartFile file,
                                         HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        File file1=null;


        String message;
        try {

            String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(System.currentTimeMillis());


            String s =timeStamp + file.getOriginalFilename();

            storageService.save(file, s);




            User user = userRepo.retrieveUser(principal.getName());

            //remove old pic
            file1 = new File(uploadedFilesPath+user.getPhotoName());




            user.setPhotoName(s);
            userRepo.update(user.getUserId(), user);

        } catch (Exception e) {

        }
        file1.delete();

        return new RedirectView("/dashboard");
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
        if (userRepo.create(user).equals(new JSONObject())) return new ResponseEntity<>(userRepo.create(user), HttpStatus.OK);
        else return new ResponseEntity<>(userRepo.create(user), HttpStatus.FOUND);

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

        //return new ResponseEntity<>(userRepo.importFileUsers(file, defaultSequence, true), HttpStatus.OK);

        JSONArray jsonArray = userRepo.importFileUsers(file, defaultSequence, true);
        if (jsonArray.get(0).equals(new JSONObject())) return new ResponseEntity<>(jsonArray, HttpStatus.OK);
        else return new ResponseEntity<>(jsonArray, HttpStatus.FOUND);
    }

    @PutMapping("/api/user/password")
    public ResponseEntity<HttpStatus> changePassword(HttpServletRequest request,
                                 @RequestParam("currentPassword") String currentPassword,
                                 @RequestParam ("newPassword") String newPassword)
    {
        //Principal principal = request.getUserPrincipal();
        return new ResponseEntity<HttpStatus>(userRepo.changePassword("bardia",currentPassword,newPassword), HttpStatus.FOUND);

    }

    @GetMapping("/api/dashboard")
    public ResponseEntity<JSONObject> retrieveDashboardData() throws ParseException, java.text.ParseException, IOException {
        return new ResponseEntity<JSONObject>(userRepo.retrieveDashboardData(), HttpStatus.OK);
    }


    //*************************************** Public Controllers ***************************************

    @GetMapping("/api/public/sendMail/{email}")
    public ResponseEntity<String> sendMail(@PathVariable("email") String email) {
        return new ResponseEntity<String>(userRepo.sendEmail(email), HttpStatus.OK);
    }

    @GetMapping("/api/public/sendSMS/{mobile}")
    public ResponseEntity<HttpStatus> sendMessage(@PathVariable("mobile") String mobile) {
        return new ResponseEntity<>(null, userRepo.sendMessage(mobile));
    }

    @GetMapping("/api/public/sendSMS/{mobile}/{uid}")
    public ResponseEntity<HttpStatus> sendMessage(@PathVariable("mobile") String mobile, @PathVariable("uid") String uid) {
        return new ResponseEntity<>(null,userRepo.sendMessage(mobile,uid));
    }

    @GetMapping("/api/public/checkMail/{email}")
    public HttpEntity<List<JSONObject>> checkMail(@PathVariable("email") String email) {
        return new ResponseEntity<List<JSONObject>>(userRepo.checkMail(email), HttpStatus.OK);
    }

    @GetMapping("/api/public/checkMobile/{mobile}")
    public HttpEntity<List<JSONObject>> checkMobile(@PathVariable("mobile") String mobile) {
        return new ResponseEntity<List<JSONObject>>(userRepo.checkMobile(mobile), HttpStatus.OK);
    }

    @PutMapping("/api/public/resetPass/{uid}/{token}")
    public ResponseEntity<String> rebindLdapUser(@RequestParam("newPassword") String newPassword,@PathVariable("token") String token, @PathVariable("uid") String uid) {
        return new ResponseEntity<>(userRepo.updatePass(uid, newPassword, token), HttpStatus.OK);
    }


    @GetMapping("/api/public/getName/{uid}/{token}")
    public ResponseEntity<User> getName (@PathVariable("uid") String uid,@PathVariable("token") String token) {
        User user = userRepo.getName(uid, token);
        if (user!=null)
            return new ResponseEntity<>(user, HttpStatus.OK);
        return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api/public/sendMail/{email}/{uid}")
    public ResponseEntity<String> sendMail(@PathVariable("email") String email, @PathVariable("uid") String uid) {
        return new ResponseEntity<>(userRepo.sendEmail(email, uid), HttpStatus.OK);
    }



    @GetMapping("/api/public/validateEmailToken/{uId}/{token}")
    public RedirectView resetPass(@PathVariable("uId") String uId, @PathVariable("token") String token, RedirectAttributes attributes) {
        HttpStatus httpStatus = userRepo.checkToken(uId, token);

        if (httpStatus==HttpStatus.OK) {
            attributes.addAttribute("uid", uId);
            attributes.addAttribute("token",token);


            return new RedirectView("/resetPassword");
        }
        return null;
    }

    @GetMapping("/api/public/validateMessageToken/{uId}/{token}")
    public HttpStatus resetPass(@PathVariable("uId") String uId, @PathVariable("token") String token) {
        return userRepo.checkToken(uId, token);


    }




}
