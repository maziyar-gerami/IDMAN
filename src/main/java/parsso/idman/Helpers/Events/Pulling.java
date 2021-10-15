package parsso.idman.Helpers.Events;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.Service.Trim;
import parsso.idman.Models.Logs.Event;
import parsso.idman.repos.LogsRepo;
import parsso.idman.repos.ServiceRepo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("SameReturnValue")
@Service
public class Pulling {
    final LogsRepo.EventRepo eventRepo;
    final ServiceRepo serviceRepo;
    final MongoTemplate mongoTemplate;

    @Autowired
    public Pulling(LogsRepo.EventRepo eventRepo, ServiceRepo serviceRepo, MongoTemplate mongoTemplate) {
        this.eventRepo = eventRepo;
        this.serviceRepo = serviceRepo;
        this.mongoTemplate = mongoTemplate;
    }

    public String insert() throws IOException {
        List<Event> mainEvents = eventRepo.analyze(0, 0);
        List<parsso.idman.Models.Services.Service> services = serviceRepo.listServicesFull();
        run(mainEvents, services);
        return null;
    }

    public void update() throws IOException {
        List<Event> secondaryEvents = eventRepo.analyze(0, 0);
        List<parsso.idman.Models.Services.Service> services = serviceRepo.listServicesFull();
        run(secondaryEvents, services);
    }

    public void run(List<Event> events, List<parsso.idman.Models.Services.Service> services) throws UnknownHostException {
        for (Event event : events) {

            InetAddress address = InetAddress.getByName(event.getProperties().getServerip());
            byte[] bytes = address.getAddress();

            if (event.getService() == null)
                for (parsso.idman.Models.Services.Service service : services) {
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


