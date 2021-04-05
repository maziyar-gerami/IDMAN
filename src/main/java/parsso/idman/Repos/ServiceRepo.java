package parsso.idman.Repos;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.Services.Service;
import parsso.idman.Models.Services.ServiceType.MicroService;
import parsso.idman.Models.Users.User;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public interface ServiceRepo {

    List<MicroService> listUserServices(User user) throws IOException, ParseException;

    List<Service> listServicesFull() throws IOException, ParseException;

    List<Service> listServicesWithGroups(String ou) throws IOException, ParseException;

    List<MicroService> listServicesMain() throws IOException, ParseException;

    Service retrieveService(long id) throws IOException, ParseException;

    LinkedList<String> deleteServices(String doerID,JSONObject files) throws IOException;

    String uploadMetadata(MultipartFile file);

    HttpStatus updateOuIdChange(String doerID,Service service, long sid, String name, String oldOu, String newOu) throws IOException;

    HttpStatus createService(String doerID, JSONObject jsonObject, String system) throws IOException;

    HttpStatus updateService(String doerID, long id, JSONObject jsonObject, String system) throws IOException, ParseException;

    HttpStatus increasePosition(String id);

    HttpStatus decreasePosition(String id);
}
