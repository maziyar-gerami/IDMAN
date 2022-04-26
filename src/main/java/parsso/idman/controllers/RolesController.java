package parsso.idman.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.helpers.Variables;
import parsso.idman.models.response.Response;
import parsso.idman.models.users.User.UserRole;
import parsso.idman.repos.RolesRepo;

@RestController
@RequestMapping(("/api/roles"))
public class RolesController {
  final RolesRepo rolesRepo;

  @Autowired
  public RolesController(RolesRepo rolesRepo) {
    this.rolesRepo = rolesRepo;
  }

  @GetMapping("/users")
  public ResponseEntity<Response> retrieveRoles(
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    List<UserRole> roles = rolesRepo.retrieve();
    if (roles != null) {
      return new ResponseEntity<>(new Response(
          roles, Variables.MODEL_ROLE, HttpStatus.OK.value(), lang),
          HttpStatus.OK);
    }
    return new ResponseEntity<>(new Response(
        null, Variables.MODEL_ROLE, HttpStatus.BAD_REQUEST.value(), lang),
          HttpStatus.BAD_REQUEST);
  }

  @PutMapping("/{role}")
  public ResponseEntity<Response> updateTicket(
      HttpServletRequest request, @RequestBody JSONObject users,
      @PathVariable("role") String role,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    HttpStatus code = rolesRepo.updateRole(request.getUserPrincipal().getName(), role, users);
    return new ResponseEntity<>(new Response(null, Variables.MODEL_ROLE, code.value(), lang), code);
  }
}
