package parsso.idman.Controllers;


import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.User;
import parsso.idman.Models.UserRole;
import parsso.idman.Repos.RolesRepo;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;



@RestController
public class RolesController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RolesRepo rolesRepo;


    //*************************************** Pages ***************************************

    @GetMapping("/roles")
    public String Roles(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepo.retrieveUsers(principal.getName());
        if (user.getUsersExtraInfo().getRole().equals("SUPERADMIN"))
                return "roles";

        return null;
    }

    //*************************************** APIs ***************************************


    @GetMapping("/api/roles")
    public ResponseEntity<List<UserRole>> retrieveRoles() {
        return new ResponseEntity<>(rolesRepo.retrieve(),rolesRepo.retrieve() != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/api/roles/{role}")
    public ResponseEntity<HttpStatus> updateTicket(@RequestBody JSONObject users, @PathVariable("role") String role) {
        return new ResponseEntity<>(rolesRepo.updateRole(role, users));
    }

}
