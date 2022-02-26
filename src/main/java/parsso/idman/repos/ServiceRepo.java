package parsso.idman.repos;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.models.services.Service;
import parsso.idman.models.services.serviceType.MicroService;
import parsso.idman.models.users.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("SameReturnValue")
public class ServiceRepo {

    public  interface Retrieve {

        List<MicroService> listUserServices(User user);

        List<Service> listServicesFull();

        List<Service> listServicesWithGroups(String ou);

        List<MicroService> listServicesMain();

        Service retrieveService(long id);

        boolean serviceAccess(long id);

        String showServicePic(HttpServletResponse response, String file);

    }

    public interface Delete {

        LinkedList<String> delete(String doerID, JSONObject files);
    }

    @org.springframework.stereotype.Service
    public interface Update {

        String uploadMetadata(MultipartFile file);

        void updateOuIdChange(String doerID, Service service, long sid, String name, String oldOu, String newOu) throws IOException;

        HttpStatus updateService(String doerID, long id, JSONObject jsonObject, String system);

        String uploadIcon(MultipartFile file);

    }


    public interface Create {

        long createService(String doerID, JSONObject jsonObject, String system) throws IOException, ParseException;

    }
}
