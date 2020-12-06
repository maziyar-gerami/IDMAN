package parsso.idman.Repos;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.Service;
import parsso.idman.Models.ServiceType.MicroService;
import parsso.idman.Models.User;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public interface ServiceRepo {

    List<MicroService> listUserServices(User user) throws IOException, ParseException;

    List<Service> listServices() throws IOException, ParseException;

    Service retrieveService(long id) throws IOException, ParseException;

    HttpStatus deleteService(long id) throws IOException, ParseException;

    LinkedList<String> deleteServices(JSONObject files) throws IOException;

    String uploadMetadata(MultipartFile file, String userId);

    HttpStatus createService(JSONObject jsonObject, String system) throws IOException;

    HttpStatus updateService(long id, JSONObject jsonObject, String system) throws IOException, ParseException;
}
