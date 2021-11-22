package parsso.idman.helpers.events;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.repos.LogsRepo;
import parsso.idman.repos.ServiceRepo;

@Service
public class Pulling {
    @SuppressWarnings("unused")
    final LogsRepo.EventRepo eventRepo;
    @SuppressWarnings("unused")
    final ServiceRepo serviceRepo;
    @SuppressWarnings("unused")
    final MongoTemplate mongoTemplate;

    @Autowired
    public Pulling(LogsRepo.EventRepo eventRepo, ServiceRepo serviceRepo, MongoTemplate mongoTemplate) {
        this.eventRepo = eventRepo;
        this.serviceRepo = serviceRepo;
        this.mongoTemplate = mongoTemplate;
    }


}


