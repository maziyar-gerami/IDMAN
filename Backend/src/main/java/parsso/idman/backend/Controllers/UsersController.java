package parsso.idman.backend.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
<<<<<<< HEAD
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
=======
>>>>>>> a0d0f3a5dec3208e6b55845dd55e395a072dce44
import org.springframework.web.bind.annotation.*;
import parsso.idman.backend.Models.Person;
import parsso.idman.backend.Repos.PersonRepo;

import java.util.List;

@RestController
public class UsersController {

    @Qualifier("personRepoImpl")
    @Autowired
    private PersonRepo personRepo;
<<<<<<< HEAD
    // TODO: PTATCH
    // TODO: private String currentUserId ;
    private int parentOU ;

    /* TODO: public void getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserId = authentication.getName();
        }

        this.parentOU = organizationalOu(currentUserId);
        System.out.println(parentOU);
    }

    public int organizationalOu(String userId) {

        return personRepo.retrieveOU(userId);
    }

     */
    @PostMapping("/api/users")
    public ResponseEntity<String> bindLdapPerson(@RequestBody Person person) {
        String result = personRepo.create(parentOU, person);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/api/users/u/{uId}")
    public ResponseEntity<String> rebindLdapPerson(@PathVariable("uId") String uid,@RequestBody Person person) {
        String result = personRepo.update(uid, person);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/api/users")
=======

    @PostMapping("/add-user")
    public ResponseEntity<String> bindLdapPerson(@RequestBody Person person) {
        String result = personRepo.create(person);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PutMapping("/update-user")

    public ResponseEntity<String> rebindLdapPerson(@RequestBody Person person) {
        String result = personRepo.update(person);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/retrieve-users")
>>>>>>> a0d0f3a5dec3208e6b55845dd55e395a072dce44
    public ResponseEntity<List<Person>> retrieve() {
        return new ResponseEntity<List<Person>>(personRepo.retrieve(), HttpStatus.OK);
    }

<<<<<<< HEAD
    @GetMapping("/api/{mId}/users")
    public ResponseEntity<List<Person>> retrieveSubUsers(@PathVariable("mId") String mId){
        return new ResponseEntity<List<Person>>(personRepo.retrieveSubUsers(mId), HttpStatus.OK);
    }

    @GetMapping("/api/users/u/{id}")
    public ResponseEntity <Person> retrievePerson(@PathVariable("id") String userId) {
        return new ResponseEntity<Person> (personRepo.retrievePerson(userId), HttpStatus.OK);
    }

    @DeleteMapping("/api/users/u/{id}")
    public ResponseEntity<String> unbindLdapPerson(@PathVariable("id") int userId) {
        return new ResponseEntity<>(personRepo.remove(userId), HttpStatus.OK);
    }

    @DeleteMapping("/api/users")
    public ResponseEntity<String> unbindAllLdapPerson() {
        return new ResponseEntity<>(personRepo.remove(), HttpStatus.OK);
    }

=======
    @GetMapping("/retrieve-user")
    public ResponseEntity <Person> retrievePerson(@RequestParam(name = "userId") String userId) {
        return new ResponseEntity<Person> (personRepo.retrievePerson(userId), HttpStatus.OK);
    }

    @GetMapping("/remove-user")
    public ResponseEntity<String> unbindLdapPerson(@RequestParam(name = "userId") String userId) {
        return new ResponseEntity<>(personRepo.remove(userId), HttpStatus.OK);
    }

>>>>>>> a0d0f3a5dec3208e6b55845dd55e395a072dce44
}