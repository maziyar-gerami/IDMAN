package parsso.idman.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.Group;
import parsso.idman.Models.User;
import parsso.idman.Repos.UserRepo;
import parsso.idman.Repos.GroupRepo;

import java.security.Principal;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
public class GroupsController {

    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/api/group")
    public ResponseEntity<List<Group>> retrieveUserOU(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepo.retrieveUser(principal.getName());
        List<String> memberOf = user.getMemberOf();
        List<Group> groups = new ArrayList<Group>();
        try{
            for(int i = 0; i < memberOf.size(); ++i){
                groups.add(groupRepo.retrieveOu(memberOf.get(i)));
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @PostMapping("/api/groups")
    public ResponseEntity<String> bindLdapUser(@RequestBody Group ou) {
        String result = groupRepo.create(ou);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/api/groups/{name}")
    public ResponseEntity<String> rebindLdapUser(@RequestBody Group ou, @PathVariable("name") String name) {
        String result = groupRepo.update(name, ou);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/api/groups")
    public ResponseEntity<List<Group>> retrieve() {
        return new ResponseEntity<>(groupRepo.retrieve(), HttpStatus.OK);
    }

    @GetMapping("/api/groups/{name}")
    public ResponseEntity <Group> retrieveOU(@PathVariable("name") String name) {
        return new ResponseEntity<> (groupRepo.retrieveOu(name), HttpStatus.OK);
    }

    @DeleteMapping("/api/groups/{name}")
    public ResponseEntity<String> unbindLdapOU(@PathVariable("name") String name) {
        return new ResponseEntity<>(groupRepo.remove(name), HttpStatus.OK);
    }

    @DeleteMapping("/api/groups")
    public ResponseEntity<String> unbindAllLdapOU() {
        return new ResponseEntity<>(groupRepo.remove(), HttpStatus.OK);
    }

}
