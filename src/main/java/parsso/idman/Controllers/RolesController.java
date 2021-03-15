package parsso.idman.Controllers;


import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.UserRole;
import parsso.idman.Repos.RolesRepo;

import java.util.List;

@RestController
public class RolesController {

    @Autowired
    RolesRepo rolesRepo;

    @GetMapping("/api/roles")
    public ResponseEntity<List<UserRole>> retrieveRoles() {
        return new ResponseEntity<>(rolesRepo.retrieve(),rolesRepo.retrieve() != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/api/roles/{role}")
    public ResponseEntity<HttpStatus> updateTicket(@RequestBody JSONObject users, @PathVariable("role") String role) {
        return new ResponseEntity<>(rolesRepo.updateRole(role, users));
    }

}
