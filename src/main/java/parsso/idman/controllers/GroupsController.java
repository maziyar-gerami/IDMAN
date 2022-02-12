package parsso.idman.controllers;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import parsso.idman.models.groups.Group;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.GroupRepo;
import parsso.idman.repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
@RequestMapping("/api/groups")
@RestController
public class GroupsController {
    private final GroupRepo groupRepo;
    private final UserRepo userRepo;

    public GroupsController(UserRepo userRepo, GroupRepo groupRepo) {
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;

    }

    @GetMapping("/user")
    public ResponseEntity<List<Group>> retrieveUserOU(HttpServletRequest request) {
        User user = userRepo.retrieveUsers(request.getUserPrincipal().getName());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<Group> groups = groupRepo.retrieveCurrentUserGroup(user);
        groups.removeAll(Collections.singleton(null));
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> bindLdapGroup(HttpServletRequest request, @RequestBody Group ou) {
        return new ResponseEntity<>(groupRepo.create(request.getUserPrincipal().getName(), ou));
    }

    @GetMapping
    public ResponseEntity<?> retrieveGroups(@RequestParam(value = "id", defaultValue = "") String id) {
        if (!id.equals(""))
            return new ResponseEntity<>(groupRepo.retrieveOu(false, id), HttpStatus.OK);
        else {
            List<Group> groups = groupRepo.retrieve();
            groups.removeIf(t -> t.getName() == null);
            return new ResponseEntity<>(groups, HttpStatus.OK);
        }
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> unbindAllLdapOU(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        return new ResponseEntity<>(groupRepo.remove(request.getUserPrincipal().getName(), jsonObject));
    }

    @PutMapping
    public ResponseEntity<HttpStatus> rebind(HttpServletRequest request, @RequestParam(value = "id") String ouID, @RequestBody Group ou) {
        return new ResponseEntity<>(groupRepo.update(request.getUserPrincipal().getName(), ouID, ou));
    }

    @PutMapping("/password/expire")
    public ResponseEntity<?> expireUsersGroupPassword(HttpServletRequest request,
                                                      @RequestBody JSONObject jsonObject,
                                                      @RequestParam(value = "id", defaultValue = "") String id) {

        String userId = request.getUserPrincipal().getName();

        if (!id.equals(""))
            return new ResponseEntity<>(userRepo.expirePassword(userId, jsonObject), HttpStatus.OK);
        else
            return expireUsersSpecGroupPassword(request, id);

    }

    private ResponseEntity<String> expirePassword(String name, JSONObject jsonObject) {

        List<String> preventedUsers = userRepo.expirePassword(name, jsonObject);

        if (preventedUsers == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else if (preventedUsers.size() == 0)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>((MultiValueMap<String, String>) preventedUsers, HttpStatus.PARTIAL_CONTENT);
    }

    public ResponseEntity<String> expireUsersSpecGroupPassword(HttpServletRequest request, String gid) {

        ArrayList<String> temp = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        List<UsersExtraInfo> users = userRepo.retrieveGroupsUsers(gid);
        for (UsersExtraInfo usersExtraInfo : users) {
            temp.add(usersExtraInfo.get_id().toString());
        }
        jsonObject.put("names", temp);
        return expirePassword(request.getUserPrincipal().getName(), jsonObject);
    }

}
