package parsso.idman.helpers.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.repos.LogsRepo;

@Service
public class Pulling {
  final LogsRepo.EventRepo eventRepo;
  final MongoTemplate mongoTemplate;

  @Autowired
  public Pulling(LogsRepo.EventRepo eventRepo, MongoTemplate mongoTemplate) {
    this.eventRepo = eventRepo;
    this.mongoTemplate = mongoTemplate;
  }

}
