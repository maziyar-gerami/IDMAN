package parsso.idman.Helpers.Service;


import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.core.MongoTemplate;
import parsso.idman.Models.ServiceType.MicroService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Pulling {

    public static void pullServices() throws UnknownHostException {

        String collection = "IDMAN_Services";
        String mongoHosts = "mongodb://parssouser:APA00918@parsso.razi.ac.ir:27017/parssodb?ssl=false";

        MongoTemplate mongoTemplate = new MongoTemplate(MongoClients.create(mongoHosts), "parssodb");

        List<MicroService> microServices = mongoTemplate.findAll(MicroService.class, collection);

        for (MicroService microservice : microServices) {
            InetAddress[] machines = InetAddress.getAllByName(microservice.getServiceId());
            List<String> IPaddresses = new LinkedList<>();

            for (InetAddress machine : machines)
                IPaddresses.add(machine.getHostAddress());
            MicroService microService = new MicroService(microservice.getServiceId(), IPaddresses);
            microService.set_id(microservice.get_id());

            mongoTemplate.save(microService, collection);

        }
        System.out.print(new Date() + "\n");

    }

}
