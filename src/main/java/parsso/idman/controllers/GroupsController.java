package parsso.idman.controllers;

import io.jsonwebtoken.io.IOException;
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
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.excelView.GroupsExcelView;
import parsso.idman.helpers.group.ImportGroups;
import parsso.idman.impls.groups.CreateGroup;
import parsso.idman.impls.groups.DeleteGroup;
import parsso.idman.impls.groups.RetrieveGroup;
import parsso.idman.impls.groups.UpdateGroup;
import parsso.idman.models.groups.Group;
import parsso.idman.models.response.Response;
import parsso.idman.models.users.User;
import parsso.idman.repos.GroupRepo;
import parsso.idman.repos.UserRepo;


@RequestMapping("/api/groups")
@RestController
public class GroupsController {
  private final UserRepo.UsersOp.Retrieve retrieveUsers;
  private final UserRepo.PasswordOp passwordOp;
  private final RetrieveGroup retrieveGroup;
  private final GroupRepo.Update updateGroup;
  private final GroupRepo.Delete deleteGroup;
  private final GroupRepo.Create createGroup;
  private final MongoTemplate mongoTemplate;

  @Autowired
  public GroupsController(UserRepo.UsersOp.Retrieve retrieveUsers, UserRepo.PasswordOp passwordOp,
      RetrieveGroup retrieveGroup, UpdateGroup updateGroup, DeleteGroup deleteGroup,
      CreateGroup createGroup, MongoTemplate mongoTemplate) {
    this.retrieveUsers = retrieveUsers;
    this.passwordOp = passwordOp;
    this.retrieveGroup = retrieveGroup;
    this.updateGroup = updateGroup;
    this.deleteGroup = deleteGroup;
    this.createGroup = createGroup;
    this.mongoTemplate = mongoTemplate;
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

    List<Group> groups = retrieveGroup.retrieve(user);
    groups.removeAll(Collections.singleton(null));
    return new ResponseEntity<>(new Response(groups, Variables.MODEL_GROUP,
        HttpStatus.OK.value(), lang), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Response> bindLdapGroup(HttpServletRequest request,
      @RequestParam(value = "lang", defaultValue = "fa") String lang, @RequestBody Group group)
      throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(new Response(null, Variables.MODEL_GROUP,
        createGroup.create(request.getUserPrincipal().getName(), group).value(), lang),
        HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<?> retrieveGroups(@RequestParam(value = "id", defaultValue = "") String id,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    if (!id.equals("")) {
      return new ResponseEntity<>(
          new Response(retrieveGroup.retrieve(false, id), Variables.MODEL_GROUP,
              HttpStatus.OK.value(), lang),
          HttpStatus.OK);
    } else {
      List<Group> groups = retrieveGroup.retrieve();
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
        deleteGroup.remove(request.getUserPrincipal().getName(), jsonObject).value(), lang),
        HttpStatus.OK);
  }

  @PutMapping("/{ouID}")
  public ResponseEntity<Response> rebind(HttpServletRequest request,
      @RequestBody Group ou, @RequestParam(value = "lang", defaultValue = "fa") String lang,@PathVariable(value = "ouID") String ouID)
      throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(new Response(null, Variables.MODEL_GROUP,
        updateGroup.update(request.getUserPrincipal().getName(), ouID, ou).value(), lang),
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
    return new ModelAndView(new GroupsExcelView(
          retrieveGroup, mongoTemplate), "listGroups", Object.class);
  }

  @PostMapping("/import")
  public ResponseEntity<Response> uploadFile(
        HttpServletRequest request, @RequestParam("file") MultipartFile file,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws IOException, NoSuchFieldException, IllegalAccessException, java.io.IOException,
      ParseException {

    org.json.simple.JSONObject jsonObject = new ImportGroups(createGroup)
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
}
