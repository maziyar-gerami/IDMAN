package parsso.idman.controllers.users.oprations;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import parsso.idman.helpers.Variables;
import parsso.idman.helpers.communicate.Token;
import parsso.idman.helpers.user.Operations;
import parsso.idman.models.response.Response;
import parsso.idman.models.users.User;
import parsso.idman.repos.UserRepo;



@RestController
public class UpdateController extends UsersOps {

  private final UserRepo.UsersOp.Update update;
  private final Operations operations;

  @Autowired
  public UpdateController(Operations operations, Token tokenClass, LdapTemplate ldapTemplate,
      MongoTemplate mongoTemplate, UserRepo.UsersOp.Retrieve userOpRetrieve,
      UserRepo.UsersOp.Update updateOp) {
    super(tokenClass, ldapTemplate, mongoTemplate, userOpRetrieve);
    this.update = updateOp;
    this.operations = operations;
  }

  @PutMapping("/api/user")
  public ResponseEntity<Response> updateUser(HttpServletRequest request,
      @RequestBody parsso.idman.models.users.User user,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(new Response(null, Variables.MODEL_USER, update
        .update(request.getUserPrincipal().getName(), request.getUserPrincipal().getName(), user)
        .value(), lang), HttpStatus.OK);
  }

  @PutMapping("/api/users")
  public ResponseEntity<Response> update(
        HttpServletRequest request, @RequestBody parsso.idman.models.users.User user,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(new Response(null, Variables.MODEL_USER,
        update.update(request.getUserPrincipal().getName(), user.get_id().toString(), user).value(),
        lang), HttpStatus.OK);
  }

  @PutMapping("/api/users/ou/{ou}")
  public ResponseEntity<Response> addGroups(
        HttpServletRequest request, @RequestParam("file") MultipartFile file,
      @PathVariable("ou") String ou,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws IOException, NoSuchFieldException, IllegalAccessException {
    List<String> notExist = update.addGroupToUsers(request.getUserPrincipal().getName(), file, ou);
    if (notExist == null) {
      return new ResponseEntity<>(new Response(
            null, Variables.MODEL_USER, HttpStatus.FORBIDDEN.value(), lang),
          HttpStatus.OK);
    } else if (notExist.size() == 0) {
      return new ResponseEntity<>(new Response(
            null, Variables.MODEL_USER, HttpStatus.OK.value(), lang),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          new Response(notExist, Variables.MODEL_USER, 
            HttpStatus.MULTI_STATUS.value(), lang), HttpStatus.OK);
    }
  }

  @PutMapping("/api/users/import/massUpdate")
  public ResponseEntity<Response> updateConflicts(
        HttpServletRequest request, @RequestBody List<User> users,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(
      new Response(update.mass(request.getUserPrincipal().getName(), users),
        Variables.MODEL_USER, HttpStatus.OK.value(), lang), HttpStatus.OK);
  }

  @PutMapping("/api/users/group/{groupId}")
  public ResponseEntity<Response> massUsersGroupUpdate(HttpServletRequest request,
      @RequestBody JSONObject gu,
      @PathVariable(name = "groupId") String groupId,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    JSONObject jsonObject = update.groupOfUsers(request.getUserPrincipal().getName(), groupId, gu);
    if (jsonObject.equals(null)) {
      return new ResponseEntity<>(
        new Response(null, Variables.MODEL_USER, HttpStatus.OK.value(), lang),
          HttpStatus.OK);
    }

    return new ResponseEntity<>(
        new Response(jsonObject, 
          Variables.MODEL_USER, HttpStatus.MULTI_STATUS.value(), lang), HttpStatus.OK);
  }

  @PutMapping("/api/users/status")
  public ResponseEntity<Response> operation(HttpServletRequest request,
      @RequestParam("id") String uid,
      @RequestParam("status") String operation,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    switch (operation) {
      case "unlock":
        return new ResponseEntity<>(
            new Response(null, Variables.MODEL_USER,
                operations.unlock(request.getUserPrincipal().getName(), uid).value(), lang),
            HttpStatus.OK);
      case "enable":
        return new ResponseEntity<>(
            new Response(null, Variables.MODEL_USER,
                operations.enable(request.getUserPrincipal().getName(), uid).value(), lang),
            HttpStatus.OK);
      case "disable":
        return new ResponseEntity<>(
            new Response(null, Variables.MODEL_USER,
                operations.disable(request.getUserPrincipal().getName(), uid).value(), lang),
            HttpStatus.OK);
      default:
        return new ResponseEntity<>(
            new Response(
              null, Variables.MODEL_USER, HttpStatus.FORBIDDEN.value(), lang), HttpStatus.OK);

    }
  }

}
