package parsso.idman.controllers.users.oprations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.communicate.Token;
import parsso.idman.helpers.excelView.UsersExcelView;
import parsso.idman.models.response.Response;
import parsso.idman.models.users.User;
import parsso.idman.repos.SystemRefresh;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;

@RestController
public class RetrieveController extends UsersOps {
  SystemRefresh systemRefresh;
  private Bucket bucket;

  @Autowired
  public RetrieveController(
      Token tokenClass, LdapTemplate ldapTemplate, MongoTemplate mongoTemplate,
      UsersRetrieveRepo usersOpRetrieve, SystemRefresh systemRefresh) {
    super(tokenClass, ldapTemplate, mongoTemplate, usersOpRetrieve);
    this.systemRefresh = systemRefresh;
    Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
    this.bucket = Bucket.builder()
        .addLimit(limit)
        .build();
  }

  @GetMapping("/api/user")
  public ResponseEntity<Response> retrieveUser(HttpServletRequest request,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    User user = usersOpRetrieve.retrieveUsers(request.getUserPrincipal().getName());
    if (user == null) {
      return new ResponseEntity<>(new Response(
          null, Variables.MODEL_USER, HttpStatus.NOT_FOUND.value(), lang),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          new Response(user, Variables.MODEL_USER, HttpStatus.OK.value(), lang),
          HttpStatus.OK);
    }
  }

  @GetMapping("/api/users/u/{uid}")
  public ResponseEntity<Response> retrieveUser(
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang,
      @PathVariable("uid") String uid)
      throws NoSuchFieldException, IllegalAccessException {
    User user = usersOpRetrieve.retrieveUsers(uid);
    if (user == null) {
      return new ResponseEntity<>(new Response(
          null, Variables.MODEL_USER, HttpStatus.NOT_FOUND.value(), lang),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          new Response(user, Variables.MODEL_USER, HttpStatus.OK.value(), lang),
          HttpStatus.OK);
    }
  }

  @GetMapping("/api/users")
  public ResponseEntity<Response> retrieveUsersMain(
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang,
      @RequestParam(value = "page", defaultValue = "-1") String page,
      @RequestParam(value = "count", defaultValue = "-1") String count,
      @RequestParam(value = "sort", defaultValue = "") String sort,
      @RequestParam(value = "role", defaultValue = "") String role,
      @RequestParam(value = "userId", defaultValue = "") String userId,
      @RequestParam(value = "displayName", defaultValue = "") String displayName)
      throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(
        new Response(
            usersOpRetrieve.mainAttributes(Integer.valueOf(page), Integer.valueOf(count), sort, role, userId,
                displayName),
            Variables.MODEL_USER, HttpStatus.OK.value(), lang),
        HttpStatus.OK);
  }

  @GetMapping("/api/users/{page}/{n}")
  public ResponseEntity<Response> retrieveUsersMain(
      @PathVariable("page") int page, @PathVariable("n") int n,
      @RequestParam(name = "sortType", defaultValue = "") String sortType,
      @RequestParam(name = "groupFilter", defaultValue = "") String groupFilter,
      @RequestParam(name = "searchUid", defaultValue = "") String searchUid,
      @RequestParam(name = "userStatus", defaultValue = "") String userStatus,
      @RequestParam(name = "mobile", defaultValue = "") String mobile,
      @RequestParam(name = "searchDisplayName", defaultValue = "") String searchDisplayName,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    new Thread(() -> systemRefresh.refreshLockedUsers()).start();
    return new ResponseEntity<>(
        new Response(usersOpRetrieve.mainAttributes(page, n, sortType, groupFilter, searchUid,
            searchDisplayName, mobile, userStatus),
            Variables.MODEL_USER, HttpStatus.OK.value(), lang),
        HttpStatus.OK);
  }

  @GetMapping("/api/users/group/{groupId}")
  public ResponseEntity<Response> retrieveUsersMainWithGroupId(
      @PathVariable(name = "groupId") String groupId,
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "nRec", defaultValue = "90000") int recs,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(new Response(
        usersOpRetrieve.retrieveUsersMainWithGroupId(groupId, page, recs),
        Variables.MODEL_USER, HttpStatus.OK.value(), lang), HttpStatus.OK);
  }

  @GetMapping("/api/users/license/u/{uid}")
  public ResponseEntity<Response> retrieveUserLicense(@PathVariable("uid") String userId,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    User user = usersOpRetrieve.retrieveUsersWithLicensed(userId);
    if (user == null) {
      return new ResponseEntity<>(
          new Response(null, Variables.MODEL_USER, HttpStatus.NOT_FOUND.value(), lang),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          new Response(user, Variables.MODEL_USER, HttpStatus.OK.value(), lang),
          HttpStatus.OK);
    }
  }

  @GetMapping("/api/users/export")
  public ResponseEntity<ByteArrayResource> downloadExcel(HttpServletResponse response,
  @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang) throws NoSuchFieldException, IllegalAccessException, IOException {

    if (bucket.tryConsume(1))
      try {
          ByteArrayOutputStream stream = new ByteArrayOutputStream();
          Workbook workbook = new UsersExcelView(usersOpRetrieve).buildExcelDocument();; // creates the workbook
          workbook.write(stream);
          workbook.close();
          return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), HttpStatus.OK);
      } catch (Exception e) {
        
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }

      return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);

  }
}
