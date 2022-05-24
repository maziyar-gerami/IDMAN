package parsso.idman.helpers.onetime;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import parsso.idman.helpers.Variables;
import parsso.idman.impls.services.RetrieveService;
import parsso.idman.models.other.OneTime;
import parsso.idman.models.services.Service;
import parsso.idman.models.services.serviceType.SimpleService;

public class SimpleServiceFix {
  final MongoTemplate mongoTemplate;
  final RetrieveService retrieveService;

  public SimpleServiceFix(MongoTemplate mongoTemplate, RetrieveService retrieveService) {
    this.mongoTemplate = mongoTemplate;
    this.retrieveService = retrieveService;
  }

  public void run(){
    List<SimpleService> sms = mongoTemplate.find(new Query(),SimpleService.class,Variables.col_servicesExtraInfo);
    for (SimpleService simpleService : sms){
      Service tmp = retrieveService.retrieveService(simpleService.get_id());
      simpleService.setName(tmp.getName());
      simpleService.setServiceId(tmp.getServiceId());
      simpleService.setDescription(tmp.getDescription());
      mongoTemplate.save(simpleService, Variables.col_servicesExtraInfo);

    }
    mongoTemplate.save(new OneTime(Variables.SIMPLESERVICE_FIX, true, new Date().getTime()), Variables.col_OneTime);
  }
  
}
