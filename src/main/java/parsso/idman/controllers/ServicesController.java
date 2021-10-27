package parsso.idman.controllers;


import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.Services.Service;
import parsso.idman.Models.Services.ServiceType.MicroService;
import parsso.idman.repos.ServiceRepo;
import parsso.idman.repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.XmlElement;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

@RestController
public class ServicesController {
    private final UserRepo userRepo;
    private final ServiceRepo serviceRepo;
    @Value("${metadata.file.path}")
    private String metadataPath;
    @Value("${base.url}")
    private String baseUrl;

    @Autowired
    public ServicesController(@Qualifier("userRepoImpl") UserRepo userRepo, @Qualifier("serviceRepoImpl") ServiceRepo serviceRepo) {
        this.serviceRepo = serviceRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/api/services/user")
    public ResponseEntity<List<MicroService>> ListUserServices(HttpServletRequest request) {
        return new ResponseEntity<>(serviceRepo.listUserServices(userRepo.retrieveUsers(request.getUserPrincipal().getName())), HttpStatus.OK);
    }

    @GetMapping("/api/services/main")
    public ResponseEntity<List<MicroService>> listServicesMain() {
        return new ResponseEntity<>(serviceRepo.listServicesMain(), HttpStatus.OK);
    }

    @GetMapping("/api/services/{id}")
    public ResponseEntity<Service> retrieveService(@PathVariable("id") long serviceId) {
        return new ResponseEntity<>(serviceRepo.retrieveService(serviceId), HttpStatus.OK);
    }

    @DeleteMapping("/api/services")
    public ResponseEntity<LinkedList<String>> deleteServices(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        LinkedList<String> ls = serviceRepo.deleteServices(request.getUserPrincipal().getName(), jsonObject);
        HttpStatus httpStatus = (ls == null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(ls, httpStatus);
    }

    @PostMapping("/api/services/{system}")
    public ResponseEntity<HttpStatus> createService(HttpServletRequest request, @RequestBody JSONObject jsonObject, @PathVariable("system") String system) throws IOException, ParseException {
        long id = serviceRepo.createService(request.getUserPrincipal().getName(), jsonObject, system);
        if (id == 0)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        else{
            if (jsonObject.get("extraInfo")!=null){
                LinkedHashMap extra = (LinkedHashMap) jsonObject.get("extraInfo");
                if(extra.get("dailyAccess")!=null){
                    Service service = serviceRepo.retrieveService(id);

                    service.getAccessStrategy().setAtClass("org.apereo.cas.services.RemoteEndpointServiceAccessStrategy");
                    service.getAccessStrategy().setEndpointUrl(baseUrl+"/api/serviceCheck/"+id);
                    service.getAccessStrategy().setAcceptableResponseCodes("200");


                    String jsonInString = new Gson().toJson(service);
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObjectExtraInfo = (JSONObject) parser.parse(jsonInString);

                    serviceRepo.updateService("System",id,jsonObjectExtraInfo,system);

                }
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PutMapping("/api/service/{id}/{system}")
    public ResponseEntity<String> updateService(HttpServletRequest request, @PathVariable("id") long id,
                                                @RequestBody JSONObject jsonObject, @PathVariable("system") String system) throws ParseException {
        if (jsonObject.get("extraInfo")!=null){
            LinkedHashMap extra = (LinkedHashMap) jsonObject.get("extraInfo");
            if(extra.get("dailyAccess")!=null){
                Service service = serviceRepo.retrieveService(id);

                service.getAccessStrategy().setAtClass("org.apereo.cas.services.RemoteEndpointServiceAccessStrategy");
                service.getAccessStrategy().setEndpointUrl(baseUrl+"/api/serviceCheck/"+id);
                service.getAccessStrategy().setAcceptableResponseCodes("200");


                String jsonInString = new Gson().toJson(service);
                JSONParser parser = new JSONParser();
                JSONObject jsonObjectExtraInfo = (JSONObject) parser.parse(jsonInString);

                serviceRepo.updateService("System",id,jsonObjectExtraInfo,system);

            }
        }

        return new ResponseEntity<>(serviceRepo.updateService(request.getUserPrincipal().getName(), id, jsonObject, system));
    }

    @GetMapping("/api/serviceCheck/{id}")
    public ResponseEntity<HttpStatus> serviceAccess(@PathVariable("id") long id) {
        return new ResponseEntity<>(serviceRepo.serviceAccess(id) ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/services/metadata")
    public ResponseEntity<String> uploadMetadata(@RequestParam("file") MultipartFile file) {
        String result = serviceRepo.uploadMetadata(file);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/services/icon")
    public ResponseEntity<String> uploadIcon(@RequestParam("file") MultipartFile file) {
        String result = serviceRepo.uploadIcon(file);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api/services/position/{serviceId}")
    public ResponseEntity<HttpStatus> increasePosition(@PathVariable("serviceId") String id, @RequestParam("value") int value) {
        if (value == 1)
            return new ResponseEntity<>(serviceRepo.increasePosition(id));
        else if (value == -1)
            return new ResponseEntity<>(serviceRepo.decreasePosition(id));
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @XmlElement
    @GetMapping(value = "/api/public/metadata/{file}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public @ResponseBody
    Object getMetaDataFile(@PathVariable("file") String file) throws IOException {

        FileInputStream in = new FileInputStream(metadataPath + file);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "xml"));

        return new HttpEntity<>(IOUtils.toByteArray(in), header);

    }

    @GetMapping(value = "/api/public/icon/{file}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<String> getIconFile(HttpServletResponse response, @PathVariable("file") String file) {
        return new ResponseEntity<>(serviceRepo.showServicePic(response, file), HttpStatus.OK);

    }
}
