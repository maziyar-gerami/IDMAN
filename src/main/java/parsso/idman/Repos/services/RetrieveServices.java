package parsso.idman.Repos.services;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.Services.Service;
import parsso.idman.Models.Services.ServiceGist;
import parsso.idman.Models.Services.ServiceType.MicroService;
import parsso.idman.Models.Users.User;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public interface RetrieveServices {
	List<MicroService> listUserServices(User user) throws IOException, ParseException;

	List<Service> listServicesFull() throws IOException, ParseException;

	List<Service> listServicesWithGroups(String ou) throws IOException, ParseException;

	List<MicroService> listServicesMain() throws IOException, ParseException;

	Service retrieveService(long id) throws IOException, ParseException;

	ServiceGist gistService(String apikey);

	boolean serviceAccess(long id);
}
