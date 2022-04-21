package parsso.idman.controllers.users.oprations;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
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
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.communicate.Token;
import parsso.idman.helpers.user.ImportUsers;
import parsso.idman.models.response.Response;
import parsso.idman.models.users.User;
import parsso.idman.repos.UserRepo;

@RestController
public class CreateController extends UsersOps {

  private final int[] defaultSequence = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };

  private final UserRepo.UsersOp.Create userOpCreate;

  @Autowired
  public CreateController(Token tokenClass, LdapTemplate ldapTemplate, MongoTemplate mongoTemplate,
      UserRepo.UsersOp.Retrieve usersOpRetrieve, UserRepo.UsersOp.Create create) {
    super(tokenClass, ldapTemplate, mongoTemplate, usersOpRetrieve);
    this.userOpCreate = create;
  }

  @PostMapping("/api/users")
  public ResponseEntity<Response> bindLdapUser(HttpServletRequest request, @RequestBody User user,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    JSONObject jsonObject = userOpCreate.create(request.getUserPrincipal().getName(), user);

    if (jsonObject == null || jsonObject.size() == 0) {
      return new ResponseEntity<>(new Response(
        null, Variables.MODEL_USER, HttpStatus.CREATED.value(), lang),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(new Response(
        jsonObject, Variables.MODEL_USER, HttpStatus.FOUND.value(), lang),
          HttpStatus.OK);
    }
  }

  @PostMapping("/api/users/import")
  public ResponseEntity<Response> uploadFile(
        HttpServletRequest request, @RequestParam("file") MultipartFile file,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws IOException, NoSuchFieldException, IllegalAccessException {

    JSONObject jsonObject = new ImportUsers(userOpCreate).importFileUsers(
          request.getUserPrincipal().getName(),
        file, defaultSequence, true);
    if (Integer.parseInt(jsonObject.getAsString("nUnSuccessful")) == 0) {
      return new ResponseEntity<>(
          new Response(jsonObject, Variables.MODEL_USER, HttpStatus.CREATED.value(), lang),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(new Response(
        jsonObject, Variables.MODEL_USER, HttpStatus.FOUND.value(), lang),
          HttpStatus.OK);
    }
  }

}
