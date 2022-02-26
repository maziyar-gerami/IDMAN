package parsso.idman.repoImpls.services;

import org.json.simple.JSONArray;
import org.springframework.data.mongodb.core.MongoTemplate;
import parsso.idman.models.license.License;
import parsso.idman.models.services.Service;
import parsso.idman.models.services.serviceType.MicroService;

import java.util.LinkedList;
import java.util.List;

public class ServicesGroup {
    final MongoTemplate mongoTemplate;

    public ServicesGroup(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public License servicesOfGroup(String ouID) {
        List<MicroService> licensed = new LinkedList<>();

        List<Service> allServices = new RetrieveService(mongoTemplate).listServicesFull();

        for (Service service : allServices)
            if (service.getAccessStrategy().getRequiredAttributes().get("ou") != null)
                for (Object name : (JSONArray) ((JSONArray) (service.getAccessStrategy().getRequiredAttributes().get("ou"))).get(1))
                    if (ouID.equalsIgnoreCase(name.toString()))
                        licensed.add(new MicroService(service));

        return new License(licensed, null);

    }
}
