package parsso.idman.Controllers;


import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.Groups.Group;
import parsso.idman.Models.Users.User;
import parsso.idman.Repos.GroupRepo;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
public class GroupsController {

    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private UserRepo userRepo;

    //*************************************** Pages ***************************************

    @GetMapping("/groups")
    public String Groups() {
        return "groups";
    }

    //*************************************** APIs ***************************************

    @GetMapping("/api/groups/user")
    public ResponseEntity<List<Group>> retrieveUserOU(HttpServletRequest request) {
        User user = userRepo.retrieveUsers(request.getUserPrincipal().getName());
        return new ResponseEntity<>(groupRepo.retrieveCurrentUserGroup(user), HttpStatus.OK);
    }

    @PostMapping("/api/groups")
    public ResponseEntity<HttpStatus> bindLdapGroup(HttpServletRequest request,@RequestBody Group ou) {
        return new ResponseEntity<>(groupRepo.create(request.getUserPrincipal().getName(),ou));
    }

    @PutMapping("/api/groups/{id}")
    public ResponseEntity<HttpStatus> rebindLdapUser(HttpServletRequest request,@RequestBody Group ou, @PathVariable("id") String id) {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(groupRepo.update(principal.getName(),id, ou));
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
    public ResponseEntity<HttpStatus> unbindAllLdapOU(HttpServletRequest request,@RequestBody JSONObject jsonObject) {
        return new ResponseEntity<>(groupRepo.remove(request.getUserPrincipal().getName(), jsonObject));
    }



}
