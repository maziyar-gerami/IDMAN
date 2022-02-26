package parsso.idman.controllers.usersController.usersOps;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.controllers.usersController.UsersOps;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.communicate.Token;
import parsso.idman.helpers.user.ImportUsers;
import parsso.idman.models.response.Response;
import parsso.idman.models.users.User;
import parsso.idman.repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class CreateController extends UsersOps {

    private final int[] defaultSequence = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

    private final UserRepo.UsersOp.Create userOpCreate;
    private final UserRepo.UsersOp.Retrieve usersOpRetrieve;

    @Autowired
    public CreateController(Token tokenClass, LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, UserRepo.UsersOp.Retrieve usersOpRetrieve, UserRepo.UsersOp.Create UserOpCreate, UserRepo.UsersOp.Create userOpCreate) {
        super(tokenClass, ldapTemplate, mongoTemplate,usersOpRetrieve);
        this.userOpCreate = userOpCreate;
        this.usersOpRetrieve = usersOpRetrieve;
    }

    @PostMapping("/api/users")
    public ResponseEntity<Response> bindLdapUser(HttpServletRequest request, @RequestBody User user,@RequestParam(value="lang",defaultValue = "fa") String lang) throws NoSuchFieldException, IllegalAccessException {
        JSONObject jsonObject = userOpCreate.create(request.getUserPrincipal().getName(), user);

        if (jsonObject == null || jsonObject.size() == 0)
            return new ResponseEntity<>(new Response(null, Variables.MODEL_USER,201,lang), HttpStatus.OK);
        else return new ResponseEntity<>(new Response(jsonObject, Variables.MODEL_USER,302,lang) , HttpStatus.OK);
    }

    @PostMapping("/api/users/import")
    public ResponseEntity<JSONObject> uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {

        JSONObject jsonObject = new ImportUsers(userOpCreate).importFileUsers(request.getUserPrincipal().getName(), file, defaultSequence, true);
        if (Integer.parseInt(jsonObject.getAsString("nUnSuccessful")) == 0)
            return new ResponseEntity<>(jsonObject, HttpStatus.OK);
        else return new ResponseEntity<>(jsonObject, HttpStatus.FOUND);
    }


}
