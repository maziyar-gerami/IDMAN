package parsso.idman.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.Group;
import parsso.idman.Models.User;
import parsso.idman.Repos.GroupRepo;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RestController
public class GroupsController {

    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/api/groups/user")
    public ResponseEntity<List<Group>> retrieveUserOU(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepo.retrieveUser(principal.getName());
        return new ResponseEntity<>(groupRepo.retrieveCurrentUserGroup(user), HttpStatus.OK);
    }

    @PostMapping("/api/groups")
    public ResponseEntity<String> bindLdapGroup(@RequestBody Group ou) {
        String result = groupRepo.create(ou);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/api/groups/{id}")
    public ResponseEntity<String> rebindLdapUser(@RequestBody Group ou, @PathVariable("id") String id) {
        String result = groupRepo.update(id, ou);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/api/groups")
    public ResponseEntity<List<Group>> retrieve() {
        return new ResponseEntity<>(groupRepo.retrieve(), HttpStatus.OK);
    }

    @GetMapping("/api/groups/{id}")
    public ResponseEntity<Group> retrieveOU(@PathVariable("id") String id) {
        return new ResponseEntity<>(groupRepo.retrieveOu(id), HttpStatus.OK);
    }

    @DeleteMapping("/api/groups/{id}")
    public ResponseEntity<String> unbindLdapOU(@PathVariable("id") String id) {
        return new ResponseEntity<>(groupRepo.remove(id), HttpStatus.OK);
    }

    @DeleteMapping("/api/groups")
    public ResponseEntity<String> unbindAllLdapOU() {
        return new ResponseEntity<>(groupRepo.remove(), HttpStatus.OK);
    }

}
