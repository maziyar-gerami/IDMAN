package parsso.idman.controllers.users.oprations;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import parsso.idman.helpers.Variables;
import parsso.idman.helpers.communicate.Token;
import parsso.idman.helpers.excelView.UsersExcelView;
import parsso.idman.models.response.Response;
import parsso.idman.models.users.User;
import parsso.idman.repos.UserRepo;


@RestController
public class RetrieveController extends UsersOps {

    @Autowired
    public RetrieveController(Token tokenClass, LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, UserRepo.UsersOp.Retrieve usersOpRetrieve) {
        super(tokenClass, ldapTemplate, mongoTemplate, usersOpRetrieve);
    }

    @GetMapping("/api/user")
    public ResponseEntity<Response> retrieveUser(HttpServletRequest request,
    @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang) throws NoSuchFieldException, IllegalAccessException {
        User user = usersOpRetrieve.retrieveUsers(request.getUserPrincipal().getName());
        if (user == null) return new ResponseEntity<>(new Response(null,Variables.MODEL_USER, HttpStatus.NOT_FOUND.value(),lang), HttpStatus.OK);
        else return new ResponseEntity<>(new Response(user,Variables.MODEL_USER,  HttpStatus.OK.value(),lang), HttpStatus.OK);
    }

    @GetMapping("/api/users")
    public ResponseEntity<Response> retrieveUsersMain(@RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang) throws NoSuchFieldException, IllegalAccessException {
        return new ResponseEntity<>(new Response(usersOpRetrieve.mainAttributes(-1, -1),Variables.MODEL_USER,HttpStatus.OK.value(), lang), HttpStatus.OK);
    }

    @GetMapping("/api/users/{page}/{n}")
    public ResponseEntity<Response> retrieveUsersMain(@PathVariable("page") int page, @PathVariable("n") int n,
                                                            @RequestParam(name = "sortType", defaultValue = "") String sortType,
                                                            @RequestParam(name = "groupFilter", defaultValue = "") String groupFilter,
                                                            @RequestParam(name = "searchUid", defaultValue = "") String searchUid,
                                                            @RequestParam(name = "userStatus", defaultValue = "") String userStatus,
                                                            @RequestParam(name = "mobile", defaultValue = "") String mobile,
                                                            @RequestParam(name = "searchDisplayName", defaultValue = "") String searchDisplayName,
                                                            @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang) throws NoSuchFieldException, IllegalAccessException {
        return new ResponseEntity<>(new Response(usersOpRetrieve.mainAttributes(page, n, sortType, groupFilter, searchUid, searchDisplayName,mobile, userStatus), Variables.MODEL_USER,HttpStatus.OK.value(),lang), HttpStatus.OK);
    }


    @GetMapping("/api/users/group/{groupId}")
    public ResponseEntity<Response> retrieveUsersMainWithGroupId(@PathVariable(name = "groupId") String groupId,
                                                                       @RequestParam(name = "page", defaultValue = "1") int page,
                                                                       @RequestParam(name = "nRec", defaultValue = "90000") int nRec,
                                                                       @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang) throws NoSuchFieldException, IllegalAccessException {
        return new ResponseEntity<>(new Response(usersOpRetrieve.retrieveUsersMainWithGroupId(groupId, page, nRec),Variables.MODEL_USER, HttpStatus.OK.value(),lang),HttpStatus.OK);
    }

    @GetMapping("/api/users/license/u/{uid}")
    public ResponseEntity<Response> retrieveUserLicense(@PathVariable("uid") String userId, @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang) throws NoSuchFieldException, IllegalAccessException {
        User user = usersOpRetrieve.retrieveUsersWithLicensed(userId);
        if (user == null) return new ResponseEntity<>(new Response (null, Variables.MODEL_USER, HttpStatus.NOT_FOUND.value(),lang), HttpStatus.OK);
            else return new ResponseEntity<>(new Response(user, Variables.MODEL_USER, HttpStatus.OK.value(),lang),HttpStatus.OK);
    }

    @GetMapping("/api/users/export")
    public ModelAndView downloadExcel() {
        return new ModelAndView(new UsersExcelView(usersOpRetrieve), "listUsers", Object.class);
    }
}
