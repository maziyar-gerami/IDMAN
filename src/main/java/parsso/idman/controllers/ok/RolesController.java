package parsso.idman.controllers.ok;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.Users.UserRole;
import parsso.idman.Repos.RolesRepo;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(("/api/roles"))
public class RolesController {
	final RolesRepo rolesRepo;

	@Autowired
	public RolesController(RolesRepo rolesRepo) {
		this.rolesRepo = rolesRepo;
	}

	@GetMapping
	public ResponseEntity<List<UserRole>> retrieveRoles() {
		return new ResponseEntity<>(rolesRepo.retrieve(), rolesRepo.retrieve() != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/{role}")
	public ResponseEntity<HttpStatus> updateTicket(HttpServletRequest request, @RequestBody JSONObject users, @PathVariable("role") String role) throws IOException, ParseException {
		return new ResponseEntity<>(rolesRepo.updateRole(request.getUserPrincipal().getName(), role, users));
	}

}
