package parsso.idman.Controllers;


import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.Services.Service;
import parsso.idman.Models.Services.ServiceType.MicroService;
import parsso.idman.Models.Users.SimpleUser;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

@Controller
public class ServicesController {

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

    @Value("${metadata.file.path}")
    private String metadataPath;

    @GetMapping("/services")
    public String Services() {return "services"; }

    @GetMapping("/createservice")
    public String CreateService() {return "createservice";}

    @Autowired
    MongoTemplate mongoTemplate;


    @GetMapping("/api/services/user")


    public ResponseEntity<List<MicroService>> ListUserServices(HttpServletRequest request) throws IOException, ParseException {
        String currentUserId = request.getUserPrincipal().getName();
        SimpleUser simpleUser = mongoTemplate.findOne(new Query(Criteria.where("userId").is(currentUserId))
                ,SimpleUser.class,"IDMAN_SimpleUsers");

        if(simpleUser==null){
            simpleUser = new SimpleUser(userRepo.retrieveUsers(currentUserId));
            mongoTemplate.save(simpleUser, "IDMAN_SimpleUsers");
        }

        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(serviceRepo.listUserServices(userRepo.retrieveUsers(principal.getName())), HttpStatus.OK);
    }

    @GetMapping("/api/services/full")
    public ResponseEntity<List<Service>> listServicesFull() throws IOException, ParseException {
        return new ResponseEntity<>(serviceRepo.listServicesFull(), HttpStatus.OK);
    }

    @GetMapping("/api/services/main")
    public ResponseEntity<List<MicroService>> listServicesMain() throws IOException, ParseException {
        return new ResponseEntity<>(serviceRepo.listServicesMain(), HttpStatus.OK);
    }

    @GetMapping("/api/services/{id}")
    public ResponseEntity<Service> retrieveService(@PathVariable("id") long serviceId) throws IOException, ParseException {
        return new ResponseEntity<>(serviceRepo.retrieveService(serviceId), HttpStatus.OK);
    }

    @DeleteMapping("/api/services")
    public ResponseEntity<LinkedList<String>> deleteServices(HttpServletRequest request, @RequestBody JSONObject jsonObject) throws IOException {
        LinkedList ls = serviceRepo.deleteServices(request.getUserPrincipal().getName(), jsonObject);
        if (ls == null) return new ResponseEntity<>(ls, HttpStatus.OK);
        else return new ResponseEntity<>(ls, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/services/{system}")
    public ResponseEntity<String> createService(HttpServletRequest request,@RequestBody JSONObject jsonObject, @PathVariable("system") String system) throws IOException {
        return new ResponseEntity<>(serviceRepo.createService(request.getUserPrincipal().getName(), jsonObject, system));
    }

    @PutMapping("/api/service/{id}/{system}")
    public ResponseEntity<String> updateService(HttpServletRequest request,@PathVariable("id") long id,
                                                @RequestBody JSONObject jsonObject, @PathVariable("system") String system) throws IOException, ParseException {
        return new ResponseEntity<>(serviceRepo.updateService(request.getUserPrincipal().getName(),id, jsonObject, system));
    }

    /**
     * metaData for logged-in user
     *
     * @return the response entity
     */
    @PostMapping("/api/services/metadata")
    public ResponseEntity<String> uploadMetadata(@RequestParam("file") MultipartFile file) {
        String result = serviceRepo.uploadMetadata(file);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/api/services/position/{serviceId}")
    public ResponseEntity<HttpStatus> increasePosition(HttpServletRequest request,
                                                       @PathVariable("serviceId") String id, @RequestParam("value") int value) {
        if (value == 1)
            return new ResponseEntity<>(serviceRepo.increasePosition(id));
        else if (value == -1)
            return new ResponseEntity<>(serviceRepo.decreasePosition(id));
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    /**
     * metaData for logged-in user
     *
     * @return the response entity
     */
    @XmlElement
    @GetMapping(value = "/api/public/metadata/{file}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public @ResponseBody
    Object getFile(@PathVariable("file") String file) throws IOException {

        FileInputStream in = new FileInputStream(new File(metadataPath + file));

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "xml"));

        return new HttpEntity<>(IOUtils.toByteArray(in), header);

    }
}
