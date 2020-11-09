package parsso.idman.Controllers;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.Service;
import parsso.idman.Models.User;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class ServiceController {

    @Qualifier("userRepoImpl")
    @Autowired
    private UserRepo userRepo;

    @Qualifier("serviceRepoImpl")
    @Autowired
    private ServiceRepo serviceRepo;

    @Value("${administrator.ou.id}")
    private String adminOu;

    @Value("${services.folder.path}")
    private String path;

    @GetMapping("/api/services/user")
    public ResponseEntity<List<Service>> ListUserServices(HttpServletRequest request) throws IOException, ParseException {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<List<Service>>(serviceRepo.listUserServices(userRepo.retrieveUser(principal.getName())), HttpStatus.OK);
    }

    @GetMapping("/api/services")
    public ResponseEntity<List<Service>> listServices() throws IOException, ParseException {
        return new ResponseEntity<List<Service>>(serviceRepo.listServices(), HttpStatus.OK);
    }

    @GetMapping("/api/services/{id}")
    public ResponseEntity<Service> retrieveService(@PathVariable("id") long serviceId) throws IOException, ParseException {
        return new ResponseEntity<Service>(serviceRepo.retrieveService(serviceId), HttpStatus.OK);
    }

    @DeleteMapping("/api/services/{id}")
    public ResponseEntity<String> deleteService(@PathVariable("id") long serviceId) throws IOException, ParseException {
        return new ResponseEntity<>(serviceRepo.deleteService(serviceId));
    }

    @DeleteMapping("/api/services")
    public ResponseEntity<String> deleteServices() {
        return new ResponseEntity<>(serviceRepo.deleteServices());
    }

    @PostMapping("/api/services")
    public ResponseEntity<String> createService(@RequestBody JSONObject jsonObject) {
        return new ResponseEntity<>(serviceRepo.createService(jsonObject));
    }

    @PutMapping("/api/service/{id}")
    public ResponseEntity<String> updateService(@PathVariable("id") long id, @RequestBody JSONObject jsonObject) throws IOException, ParseException {
        return new ResponseEntity<>(serviceRepo.updateService(id, jsonObject));
    }

    @GetMapping("/services")
    public String Services(HttpServletRequest request) {
        try {
            Principal principal = request.getUserPrincipal();
            User user = userRepo.retrieveUser(principal.getName());
            List<String> memberOf = user.getMemberOf();


            for (String group : memberOf) {
                if (group.equals(adminOu))
                    return "services";
            }

        } catch (Exception e) {
            return "403";
        }
        return "403";
    }

    @GetMapping("/createservice")
    public String CreateService(HttpServletRequest request) {
        try {
            Principal principal = request.getUserPrincipal();
            User user = userRepo.retrieveUser(principal.getName());

            List<String> memberOf = user.getMemberOf();


            for (String group : memberOf) {
                if (group.equals(adminOu))
                    return "createservice";
            }

        } catch (Exception e) {
            return "403";
        }
        return "403";
    }


}
