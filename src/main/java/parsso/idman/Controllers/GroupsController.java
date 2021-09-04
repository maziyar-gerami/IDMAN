package parsso.idman.Controllers;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.Groups.Group;
import parsso.idman.Models.Users.User;
import parsso.idman.Models.Users.UsersExtraInfo;
import parsso.idman.Repos.GroupRepo;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class GroupsController {
    @Autowired
    private GroupRepo groupRepo;
    @Autowired
    private UserRepo userRepo;

    //************************************* Pages ****************************************
    @GetMapping("/groups")
    public String Groups() {
        return "groups";
    }

    //************************************* APIs ****************************************

    @GetMapping("/api/groups/user")
    public ResponseEntity<List<Group>> retrieveUserOU(HttpServletRequest request) throws IOException, ParseException {
        User user = userRepo.retrieveUsers(request.getUserPrincipal().getName());
        List<Group> groups = groupRepo.retrieveCurrentUserGroup(user);
        groups.removeAll(Collections.singleton(null));
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @PostMapping("/api/groups")
    public ResponseEntity<HttpStatus> bindLdapGroup(HttpServletRequest request, @RequestBody Group ou) {
        return new ResponseEntity<>(groupRepo.create(request.getUserPrincipal().getName(), ou));
    }

    @PutMapping("/api/groups/{id}")
    public ResponseEntity<HttpStatus> rebindLdapUser(HttpServletRequest request, @RequestBody Group ou, @PathVariable("id") String id) {

        return new ResponseEntity<>(groupRepo.update(request.getUserPrincipal().getName(), id, ou));
    }

    @GetMapping("/api/groups")
    public ResponseEntity<List<Group>> retrieve() {
        List<Group> groups = groupRepo.retrieve();
        groups.removeIf(t -> t.getName() == null);
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @GetMapping("/api/groups/{id}")
    public ResponseEntity<Group> retrieveOU(@PathVariable("id") String id) throws IOException, ParseException {
        return new ResponseEntity<>(groupRepo.retrieveOu(false, id), HttpStatus.OK);
    }

    @DeleteMapping("/api/groups")
    public ResponseEntity<HttpStatus> unbindAllLdapOU(HttpServletRequest request, @RequestBody JSONObject jsonObject) throws IOException, ParseException {
        return new ResponseEntity<>(groupRepo.remove(request.getUserPrincipal().getName(), jsonObject));
    }

    @PutMapping("/api/groups/password/expire")
    public ResponseEntity<List<String>> expireUsersGroupPassword(HttpServletRequest request, @RequestBody JSONObject jsonObject) {

        List<String> preventedUsers = userRepo.expirePassword(request.getUserPrincipal().getName(), jsonObject);

        if (preventedUsers == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else if (preventedUsers.size() == 0)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(preventedUsers, HttpStatus.PARTIAL_CONTENT);

    }

    @PutMapping("/api/groups/password/expire/{groupId}")
    public ResponseEntity<List<String>> expireUsersSpecGroupPassword(HttpServletRequest request, @PathVariable(name = "groupId") String gid) {

        ArrayList temp = new ArrayList();
        JSONObject jsonObject = new JSONObject();
        List<UsersExtraInfo> users = userRepo.retrieveGroupsUsers(gid);
        for (UsersExtraInfo usersExtraInfo : users) {
            temp.add(usersExtraInfo.getUserId());
        }
        jsonObject.put("names", temp);
        List<String> preventedUsers = userRepo.expirePassword(request.getUserPrincipal().getName(), jsonObject);

        if (preventedUsers == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else if (preventedUsers.size() == 0)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(preventedUsers, HttpStatus.PARTIAL_CONTENT);

    }

}
