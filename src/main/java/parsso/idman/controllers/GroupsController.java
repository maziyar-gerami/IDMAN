package parsso.idman.controllers;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import io.jsonwebtoken.io.IOException;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import parsso.idman.helpers.Extentsion;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.excelView.GroupsExcelView;
import parsso.idman.helpers.group.ImportGroups;
import parsso.idman.models.groups.Group;
import parsso.idman.models.response.Response;
import parsso.idman.models.users.User;
import parsso.idman.repos.GroupRepo;
import parsso.idman.repos.users.PasswordOpsRepo;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;


@RequestMapping("/api/groups")
@RestController
public class GroupsController {
  private final UsersRetrieveRepo retrieveUsers;
  private final PasswordOpsRepo passwordOp;
  private final GroupRepo groupRepo;
  private final MongoTemplate mongoTemplate;
  private Bucket bucket;

  @Autowired
  public GroupsController(UsersRetrieveRepo retrieveUsers, PasswordOpsRepo passwordOp, GroupRepo groupRepo, MongoTemplate mongoTemplate) {
    this.retrieveUsers = retrieveUsers;
    this.passwordOp = passwordOp;
    this.groupRepo = groupRepo;
    this.mongoTemplate = mongoTemplate;
    Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
    this.bucket = Bucket4j.builder()
            .addLimit(limit)
            .build();
  }

  @GetMapping("/user")
  public ResponseEntity<Response> retrieveUserOU(HttpServletRequest request,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    User user = retrieveUsers.retrieveUsers(request.getUserPrincipal().getName());
    if (user == null) {
      return new ResponseEntity<>(new Response(null, Variables.MODEL_GROUP,
          HttpStatus.FORBIDDEN.value(), lang), HttpStatus.OK);
    }

    List<Group> groups = groupRepo.retrieve(user);
    groups.removeAll(Collections.singleton(null));
    return new ResponseEntity<>(new Response(groups, Variables.MODEL_GROUP,
        HttpStatus.OK.value(), lang), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Response> bindLdapGroup(HttpServletRequest request,
      @RequestParam(value = "lang", defaultValue = "fa") String lang, @RequestBody Group group)
      throws NoSuchFieldException, IllegalAccessException {
        if (bucket.tryConsume(1))
    return new ResponseEntity<>(new Response(null, Variables.MODEL_GROUP,
        groupRepo.create(request.getUserPrincipal().getName(), group).value(), lang),
        HttpStatus.OK);
        return new ResponseEntity(new Response(null, Variables.MODEL_GROUP,HttpStatus.TOO_MANY_REQUESTS.value(),lang),HttpStatus.OK);
        
  }

  @GetMapping
  public ResponseEntity<?> retrieveGroups(@RequestParam(value = "id", defaultValue = "") String id,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    if (!id.equals("")) {
      return new ResponseEntity<>(
          new Response(groupRepo.retrieve(false, id), Variables.MODEL_GROUP,
              HttpStatus.OK.value(), lang),
          HttpStatus.OK);
    } else {
      List<Group> groups = groupRepo.retrieve();
      groups.removeIf(t -> t.getName() == null);
      return new ResponseEntity<>(new Response(groups, Variables.MODEL_GROUP,
          HttpStatus.OK.value(), lang), HttpStatus.OK);
    }
  }

  @DeleteMapping
  public ResponseEntity<Response> unbindAllLdapOU(
      HttpServletRequest request, @RequestBody JSONObject jsonObject,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(new Response(null, Variables.MODEL_GROUP,
        groupRepo.remove(request.getUserPrincipal().getName(), jsonObject).value(), lang),
        HttpStatus.OK);
  }

  @PutMapping("/{ouID}")
  public ResponseEntity<Response> rebind(HttpServletRequest request,
      @RequestBody Group ou, @RequestParam(value = "lang", defaultValue = "fa") String lang,@PathVariable(value = "ouID") String ouID)
      throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(new Response(null, Variables.MODEL_GROUP,
        groupRepo.update(request.getUserPrincipal().getName(), ouID, ou).value(), lang),
        HttpStatus.OK);
  }

  @PutMapping("/password/expire")
  public ResponseEntity<Response> expireUsersGroupPassword(HttpServletRequest request,
      @RequestBody JSONObject jsonObject,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {

    return new ResponseEntity<>(
        new Response(passwordOp.expireGroup(request.getUserPrincipal().getName(), jsonObject),
            Variables.MODEL_GROUP, HttpStatus.OK.value(), lang),
        HttpStatus.OK);

  }

  @GetMapping("/export")
  public ModelAndView downloadExcel() {
    if (bucket.tryConsume(1))
    return new ModelAndView(new GroupsExcelView(
      groupRepo, mongoTemplate), "listGroups", Object.class);
          return null;
  }

  @PostMapping("/import")
  public ResponseEntity<Response> uploadFile(
        HttpServletRequest request, @RequestParam("file") MultipartFile file,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws IOException, NoSuchFieldException, IllegalAccessException, java.io.IOException,
      ParseException {
        if(!Extentsion.check(file.getOriginalFilename(),Variables.EXT_USER_IMPORT))
          return new ResponseEntity(new Response(null,Variables.MODEL_GROUP,HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),lang),HttpStatus.OK);

       if( bucket.tryConsume(1)){

    org.json.simple.JSONObject jsonObject = new ImportGroups(groupRepo)
        .importFileGroups(request.getUserPrincipal().getName(), file, true);
    if (Integer.parseInt(jsonObject.get("nUnSuccessful").toString()) == 0) {
      return new ResponseEntity<>(
          new Response(jsonObject, Variables.MODEL_USER, HttpStatus.CREATED.value(),
              lang),
          HttpStatus.OK);
    } else if (Integer.parseInt(jsonObject.get("nUnSuccessful").toString()) > 0
        && Integer.parseInt(jsonObject.get("nSuccessful").toString()) > 0) {
      return new ResponseEntity<>(
          new Response(jsonObject, Variables.MODEL_USER, HttpStatus.MULTI_STATUS.value(),
              lang),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          new Response(jsonObject, Variables.MODEL_USER, HttpStatus.BAD_REQUEST.value(),
              lang),
          HttpStatus.OK);
    }
  }
  return new ResponseEntity(new Response(null, Variables.MODEL_GROUP,HttpStatus.TOO_MANY_REQUESTS.value(),lang),HttpStatus.OK);
}
}
