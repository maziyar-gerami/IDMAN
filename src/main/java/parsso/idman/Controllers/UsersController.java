package parsso.idman.Controllers;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.SimpleUser;
import parsso.idman.Models.User;
import parsso.idman.Repos.FilesStorageService;
import parsso.idman.Repos.UserRepo;

import java.io.IOException;
import java.util.List;

/**
 * The type Users controller.
 */
@RestController
public class UsersController {


    // default sequence of variables which can be changed using frontend
    private final int[] defaultSequence = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    /**
     * The Storage service.
     */
    @Autowired
    FilesStorageService storageService;
    @Qualifier("userRepoImpl")
    @Autowired
    private UserRepo userRepo;


    @Value("${api.get.users}")
    private final  static String  apiAddress = null;


    /**
     * Retrieve user with provided uId
     *
     * @param userId the user uId
     * @return the the user object with provided uId
     */
    @GetMapping("/api/users/u/{uid}")
    public ResponseEntity<User> retrieveUser(@PathVariable("uid") String userId) {
        User user = userRepo.retrieveUser(userId);
        if (user == null) return new ResponseEntity<>(user, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Retrieve all users user only with main attributes
     * main attributes are as following: userId, displayName, OU
     *
     * @return the list of simpleUser object
     */
    @GetMapping("/api/users")
    public ResponseEntity<List<SimpleUser>> retrieveUsersMain() {
        List<User> user = userRepo.retrieveUsersFull();
        if (user.size() == 0) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
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
        JSONObject user1 = userRepo.create(user);
        if (user1.equals(new JSONObject())) return new ResponseEntity<>(user1, HttpStatus.OK);
        else return new ResponseEntity<>(user1, HttpStatus.FOUND);

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

}
