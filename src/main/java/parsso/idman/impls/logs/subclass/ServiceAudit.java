package parsso.idman.impls.logs.subclass;

import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import parsso.idman.helpers.Variables;
import parsso.idman.models.services.serviceType.MicroService;
import parsso.idman.repos.ServiceRepo;

public class ServiceAudit {
  final ServiceRepo serviceRepo;
  final MongoTemplate mongoTemplate;
  List<String> services;


  public ServiceAudit(ServiceRepo serviceRepo, MongoTemplate mongoTemplate) {
    this.serviceRepo = serviceRepo;
    this.mongoTemplate = mongoTemplate;
    services = new LinkedList<>();
  }

  public List<JSONObject> usedService(String userId){
    List<MicroService> microServices = serviceRepo.listServicesMain();
    Query query;
    List<JSONObject> result = new LinkedList<>();
    for (MicroService microService : microServices) {
      query = new Query(Criteria.where("principal").is(userId));
      query.addCriteria(Criteria.where("resourceOperatedUpon").regex(microService.getServiceId()));
      if (mongoTemplate.exists(query, Variables.col_audit)) {
        JSONObject temp = new JSONObject();
        temp.put("_id", microService.get_id());
        temp.put("name", microService.getName());
        result.add(temp);
      }

    }
    return result;
  }

  
}
