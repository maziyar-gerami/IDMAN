package parsso.idman.Controllers;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.Person;
import parsso.idman.Repos.FilesStorageService;
import parsso.idman.Repos.PersonRepo;
import parsso.idman.utils.FilesStorageService.ResponseMessage;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLOutput;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
public class UsersController {


    @Qualifier("personRepoImpl")
    @Autowired
    private PersonRepo personRepo;

    @Autowired
    FilesStorageService storageService;

    @Value("${administrator.ou.name}")
    private String adminOu;

    private int[] defaultSequence = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};


    @GetMapping("/api/users/u/{uid}")
    public ResponseEntity<Person> retrievePerson(@PathVariable("uid") String userId) {
        return new ResponseEntity<>(personRepo.retrievePerson(userId), HttpStatus.OK);
    }

    @GetMapping("/api/user")
    public ResponseEntity<Person> retrievePerson(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(personRepo.retrievePerson(principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<Person>> retrieveUsersMain() {
        return new ResponseEntity<>(personRepo.retrieveUsersMain(), HttpStatus.OK);
    }

    @PostMapping("/api/users")
    public ResponseEntity<String> bindLdapPerson(@RequestBody Person person) {
        String result = personRepo.create(person);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/api/users/u/{uId}")
    public ResponseEntity<String> rebindLdapPerson(@PathVariable("uId") String uid, @RequestBody Person person) {
        String result = personRepo.update(uid, person);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/api/users/u/{id}")
    public ResponseEntity<String> unbindLdapPerson(@PathVariable("id") String userId) {
        return new ResponseEntity<>(personRepo.remove(userId), HttpStatus.OK);
    }

    @DeleteMapping("/api/users")
    public ResponseEntity<String> unbindAllLdapPerson() {
        return new ResponseEntity<>(personRepo.remove(), HttpStatus.OK);
    }

    @PostMapping("/api/users/import")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file
                                                      //@RequestParam("sequence") int[] sequence,
                                                      //@RequestParam("hasHeader") boolean hasHeader
    ) {
        String message;
        try {
            //storageService.save(file);

            List<Person> users = personRepo.importFileUsers(file, defaultSequence, true);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/api/user/isAdmin")
    public boolean isAdmin (HttpServletRequest request) {
        try {
            Principal principal = request.getUserPrincipal();
            Person person = personRepo.retrievePerson(principal.getName());
            List<String> memberOf = person.getMemberOf();
            if(memberOf.contains(adminOu)) return true;
            else return false;

        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("/api/user/photo")
    public BufferedImage getCurrentUserPhoto (HttpServletRequest request) throws IOException {

            Principal principal = request.getUserPrincipal();
            return personRepo.retrievePic(principal.getName());

    }

    @PostMapping("/api/user/photo")
    public String uploadProfilePic(@RequestParam("file") MultipartFile file,
                                                             HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();


        String message;
        try {

            //storageService.save(file);

            Person person = personRepo.retrievePerson(principal.getName());
            String photo = file.toString();
            byte[] photoBye = photo.getBytes();
            person.setPhoto(photoBye);
            personRepo.update(principal.getName(), person);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();

        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";

        }
        return "redirect:/dashboard";
    }

}
