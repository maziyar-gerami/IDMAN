package parsso.idman.controllers.usersController.usersOps;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.controllers.usersController.UsersOps;
import parsso.idman.helpers.communicate.Token;
import parsso.idman.helpers.reloadConfigs.PwdAttributeMapper;
import parsso.idman.helpers.user.Operations;
import parsso.idman.models.other.PWD;
import parsso.idman.models.users.User;
import parsso.idman.repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
public class UpdateController extends UsersOps {

    final private UserRepo.UsersOp.Update updateOpUpdate;
    final private Operations operations;

    @Autowired
    public UpdateController(Operations operations, Token tokenClass, LdapTemplate ldapTemplate,
                            MongoTemplate mongoTemplate, UserRepo.UsersOp.Retrieve userOpRetrieve, UserRepo.UsersOp.Update updateOp ){
        super(tokenClass, ldapTemplate, mongoTemplate, userOpRetrieve);
        this.updateOpUpdate = updateOp;
        this.operations = operations;
    }

    @PutMapping("/api/user")
    public ResponseEntity<HttpStatus> updateUser(HttpServletRequest request, @RequestBody parsso.idman.models.users.User user) {
        return new ResponseEntity<>(updateOpUpdate.update(request.getUserPrincipal().getName(), request.getUserPrincipal().getName(), user));
    }


    @PutMapping("/api/users/ou/{ou}")
    public ResponseEntity<List<String>> addGroups(HttpServletRequest request, @RequestParam("file") MultipartFile file, @PathVariable("ou") String ou) throws IOException {
        List<String> notExist = updateOpUpdate.addGroupToUsers(request.getUserPrincipal().getName(), file, ou);
        if (ou.equals("none"))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (notExist == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        if (notExist.size() == 0)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(notExist, HttpStatus.PARTIAL_CONTENT);
    }


    @PutMapping("/api/users/import/massUpdate")
    public ResponseEntity<JSONObject> updateConflicts(HttpServletRequest request, @RequestBody List<User> users) {
        return new ResponseEntity<>(updateOpUpdate.mass(request.getUserPrincipal().getName(), users), HttpStatus.OK);
    }



    @PutMapping("/api/users/group/{groupId}")
    public ResponseEntity<HttpStatus> massUsersGroupUpdate(HttpServletRequest request,
                                                           @RequestBody JSONObject gu,
                                                           @PathVariable(name = "groupId") String groupId) {
        return new ResponseEntity<>(updateOpUpdate.groupOfUsers(request.getUserPrincipal().getName(), groupId, gu));
    }

    @PutMapping("/api/users/operation")
    public ResponseEntity<HttpStatus> operation(HttpServletRequest request,
                                                @RequestParam("id") String uid,
                                                @RequestParam("operation") String operation) {
        switch (operation) {
            case "unlock":
                return new ResponseEntity<>(operations.unlock(request.getUserPrincipal().getName(), uid));
            case "enable":
                return new ResponseEntity<>(operations.enable(request.getUserPrincipal().getName(), uid));
            case "disable":
                return new ResponseEntity<>(operations.disable(request.getUserPrincipal().getName(), uid));
            default:
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        }

    }

    @PutMapping("/api/users/u/{uId}")
    public ResponseEntity<JSONObject> rebindLdapUser(HttpServletRequest request, @PathVariable("uId") String uid, @RequestBody User user) {
        JSONObject objectResult = new JSONObject();
        String dn = "cn=DefaultPPolicy,ou=Policies," + BASE_DN;
        PWD pwd = this.ldapTemplate.lookup(dn, new PwdAttributeMapper());
        int pwdin = Integer.parseInt(pwd.getPwdInHistory().replaceAll("[^0-9]", ""));
        objectResult.put("pwdInHistory", pwdin);
        return new ResponseEntity<>(objectResult, updateOpUpdate.update(request.getUserPrincipal().getName(), uid, user));
    }

}
