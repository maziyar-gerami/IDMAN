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
import parsso.idman.Models.ServiceType.MicroService;
import parsso.idman.Models.User;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.LinkedList;
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
    public ResponseEntity<List<MicroService>> ListUserServices(HttpServletRequest request) throws IOException, ParseException {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(serviceRepo.listUserServices(userRepo.retrieveUser(principal.getName())), HttpStatus.OK);
    }

    @GetMapping("/api/services")
    public ResponseEntity<List<Service>> listServices() throws IOException, ParseException {
        return new ResponseEntity<>(serviceRepo.listServices(), HttpStatus.OK);
    }

    @GetMapping("/api/services/{id}")
    public ResponseEntity<Service> retrieveService(@PathVariable("id") long serviceId) throws IOException, ParseException {
        return new ResponseEntity<>(serviceRepo.retrieveService(serviceId), HttpStatus.OK);
    }

    @DeleteMapping("/api/services/{id}")
    public ResponseEntity<String> deleteService(@PathVariable("id") long serviceId) throws IOException, ParseException {
        return new ResponseEntity<>(serviceRepo.deleteService(serviceId));
    }

    @DeleteMapping("/api/services")
    public ResponseEntity<LinkedList<String>> deleteServices(@RequestBody JSONObject jsonObject) throws IOException {
        LinkedList ls = serviceRepo.deleteServices(jsonObject);
        if (ls==null) return new ResponseEntity<>(ls,HttpStatus.OK);
        else return new ResponseEntity<>(ls,HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/services/{system}")
    public ResponseEntity<String> createService(@RequestBody JSONObject jsonObject, @PathVariable("system") String system) throws IOException {
        return new ResponseEntity<>(serviceRepo.createService(jsonObject, system));
    }

    @PutMapping("/api/service/{id}/{system}")
    public ResponseEntity<String> updateService(@PathVariable("id") long id,
                                                @RequestBody JSONObject jsonObject,@PathVariable("system") String system) throws IOException, ParseException {
        return new ResponseEntity<>(serviceRepo.updateService(id, jsonObject, system));
    }



    @GetMapping("/createservice")
    public String CreateService(HttpServletRequest request) {
        try {
            Principal principal = request.getUserPrincipal();
            User user = userRepo.retrieveUser(principal.getName());

            List<String> memberOf = user.getMemberOf();


            if (user.getUserId().equals("su"))
                return "createservice";

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
