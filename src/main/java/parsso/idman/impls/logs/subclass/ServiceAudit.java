package parsso.idman.impls.logs.subclass;

import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import parsso.idman.helpers.Variables;
import parsso.idman.impls.services.RetrieveService;
import parsso.idman.models.services.serviceType.MicroService;

public class ServiceAudit {
  final RetrieveService retrieveService;
  final MongoTemplate mongoTemplate;
  List<String> services;


  public ServiceAudit(RetrieveService retrieveService, MongoTemplate mongoTemplate) {
    this.retrieveService = retrieveService;
    this.mongoTemplate = mongoTemplate;
    services = new LinkedList<>();
  }

  public List<JSONObject> usedService(String userId){
    List<MicroService> microServices = retrieveService.listServicesMain("","");
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
