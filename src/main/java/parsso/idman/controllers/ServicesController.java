package parsso.idman.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.XmlElement;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.service.Metadata;
import parsso.idman.helpers.service.Position;
import parsso.idman.helpers.service.ServiceAccess;
import parsso.idman.helpers.service.ServiceIcon;
import parsso.idman.impls.logs.subclass.ServiceAudit;
import parsso.idman.models.response.Response;
import parsso.idman.repos.FilesStorageService;
import parsso.idman.repos.ServiceRepo;
import parsso.idman.repos.UserRepo;

@RestController
public class ServicesController {
  final UserRepo.UsersOp.Retrieve userOpRetrieve;
  final MongoTemplate mongoTemplate;
  final FilesStorageService storageService;
  final UniformLogger uniformLogger;
  final ServiceRepo serviceRepo;

  @Autowired
  public ServicesController(UserRepo.UsersOp.Retrieve userOpRetrieve,
      MongoTemplate mongoTemplate, FilesStorageService storageService, UniformLogger uniformLogger,
      ServiceRepo serviceRepo) {
    this.userOpRetrieve = userOpRetrieve;
    this.mongoTemplate = mongoTemplate;
    this.storageService = storageService;
    this.uniformLogger = uniformLogger;
    this.serviceRepo = serviceRepo;

  }

  @Value("${base.url}")
  private String baseurl;

  @GetMapping("/api/services/user")
  public ResponseEntity<Response> listUserServices(HttpServletRequest request,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang) 
          throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(new Response(
      serviceRepo.listUserServices(
          userOpRetrieve.retrieveUsers(request.getUserPrincipal().getName())),
        Variables.MODEL_SERVICE, HttpStatus.OK.value(), lang), HttpStatus.OK);
  }

  @GetMapping("/api/services/main")
  public ResponseEntity<Response> listServicesMain(
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(
        new Response(serviceRepo.listServicesMain(),
          Variables.MODEL_SERVICE, HttpStatus.OK.value(), lang),
        HttpStatus.OK);
  }

  @GetMapping("/api/services/{id}")
  public ResponseEntity<Response> retrieveService(@PathVariable("id") long serviceId,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
       throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(new Response(
      serviceRepo.retrieveService(serviceId), Variables.MODEL_SERVICE,
        HttpStatus.OK.value(), lang), HttpStatus.OK);
  }

  @DeleteMapping("/api/services")
  public ResponseEntity<Response> deleteServices(
        HttpServletRequest request, @RequestBody JSONObject jsonObject,
            @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
                throws NoSuchFieldException, IllegalAccessException {
    LinkedList<String> ls = serviceRepo.deleteServices(request.getUserPrincipal().getName(), jsonObject);
    HttpStatus httpStatus = (ls == null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

    return new ResponseEntity<>(new Response(
        ls, Variables.MODEL_SERVICE, httpStatus.value(), lang), HttpStatus.OK);
  }

  @PostMapping("/api/services/{system}")
  public ResponseEntity<Response> createService(
        HttpServletRequest request, @RequestBody JSONObject jsonObject,
      @PathVariable("system") String system, @RequestParam(
        value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws IOException, ParseException, NoSuchFieldException, IllegalAccessException {
    long id = serviceRepo.createService(request.getUserPrincipal().getName(), jsonObject, system);
    HttpStatus httpStatus = (id == 0 ? HttpStatus.FORBIDDEN : HttpStatus.OK);
    return new ResponseEntity<>(new Response(
          null, Variables.MODEL_SERVICE, httpStatus.value(), lang),
        HttpStatus.OK);
  }

  @PutMapping("/api/service/{id}/{system}")
  public ResponseEntity<Response> updateService(
        HttpServletRequest request, @PathVariable("id") long id,
      @RequestBody JSONObject jsonObject, @PathVariable("system") String system,
      @RequestParam(value = "lang", defaultValue =
          Variables.DEFAULT_LANG) String lang) throws NoSuchFieldException, IllegalAccessException, IOException {
    return new ResponseEntity<>(new Response(null, Variables.MODEL_SERVICE,
    serviceRepo.updateService(
            request.getUserPrincipal().getName(), id, jsonObject, system).value(),
        lang), HttpStatus.OK);
  }

  @GetMapping("/api/service/used")
    public ResponseEntity<Response> userService(HttpServletRequest request,
        @RequestParam (value = "lang",
            defaultValue = "fa") String lang) throws NoSuchFieldException, IllegalAccessException{
      return new ResponseEntity(new Response(
        new ServiceAudit(serviceRepo, mongoTemplate).usedService(
            request.getUserPrincipal().getName()),
                Variables.MODEL_SERVICE,HttpStatus.OK.value(),lang), HttpStatus.OK);
    }

  @GetMapping("/api/serviceCheck/{id}")
  public ResponseEntity<Response> serviceAccess(@PathVariable("id") long id,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang) {
    return new ResponseEntity<>(
        new ServiceAccess(mongoTemplate)
        .serviceAccess(id) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
  }

  @PostMapping("/api/services/metadata")
  public ResponseEntity<Response> uploadMetadata(@RequestParam("file") MultipartFile file,
      @RequestParam(value = "lang", defaultValue 
        = Variables.DEFAULT_LANG) String lang) throws NoSuchFieldException, IllegalAccessException {
    String result = new Metadata(storageService, baseurl).upload(file);
    HttpStatus httpStatus = (result == null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    return new ResponseEntity<>(
      new Response(result, Variables.MODEL_SERVICE, httpStatus.value(), lang), HttpStatus.OK);
  }

  @PostMapping("/api/services/icon")
  public ResponseEntity<Response> uploadIcon(@RequestParam("file") MultipartFile file,
      @RequestParam(value = "lang", defaultValue 
        = Variables.DEFAULT_LANG) String lang) throws NoSuchFieldException, IllegalAccessException {
    String result = new ServiceIcon(storageService, baseurl).upload(file);
    HttpStatus httpStatus = (result == null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    return new ResponseEntity<>(new Response(
        result, Variables.MODEL_SERVICE, httpStatus.value(), lang), HttpStatus.OK);

  }

  @GetMapping("/api/services/position/{serviceId}")
  public ResponseEntity<Response> increasePosition(@PathVariable("serviceId") String id,
      @RequestParam("value") int value, @RequestParam(value = "lang",
          defaultValue = Variables.DEFAULT_LANG) String lang) {
    if (value == 1) {
      return new ResponseEntity<>(new Position(mongoTemplate).increase(id));
    } else if (value == -1) {
      return new ResponseEntity<>(new Position(mongoTemplate).decrease(id));
    } else {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
  }

  @XmlElement
  @GetMapping(value = "/api/public/metadata/{file}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public @ResponseBody Object getMetaDataFile(@PathVariable("file") String file,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang) throws IOException {

    String metadataPath = new Settings().retrieve("metadata.file.path").getValue();

    FileInputStream in = new FileInputStream(metadataPath + file);

    HttpHeaders header = new HttpHeaders();
    header.setContentType(new MediaType("application", "xml"));

    return new HttpEntity<>(IOUtils.toByteArray(in), header);

  }

  @GetMapping(value = "/api/public/icon/{file}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  ResponseEntity<Response> getIconFile(
      HttpServletResponse response, @PathVariable("file") String file,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
          throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(
        new Response(new ServiceIcon(storageService, baseurl).show(response, file),
        Variables.MODEL_SERVICE, HttpStatus.OK.value(), lang), HttpStatus.OK);

  }
}
