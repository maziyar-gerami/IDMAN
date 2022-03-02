package parsso.idman.controllers;


import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.helpers.Variables;
import parsso.idman.models.groups.Group;
import parsso.idman.models.response.Response;
import parsso.idman.models.users.User;
import parsso.idman.repoImpls.groups.helper.ExpirePassword;
import parsso.idman.repoImpls.groups.CreateGroup;
import parsso.idman.repoImpls.groups.DeleteGroup;
import parsso.idman.repoImpls.groups.RetrieveGroup;
import parsso.idman.repoImpls.groups.UpdateGroup;
import parsso.idman.repos.GroupRepo;
import parsso.idman.repos.UserRepo;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@RequestMapping("/api/groups")
@RestController
public class GroupsController {
    private final UserRepo.UsersOp.Retrieve retrieveUsers;
    private final UserRepo.PasswordOp passwordOp;
    private final GroupRepo.Retrieve retrieveGroup;
    private final GroupRepo.Update updateGroup;
    private final GroupRepo.Delete deleteGroup;
    private final GroupRepo.Create createGroup;


    @Autowired
    public GroupsController(UserRepo.UsersOp.Retrieve retrieveUsers, UserRepo.PasswordOp passwordOp,
                            RetrieveGroup retrieveGroup, UpdateGroup updateGroup, DeleteGroup deleteGroup, CreateGroup createGroup) {
        this.retrieveUsers = retrieveUsers;
        this.passwordOp = passwordOp;
        this.retrieveGroup = retrieveGroup;
        this.updateGroup = updateGroup;
        this.deleteGroup = deleteGroup;
        this.createGroup = createGroup;
    }

    @GetMapping("/user")
    public ResponseEntity<Response> retrieveUserOU(HttpServletRequest request,
                                                   @RequestParam(value = "lang",defaultValue = "fa") String lang) throws NoSuchFieldException, IllegalAccessException {
        User user = retrieveUsers.retrieveUsers(request.getUserPrincipal().getName());
        if (user == null)
            return new ResponseEntity<>(new Response(null, Variables.MODEL_GROUP,
                    HttpStatus.FORBIDDEN.value(), lang),HttpStatus.OK);

        List<Group> groups = retrieveGroup.retrieve(user);
        groups.removeAll(Collections.singleton(null));
        return new ResponseEntity<>(new Response(groups, Variables.MODEL_GROUP,
                HttpStatus.OK.value(), lang),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Response> bindLdapGroup(HttpServletRequest request,
                                                  @RequestParam(value = "lang",defaultValue = "fa") String lang, @RequestBody Group group) throws NoSuchFieldException, IllegalAccessException {
        return new ResponseEntity<>(new Response(null, Variables.MODEL_GROUP,
                createGroup.create(request.getUserPrincipal().getName(),group).value(), lang),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> retrieveGroups(@RequestParam(value = "id", defaultValue = "") String id,
                                            @RequestParam(value = "lang",defaultValue = "fa") String lang) throws NoSuchFieldException, IllegalAccessException {
        if (!id.equals(""))
        return new ResponseEntity<>(new Response(retrieveGroup.retrieve(false, id), Variables.MODEL_GROUP,
                HttpStatus.OK.value(), lang),HttpStatus.OK);
        else {
            List<Group> groups = retrieveGroup.retrieve();
            groups.removeIf(t -> t.getName() == null);
            return new ResponseEntity<>(new Response(groups, Variables.MODEL_GROUP,
                    HttpStatus.OK.value(), lang),HttpStatus.OK);
        }
    }

    @DeleteMapping
    public ResponseEntity<Response> unbindAllLdapOU(HttpServletRequest request, @RequestBody JSONObject jsonObject,
                                                      @RequestParam(value = "lang",defaultValue = "fa") String lang) throws NoSuchFieldException, IllegalAccessException {
        return new ResponseEntity<>(new Response(null, Variables.MODEL_GROUP,
                deleteGroup.remove(request.getUserPrincipal().getName(), jsonObject).value(), lang),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Response> rebind(HttpServletRequest request, @RequestParam(value = "id") String ouID,
                                             @RequestBody Group ou,@RequestParam(value = "lang",defaultValue = "fa") String lang) throws NoSuchFieldException, IllegalAccessException {
        return new ResponseEntity<>(new Response(null, Variables.MODEL_GROUP,
                updateGroup.update(request.getUserPrincipal().getName(), ouID, ou).value(), lang),HttpStatus.OK);
    }

    @PutMapping("/password/expire")
    public ResponseEntity<Response> expireUsersGroupPassword(HttpServletRequest request,
                                                      @RequestBody JSONObject jsonObject,
                                                      @RequestParam(value = "id", defaultValue = "") String id,
                                                      @RequestParam(value = "lang",defaultValue = "fa") String lang) throws NoSuchFieldException, IllegalAccessException {

        String userId = request.getUserPrincipal().getName();

        if (!id.equals(""))
            return new ResponseEntity<>(new Response(passwordOp.expire(userId, jsonObject),Variables.MODEL_GROUP, HttpStatus.OK.value(),lang), HttpStatus.OK);
        else
            return new ResponseEntity<>(new ExpirePassword(passwordOp,retrieveUsers).expireUsersSpecGroupPassword(request,id,lang),HttpStatus.OK);

    }
}
