package parsso.idman.repos;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.Services.Service;
import parsso.idman.Models.Services.ServiceType.MicroService;
import parsso.idman.Models.Users.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("SameReturnValue")
public interface ServiceRepo {
    List<MicroService> listUserServices(User user);

    List<Service> listServicesFull();

    List<Service> listServicesWithGroups(String ou);

    List<MicroService> listServicesMain();

    Service retrieveService(long id);

    LinkedList<String> deleteServices(String doerID, JSONObject files);

    String uploadMetadata(MultipartFile file);

    void updateOuIdChange(String doerID, Service service, long sid, String name, String oldOu, String newOu) throws IOException;

    HttpStatus createService(String doerID, JSONObject jsonObject, String system) throws IOException, ParseException;

    HttpStatus updateService(String doerID, long id, JSONObject jsonObject, String system);

    HttpStatus increasePosition(String id);

    HttpStatus decreasePosition(String id);


    boolean serviceAccess(long id);

    String uploadIcon(MultipartFile file);

    String showServicePic(HttpServletResponse response, String file);
}
