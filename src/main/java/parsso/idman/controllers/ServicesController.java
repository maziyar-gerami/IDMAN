package parsso.idman.controllers;


import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.service.Position;
import parsso.idman.models.services.Service;
import parsso.idman.models.services.serviceType.MicroService;
import parsso.idman.repoImpls.services.DeleteService;
import parsso.idman.repoImpls.services.RetrieveService;
import parsso.idman.repoImpls.services.create.CreateService;
import parsso.idman.repoImpls.services.create.sublcasses.Metadata;
import parsso.idman.repoImpls.services.create.sublcasses.ServiceAccess;
import parsso.idman.repoImpls.services.create.sublcasses.ServiceIcon;
import parsso.idman.repoImpls.services.update.UpdateService;
import parsso.idman.repos.FilesStorageService;
import parsso.idman.repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.XmlElement;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@RestController
public class ServicesController {
    final UserRepo.UsersOp.Retrieve userOpRetrieve;
    final MongoTemplate mongoTemplate;
    final FilesStorageService storageService;
    final UniformLogger uniformLogger;
    final CreateService createService;
    final DeleteService deleteService;
    final UpdateService updateService;
    final RetrieveService retrieveService;
    @Autowired
    public ServicesController(UserRepo.UsersOp.Retrieve userOpRetrieve,
                              MongoTemplate mongoTemplate, FilesStorageService storageService, UniformLogger uniformLogger, CreateService createService, DeleteService deleteService, UpdateService updateService, RetrieveService retrieveService) {
        this.userOpRetrieve = userOpRetrieve;
        this.mongoTemplate = mongoTemplate;
        this.storageService = storageService;
        this.uniformLogger = uniformLogger;
        this.createService = createService;
        this.deleteService = deleteService;
        this.updateService = updateService;
        this.retrieveService = retrieveService;
    }

    @Value("${base.url}")
    private String BASE_URL;




    @GetMapping("/api/services/user")
    public ResponseEntity<List<MicroService>> ListUserServices(HttpServletRequest request) {
        return new ResponseEntity<>(retrieveService.listUserServices(userOpRetrieve.retrieveUsers(request.getUserPrincipal().getName())), HttpStatus.OK);
    }

    @GetMapping("/api/services/main")
    public ResponseEntity<List<MicroService>> listServicesMain() {
        return new ResponseEntity<>(retrieveService.listServicesMain(), HttpStatus.OK);
    }

    @GetMapping("/api/services/{id}")
    public ResponseEntity<Service> retrieveService(@PathVariable("id") long serviceId) {
        return new ResponseEntity<>(retrieveService.retrieveService(serviceId), HttpStatus.OK);
    }

    @DeleteMapping("/api/services")
    public ResponseEntity<LinkedList<String>> deleteServices(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        LinkedList<String> ls = deleteService.delete(request.getUserPrincipal().getName(), jsonObject);
        HttpStatus httpStatus = (ls == null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(ls, httpStatus);
    }

    @PostMapping("/api/services/{system}")
    public ResponseEntity<HttpStatus> createService(HttpServletRequest request, @RequestBody JSONObject jsonObject, @PathVariable("system") String system) throws IOException, ParseException {
        long id = createService.createService(request.getUserPrincipal().getName(), jsonObject, system);
        if (id == 0)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PutMapping("/api/service/{id}/{system}")
    public ResponseEntity<String> updateService(HttpServletRequest request, @PathVariable("id") long id,
                                                @RequestBody JSONObject jsonObject, @PathVariable("system") String system) {
        return new ResponseEntity<>(updateService.updateService(request.getUserPrincipal().getName(), id, jsonObject, system));
    }

    @GetMapping("/api/serviceCheck/{id}")
    public ResponseEntity<HttpStatus> serviceAccess(@PathVariable("id") long id) {
        return new ResponseEntity<>(new ServiceAccess(mongoTemplate).serviceAccess(id) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/services/metadata")
    public ResponseEntity<String> uploadMetadata(@RequestParam("file") MultipartFile file) {
        String result = new Metadata(storageService,BASE_URL).upload(file);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/services/icon")
    public ResponseEntity<String> uploadIcon(@RequestParam("file") MultipartFile file) {
        String result = new ServiceIcon(storageService,BASE_URL).upload(file);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api/services/position/{serviceId}")
    public ResponseEntity<HttpStatus> increasePosition(@PathVariable("serviceId") String id, @RequestParam("value") int value) {
        if (value == 1)
            return new ResponseEntity<>(new Position(mongoTemplate).increase(id));
        else if (value == -1)
            return new ResponseEntity<>(new Position(mongoTemplate).decrease(id));
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @XmlElement
    @GetMapping(value = "/api/public/metadata/{file}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public @ResponseBody
    Object getMetaDataFile(@PathVariable("file") String file) throws IOException {

        String metadataPath = new Settings().retrieve("metadata.file.path").getValue();

        FileInputStream in = new FileInputStream(metadataPath + file);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "xml"));

        return new HttpEntity<>(IOUtils.toByteArray(in), header);

    }

    @GetMapping(value = "/api/public/icon/{file}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<String> getIconFile(HttpServletResponse response, @PathVariable("file") String file) {
        return new ResponseEntity<>(new ServiceIcon(storageService,BASE_URL).show(response, file), HttpStatus.OK);

    }
}
