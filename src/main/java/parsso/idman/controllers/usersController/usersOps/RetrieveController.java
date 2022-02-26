package parsso.idman.controllers.usersController.usersOps;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import parsso.idman.controllers.usersController.UsersOps;
import parsso.idman.helpers.communicate.Token;
import parsso.idman.helpers.excelView.UsersExcelView;
import parsso.idman.models.users.User;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class RetrieveController extends UsersOps {

    public RetrieveController(Token tokenClass, LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, UserRepo.UsersOp.Retrieve usersOpRetrieve) {
        super(tokenClass, ldapTemplate, mongoTemplate, usersOpRetrieve);
    }

    @GetMapping("/api/user")
    public ResponseEntity<parsso.idman.models.users.User> retrieveUser(HttpServletRequest request) {

        return new ResponseEntity<>(usersOpRetrieve.retrieveUsers(request.getUserPrincipal().getName()), HttpStatus.OK);
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<UsersExtraInfo>> retrieveUsersMain() {
        return new ResponseEntity<>(usersOpRetrieve.mainAttributes(-1, -1), HttpStatus.OK);
    }

    @GetMapping("/api/users/{page}/{n}")
    public ResponseEntity<User.ListUsers> retrieveUsersMain(@PathVariable("page") int page, @PathVariable("n") int n,
                                                            @RequestParam(name = "sortType", defaultValue = "") String sortType,
                                                            @RequestParam(name = "groupFilter", defaultValue = "") String groupFilter,
                                                            @RequestParam(name = "searchUid", defaultValue = "") String searchUid,
                                                            @RequestParam(name = "userStatus", defaultValue = "") String userStatus,
                                                            @RequestParam(name = "mobile", defaultValue = "") String mobile,
                                                            @RequestParam(name = "searchDisplayName", defaultValue = "") String searchDisplayName) {
        return new ResponseEntity<>(usersOpRetrieve.mainAttributes(page, n, sortType, groupFilter, searchUid, searchDisplayName,mobile, userStatus), HttpStatus.OK);
    }

    @GetMapping("/api/users/u/{uid}")
    public ResponseEntity<User> retrieveUser(@PathVariable("uid") String userId) {
        User user = usersOpRetrieve.retrieveUsers(userId);
        if (user == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/api/users/group/{groupId}")
    public ResponseEntity<User.ListUsers> retrieveUsersMainWithGroupId(@PathVariable(name = "groupId") String groupId,
                                                                       @RequestParam(name = "page", defaultValue = "1") int page,
                                                                       @RequestParam(name = "nRec", defaultValue = "90000") int nRec) {
        User.ListUsers users = usersOpRetrieve.retrieveUsersMainWithGroupId(groupId, page, nRec);
        if (users == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/api/users/license/u/{uid}")
    public ResponseEntity<User> retrieveUserLicense(@PathVariable("uid") String userId) {
        User user = usersOpRetrieve.retrieveUsersWithLicensed(userId);
        if (user == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/api/users/export")
    public ModelAndView downloadExcel() {
        return new ModelAndView(new UsersExcelView(usersOpRetrieve), "listUsers", Object.class);
    }
}
