package parsso.idman.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.Person;
import parsso.idman.Repos.PersonRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.List;
import java.util.UUID;

@RestController
public class UsersController {


    @Qualifier("personRepoImpl")
    @Autowired
    private PersonRepo personRepo;

    @Value("${administrator.ou.name}")
    private String adminOu;

    @PostMapping("/api/users")
    public ResponseEntity<String> bindLdapPerson(@RequestBody Person person){
        String result = personRepo.create(person);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/api/users/u/{uId}")
    public ResponseEntity<String> rebindLdapPerson(@PathVariable("uId") String uid, @RequestBody Person person) {
        String result = personRepo.update(uid,person);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }




    @GetMapping("/api/users")
    public ResponseEntity<List<Person>> retrieve() {
        return new ResponseEntity<List<Person>>(personRepo.retrieve(), HttpStatus.OK);
    }


    @GetMapping("/api/users/u/{id}")
    public ResponseEntity <Person> retrievePerson(@PathVariable("id") String userId) {
        return new ResponseEntity<Person> (personRepo.retrievePerson(userId), HttpStatus.OK);
    }

    @GetMapping("/api/users/checkMail/{email}")
    public ResponseEntity <List<Person>> checkMail (@PathVariable("email") String email) {
        return new ResponseEntity<List<Person>> (personRepo.checkMail(email), HttpStatus.OK);
    }

    @GetMapping("/api/users/sendMail/{email}")
    public ResponseEntity <String> sendMail(@PathVariable("email") String email) {
        return new ResponseEntity<String> (personRepo.sendEmail(email), HttpStatus.OK);
    }

    @GetMapping("/api/users/sendMail/{email}/{uid}")
    public ResponseEntity <String> sendMail(@PathVariable("email") String email,@PathVariable ("uid") String uid) {
        return new ResponseEntity<String> (personRepo.sendEmail(email, uid), HttpStatus.OK);
    }

    @DeleteMapping("/api/users/u/{id}")
    public ResponseEntity<String> unbindLdapPerson(@PathVariable("id") String userId) {
        return new ResponseEntity<>(personRepo.remove(userId), HttpStatus.OK);
    }

    @DeleteMapping("/api/users")
    public ResponseEntity<String> unbindAllLdapPerson() {
        return new ResponseEntity<>(personRepo.remove(), HttpStatus.OK);
    }

    @PutMapping("/api/users/u/{uid}/{pass}/{token}")
    public ResponseEntity<String> rebindLdapPerson
            (@PathVariable("uid") String uid,@PathVariable("pass") String pass,@PathVariable("token") String token) {
        String result = personRepo.updatePass(uid,pass,token);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/api/users/isAdmin/{uid}")
    public boolean isAdmin (@PathVariable("uid") String uid, HttpServletRequest request) {
        try {
            Person person = personRepo.retrievePerson(uid);
            List<String> memberOf = person.getMemberOf();
            if(memberOf.contains(adminOu)){
                    return true;
            }else{
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
