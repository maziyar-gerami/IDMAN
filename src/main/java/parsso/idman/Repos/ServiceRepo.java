package parsso.idman.Repos;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import parsso.idman.Models.Service;
import parsso.idman.Models.User;

import java.io.IOException;
import java.util.List;

public interface ServiceRepo {

    List<Service> listUserServices(User user) throws IOException, ParseException;
    List<Service> listServices() throws IOException, ParseException;
    Service retrieveService(long id) throws IOException, ParseException;
    HttpStatus deleteService(long id) throws IOException, ParseException;
    HttpStatus deleteServices();
    HttpStatus createService(JSONObject jsonObject);
    HttpStatus updateService(long id, JSONObject jsonObject) throws IOException, ParseException;
}
