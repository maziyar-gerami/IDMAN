package parsso.idman.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.Person;
import parsso.idman.Repos.PersonRepo;

import java.util.List;

@RestController
public class UsersController {


    @Qualifier("personRepoImpl")
    @Autowired
    private PersonRepo personRepo;
    // TODO: PATCH

    private int currentOU ;
    private String currentUserId;

    /* public UsersController() {

        Object principal = (null !=SecurityContextHolder.getContext() ?
                SecurityContextHolder.getContext().getAuthentication().getPrincipal():null);
        if (principal instanceof UserDetails) {
            currentUserId = ((UserDetails)principal).getUsername();
        } else {
            currentUserId = principal.toString();
        }

        this.currentOU = organizationalOu(currentUserId);

     */


    public int organizationalOu(String userId) {

        return personRepo.retrieveOU(userId);
    }


    @PostMapping("/api/users")
    public ResponseEntity<String> bindLdapPerson(@RequestBody Person person) {
        String result = personRepo.create(currentOU, person);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/api/users/u/{uId}")
    public ResponseEntity<String> rebindLdapPerson(@PathVariable("uId") String uid,@RequestBody Person person) {
        String result = personRepo.update(uid, person);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /*
    @PutMapping("/api/users/u/{uId}/{attr}")
    public ResponseEntity<String> rebindLdapPerson(@PathVariable("uId") String uid,
                                                   @PathVariable("attr") String attr,
                                                   @RequestBody Person person) {
        String result = personRepo.update(uid, attr, person);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

     */

    @GetMapping("/api/users")
    public ResponseEntity<List<Person>> retrieve() {
        return new ResponseEntity<List<Person>>(personRepo.retrieve(), HttpStatus.OK);
    }

    @GetMapping("/api/{mId}/users")
    public ResponseEntity<List<Person>> retrieveSubUsers(@PathVariable("mId") String mId){
        return new ResponseEntity<List<Person>>(personRepo.retrieveSubUsers(mId), HttpStatus.OK);
    }

    @GetMapping("/api/users/u/{id}")
    public ResponseEntity <Person> retrievePerson(@PathVariable("id") String userId) {
        return new ResponseEntity<Person> (personRepo.retrievePerson(userId), HttpStatus.OK);
    }

    @DeleteMapping("/api/users/u/{id}")
    public ResponseEntity<String> unbindLdapPerson(@PathVariable("id") String userId) {
        return new ResponseEntity<>(personRepo.remove(userId), HttpStatus.OK);
    }

    @DeleteMapping("/api/users")
    public ResponseEntity<String> unbindAllLdapPerson() {
        return new ResponseEntity<>(personRepo.remove(), HttpStatus.OK);
    }

}
