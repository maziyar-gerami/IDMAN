package parsso.idman.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.Group;
import parsso.idman.Repos.GroupRepo;


import java.sql.SQLOutput;
import java.util.List;

@RestController
public class GroupsController {

    @Autowired
    private GroupRepo groupRepo;

    @PostMapping("/api/groups")
    public ResponseEntity<String> bindLdapPerson(@RequestBody Group ou) {
        String result = groupRepo.create(ou);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/api/groups/{name}")
    public ResponseEntity<String> rebindLdapPerson(@RequestBody Group ou, @PathVariable("name") String name) {
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
