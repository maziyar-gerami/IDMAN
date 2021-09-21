package parsso.idman.Controllers;


import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.Services.Service;
import parsso.idman.Models.Services.ServiceGist;
import parsso.idman.Models.Services.ServiceType.MicroService;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Controller
public class ServicesController {
	MongoTemplate mongoTemplate;
	private UserRepo userRepo;
	private ServiceRepo serviceRepo;
	@Value("${metadata.file.path}")
	private String metadataPath;

	public ServicesController(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Autowired
	public ServicesController(@Qualifier("userRepoImpl") UserRepo userRepo, @Qualifier("serviceRepoImpl") ServiceRepo serviceRepo) {
		this.serviceRepo = serviceRepo;
		this.userRepo = userRepo;
	}

	@SuppressWarnings("SameReturnValue")
	@GetMapping("/services")
	public String getPageServices() {
		return "services";
	}

	@SuppressWarnings("SameReturnValue")
	@GetMapping("/createservice")
	public String CreateService() {
		return "createservice";
	}

	@GetMapping("/api/services/user")
	public ResponseEntity<List<MicroService>> ListUserServices(HttpServletRequest request) throws IOException, ParseException {
		return new ResponseEntity<>(serviceRepo.listUserServices(userRepo.retrieveUsers(request.getUserPrincipal().getName())), HttpStatus.OK);
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

	@PostMapping("/service/notifyService")
	public ResponseEntity<ServiceGist> retrieveGistService(@RequestBody JSONObject jsonObject) {
		return new ResponseEntity<>(serviceRepo.gistService("jsonObject"), HttpStatus.OK);
	}

	@DeleteMapping("/api/services")
	public ResponseEntity<LinkedList<String>> deleteServices(HttpServletRequest request, @RequestBody JSONObject jsonObject) throws IOException {
		LinkedList<String> ls = serviceRepo.deleteServices(request.getUserPrincipal().getName(), jsonObject);
		HttpStatus httpStatus = (ls == null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

		return new ResponseEntity<>(ls, httpStatus);
	}

	@PostMapping("/api/services/{system}")
	public ResponseEntity<String> createService(HttpServletRequest request, @RequestBody JSONObject jsonObject, @PathVariable("system") String system) throws IOException, ParseException {

		return new ResponseEntity<>(serviceRepo.createService("maziyar", jsonObject, system));
	}

	@PutMapping("/api/service/{id}/{system}")
	public ResponseEntity<String> updateService(HttpServletRequest request, @PathVariable("id") long id,
	                                            @RequestBody JSONObject jsonObject, @PathVariable("system") String system) throws IOException, ParseException {
		return new ResponseEntity<>(serviceRepo.updateService("maziyar", id, jsonObject, system));
	}

	@PostMapping("/api/services/metadata")
	public ResponseEntity<String> uploadMetadata(@RequestParam("file") MultipartFile file) {
		String result = serviceRepo.uploadMetadata(file);
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
	Object getFile(@PathVariable("file") String file) throws IOException {

		FileInputStream in = new FileInputStream(new File(metadataPath + file));

		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "xml"));

		return new HttpEntity<>(IOUtils.toByteArray(in), header);

	}
}
