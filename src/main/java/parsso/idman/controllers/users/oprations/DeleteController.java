package parsso.idman.controllers.users.oprations;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.minidev.json.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.communicate.Token;
import parsso.idman.models.response.Response;
import parsso.idman.repos.UserRepo;


@RestController

public class DeleteController extends UsersOps {

  private final UserRepo.UsersOp.Delete usersOpDelete;

  public DeleteController(Token tokenClass, LdapTemplate ldapTemplate, MongoTemplate mongoTemplate,
      UserRepo.UsersOp.Retrieve usersOpRetrieve, UserRepo.UsersOp.Delete usersOpDelete) {
    super(tokenClass, ldapTemplate, mongoTemplate, usersOpRetrieve);
    this.usersOpDelete = usersOpDelete;
  }

  @DeleteMapping("/api/users")
  public ResponseEntity<Response> unbindAllLdapUser(
        HttpServletRequest request, @RequestBody JSONObject jsonObject,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    List<String> names = usersOpDelete.remove(request.getUserPrincipal().getName(), jsonObject);
    if (names.size() == 0) {
      return new ResponseEntity<>(
        new Response(null, Variables.MODEL_USER, HttpStatus.NO_CONTENT.value(), lang),
        HttpStatus.OK);
    }
    return new ResponseEntity<>(new Response(
      names, Variables.MODEL_USER, HttpStatus.PARTIAL_CONTENT.value(), lang),
        HttpStatus.OK);
  }

}
