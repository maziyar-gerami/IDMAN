package parsso.idman.controllers;


import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import parsso.idman.Models.Users.UserRole;
import parsso.idman.Repos.RolesRepo;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class RolesController {
	final RolesRepo rolesRepo;

	@Autowired
	public RolesController(RolesRepo rolesRepo, UserRepo userRepo) {
		this.rolesRepo = rolesRepo;
	}

	//************************************* Pages ****************************************

	@SuppressWarnings("SameReturnValue")
	@GetMapping("/roles")
	public String getPageRoles() {
		return "roles";
	}

	//************************************* APIs ****************************************

	@GetMapping("/api/roles")
	public ResponseEntity<List<UserRole>> retrieveRoles() {
		return new ResponseEntity<>(rolesRepo.retrieve(), rolesRepo.retrieve() != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/api/roles/{role}")
	public ResponseEntity<HttpStatus> updateTicket(HttpServletRequest request, @RequestBody JSONObject users, @PathVariable("role") String role) {
		return new ResponseEntity<>(rolesRepo.updateRole(request.getUserPrincipal().getName(), role, users));
	}

}