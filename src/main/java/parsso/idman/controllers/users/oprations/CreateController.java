package parsso.idman.controllers.users.oprations;

import java.io.IOException;
import java.time.Duration;

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

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import parsso.idman.helpers.Extentsion;
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
  private Bucket bucket;

  @Autowired
  public CreateController(Token tokenClass, LdapTemplate ldapTemplate, MongoTemplate mongoTemplate,
      UserRepo.UsersOp.Retrieve usersOpRetrieve, UserRepo.UsersOp.Create create) {
    super(tokenClass, ldapTemplate, mongoTemplate, usersOpRetrieve);
    this.userOpCreate = create;
    Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
    this.bucket = Bucket4j.builder()
        .addLimit(limit)
        .build();
  }

  @PostMapping("/api/users")
  public ResponseEntity<Response> bindLdapUser(HttpServletRequest request, @RequestBody User user,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    if (bucket.tryConsume(1)) {
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
    return new ResponseEntity(new Response(null, Variables.MODEL_USER, HttpStatus.TOO_MANY_REQUESTS.value(), lang),
        HttpStatus.OK);

  }

  @PostMapping("/api/users/import")
  public ResponseEntity<Response> uploadFile(
      HttpServletRequest request, @RequestParam("file") MultipartFile file,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws IOException, NoSuchFieldException, IllegalAccessException {

        if(!Extentsion.check(file.getOriginalFilename(),Variables.EXT_USER_IMPORT))
          return new ResponseEntity(new Response(null,Variables.MODEL_USER,HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),lang),HttpStatus.OK);

    if (bucket.tryConsume(1)) {

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
    return new ResponseEntity(new Response(null, Variables.MODEL_USER, HttpStatus.TOO_MANY_REQUESTS.value(), lang),
        HttpStatus.OK);
  }

}
