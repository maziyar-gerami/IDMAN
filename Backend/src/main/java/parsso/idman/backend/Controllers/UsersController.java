package parsso.idman.backend.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.backend.Models.Person;
import parsso.idman.backend.Repos.PersonRepo;

import java.util.List;

@RestController
public class UsersController {

    @Qualifier("personRepoImpl")
    @Autowired
    private PersonRepo personRepo;

    @PostMapping("/users/add-user")
    public ResponseEntity<String> bindLdapPerson(@RequestBody Person person) {
        String result = personRepo.create(person);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PutMapping("/users/update-user")

    public ResponseEntity<String> rebindLdapPerson(@RequestBody Person person) {
        String result = personRepo.update(person);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/users/retrieve-users")
    public ResponseEntity<List<Person>> retrieve() {
        return new ResponseEntity<List<Person>>(personRepo.retrieve(), HttpStatus.OK);
    }

    @GetMapping("/users/retrieve-user")
    public ResponseEntity <Person> retrievePerson(@RequestParam(name = "userId") String userId) {
        return new ResponseEntity<Person> (personRepo.retrievePerson(userId), HttpStatus.OK);
    }

    @GetMapping("/users/remove-user")
    public ResponseEntity<String> unbindLdapPerson(@RequestParam(name = "userId") String userId) {
        return new ResponseEntity<>(personRepo.remove(userId), HttpStatus.OK);
    }

}