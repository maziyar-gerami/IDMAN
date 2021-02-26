package parsso.idman.Controllers;


import net.minidev.json.JSONObject;
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
        User user = userRepo.retrieveUsers(principal.getName());
        return new ResponseEntity<>(groupRepo.retrieveCurrentUserGroup(user), HttpStatus.OK);
    }

    @PostMapping("/api/groups")
    public ResponseEntity<HttpStatus> bindLdapGroup(@RequestBody Group ou) {
        return new ResponseEntity<>(groupRepo.create(ou));
    }

    @PutMapping("/api/groups/{id}")
    public ResponseEntity<HttpStatus> rebindLdapUser(@RequestBody Group ou, @PathVariable("id") String id) {
        return new ResponseEntity<>(groupRepo.update(id, ou));
    }

    @GetMapping("/api/groups")
    public ResponseEntity<List<Group>> retrieve() {
        return new ResponseEntity<>(groupRepo.retrieve(), HttpStatus.OK);
    }

    @GetMapping("/api/groups/{id}")
    public ResponseEntity<Group> retrieveOU(@PathVariable("id") String id) {
        return new ResponseEntity<>(groupRepo.retrieveOu(id), HttpStatus.OK);
    }

    @DeleteMapping("/api/groups")
    public ResponseEntity<HttpStatus> unbindAllLdapOU(@RequestBody JSONObject jsonObject) {
        return new ResponseEntity<>(groupRepo.remove(jsonObject));
    }

}
