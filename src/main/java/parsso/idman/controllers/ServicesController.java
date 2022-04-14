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
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.service.Position;
import parsso.idman.models.response.Response;
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
    public ResponseEntity<Response> ListUserServices(HttpServletRequest request,@RequestParam(value = "lang") String lang) throws NoSuchFieldException, IllegalAccessException {
        return new ResponseEntity<>(new Response(retrieveService.listUserServices(userOpRetrieve.retrieveUsers(request.getUserPrincipal().getName())),Variables.MODEL_SERVICE, HttpStatus.OK.value(),lang),HttpStatus.OK);
    }

    @GetMapping("/api/services/main")
    public ResponseEntity<Response> listServicesMain(@RequestParam(value = "lang") String lang) throws NoSuchFieldException, IllegalAccessException {
        return new ResponseEntity<>(new Response(retrieveService.listServicesMain(), Variables.MODEL_SERVICE,HttpStatus.OK.value(),lang),HttpStatus.OK);
    }

    @GetMapping("/api/services/{id}")
    public ResponseEntity<Response> retrieveService(@PathVariable("id") long serviceId,@RequestParam(value = "lang") String lang) throws NoSuchFieldException, IllegalAccessException {
        return new ResponseEntity<>(new Response(retrieveService.retrieveService(serviceId), Variables.MODEL_SERVICE, HttpStatus.OK.value(),lang),HttpStatus.OK);
    }

    @DeleteMapping("/api/services")
    public ResponseEntity<Response> deleteServices(HttpServletRequest request, @RequestBody JSONObject jsonObject,@RequestParam(value = "lang") String lang) throws NoSuchFieldException, IllegalAccessException {
        LinkedList<String> ls = deleteService.delete(request.getUserPrincipal().getName(), jsonObject);
        HttpStatus httpStatus = (ls == null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(new Response(ls, Variables.MODEL_SERVICE, httpStatus.value(),lang), HttpStatus.OK);
    }

    @PostMapping("/api/services/{system}")
    public ResponseEntity<Response> createService(HttpServletRequest request, @RequestBody JSONObject jsonObject, @PathVariable("system") String system,@RequestParam(value = "lang") String lang) throws IOException, ParseException, NoSuchFieldException, IllegalAccessException {
        long id = createService.createService(request.getUserPrincipal().getName(), jsonObject, system);
        HttpStatus httpStatus= (id==0?HttpStatus.FORBIDDEN:HttpStatus.OK);
        return new ResponseEntity<>(new Response(null, Variables.MODEL_SERVICE, httpStatus.value(),lang), HttpStatus.OK);
    }

    @PutMapping("/api/service/{id}/{system}")
    public ResponseEntity<Response> updateService(HttpServletRequest request, @PathVariable("id") long id,
                                                @RequestBody JSONObject jsonObject, @PathVariable("system") String system,@RequestParam(value = "lang") String lang) throws NoSuchFieldException, IllegalAccessException {
        return new ResponseEntity<>(new Response(null,Variables.MODEL_SERVICE, updateService.updateService(request.getUserPrincipal().getName(), id, jsonObject, system).value(),lang), HttpStatus.OK);
    }

    @GetMapping("/api/serviceCheck/{id}")
    public ResponseEntity<Response> serviceAccess(@PathVariable("id") long id,@RequestParam(value = "lang") String lang) {
        return new ResponseEntity<>(new ServiceAccess(mongoTemplate).serviceAccess(id) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/services/metadata")
    public ResponseEntity<Response> uploadMetadata(@RequestParam("file") MultipartFile file,@RequestParam(value = "lang") String lang) throws NoSuchFieldException, IllegalAccessException {
        String result = new Metadata(storageService,BASE_URL).upload(file);
        HttpStatus httpStatus = (result==null)? HttpStatus.OK:HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(new Response(result, Variables.MODEL_SERVICE, httpStatus.value(),lang),HttpStatus.OK);
    }

    @PostMapping("/api/services/icon")
    public ResponseEntity<Response> uploadIcon(@RequestParam("file") MultipartFile file,@RequestParam(value = "lang") String lang) throws NoSuchFieldException, IllegalAccessException {
        String result = new ServiceIcon(storageService,BASE_URL).upload(file);
        HttpStatus httpStatus = (result==null)? HttpStatus.OK:HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(new Response(result, Variables.MODEL_SERVICE, httpStatus.value(),lang),HttpStatus.OK);
        
    }

    @GetMapping("/api/services/position/{serviceId}")
    public ResponseEntity<Response> increasePosition(@PathVariable("serviceId") String id, @RequestParam("value") int value,@RequestParam(value = "lang") String lang) {
        if (value == 1)
            return new ResponseEntity<>(new Position(mongoTemplate).increase(id));
        else if (value == -1)
            return new ResponseEntity<>(new Position(mongoTemplate).decrease(id));
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @XmlElement
    @GetMapping(value = "/api/public/metadata/{file}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    Object getMetaDataFile(@PathVariable("file") String file,@RequestParam(value = "lang") String lang) throws IOException {

        String metadataPath = new Settings().retrieve("metadata.file.path").getValue();

        FileInputStream in = new FileInputStream(metadataPath + file);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "xml"));

        return new HttpEntity<>(IOUtils.toByteArray(in), header);

    }

    @GetMapping(value = "/api/public/icon/{file}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<Response> getIconFile(HttpServletResponse response, @PathVariable("file") String file,@RequestParam(value = "lang") String lang) throws NoSuchFieldException, IllegalAccessException {
        return new ResponseEntity<>(new Response(new ServiceIcon(storageService,BASE_URL).show(response, file),Variables.MODEL_SERVICE, HttpStatus.OK.value(),lang),HttpStatus.OK);

    }
}
