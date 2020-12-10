package parsso.idman.Helpers.Events;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.Service.Trim;
import parsso.idman.Models.Event;
import parsso.idman.Repos.EventRepo;
import parsso.idman.Repos.ServiceRepo;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;


@Service
public class Pulling {

    public static String mainCollection = "MongoDbCasEventRepository";
    public static String secondaryCollection = "IDMAN_Events";
    @Autowired
    EventRepo eventRepo;
    @Autowired
    ServiceRepo serviceRepo;
    @Autowired
    MongoTemplate mongoTemplate;

    public String insert() throws ParseException, org.json.simple.parser.ParseException, IOException {
        List<Event> mainEvents = eventRepo.getMainListEvents();
        List<parsso.idman.Models.Service> services = serviceRepo.listServices();
        run(mainEvents,services, mainCollection);
        return null;
    }

    public void update() throws ParseException, org.json.simple.parser.ParseException, IOException {
        List<Event> secondaryEvents = eventRepo.getMainListEvents();
        List<parsso.idman.Models.Service> services = serviceRepo.listServices();
        run(secondaryEvents, services, secondaryCollection);
    }


    public void run(List<Event> events, List<parsso.idman.Models.Service> services, String collection) throws UnknownHostException {
        for (Event event : events) {

            InetAddress address = InetAddress.getByName(event.getProperties().getServerip());
            byte[] bytes = address.getAddress();

            if (event.getService() == null)
                for (parsso.idman.Models.Service service : services) {
                    try {
                        InetAddress address1 = InetAddress.getByName(Trim.trimServiceId(service.getServiceId()));
                        byte[] bytes1 = address1.getAddress();

                        if (Arrays.equals(bytes, bytes1))
                            event.setService(service.getServiceId());


                    } catch (Exception e) {
                        event.setService(null);

                    }

                }

        }
    }
}


