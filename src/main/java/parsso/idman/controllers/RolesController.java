package parsso.idman.controllers;


import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.Users.UserRole;
import parsso.idman.repos.RolesRepo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(("/api/roles"))
public class RolesController {
    final RolesRepo rolesRepo;

    @Autowired
    public RolesController(RolesRepo rolesRepo) {
        this.rolesRepo = rolesRepo;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserRole>> retrieveRoles() {
        return new ResponseEntity<>(rolesRepo.retrieve(), rolesRepo.retrieve() != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{role}")
    public ResponseEntity<HttpStatus> updateTicket(HttpServletRequest request, @RequestBody JSONObject users, @PathVariable("role") String role) {
        return new ResponseEntity<>(rolesRepo.updateRole("SU", role, users));
    }

}
