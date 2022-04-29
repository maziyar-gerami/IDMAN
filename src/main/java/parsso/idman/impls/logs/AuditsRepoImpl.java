package parsso.idman.impls.logs;

import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.LogTime;
import parsso.idman.helpers.Variables;
import parsso.idman.impls.logs.subclass.ServiceAudit;
import parsso.idman.impls.services.RetrieveService;
import parsso.idman.models.logs.Audit;
import parsso.idman.models.logs.Event;
import parsso.idman.models.other.Time;
import parsso.idman.models.services.ServiceGist;
import parsso.idman.models.services.serviceType.MicroService;
import parsso.idman.repos.LogsRepo;
import parsso.idman.repos.ServiceRepo;

@Service
public class AuditsRepoImpl implements LogsRepo.AuditRepo {
  final MongoTemplate mongoTemplate;
  final RetrieveService retrieveService;

  @Autowired
  public AuditsRepoImpl(MongoTemplate mongoTemplate, RetrieveService retrieveService) {
    this.mongoTemplate = mongoTemplate;
    this.retrieveService = retrieveService;
  }

  @Override
  public Audit.ListAudits retrieve(String userId, String startDate, String endDate, int p, int n, List<Long> services) {
    
    long[] range = null;
    Query query = new Query(Criteria.where("actionPerformed").is("SERVICE_ACCESS_ENFORCEMENT_TRIGGERED"));
    if (!userId.equals("")) {
      query.addCriteria(Criteria.where("principal").is(userId));
    }

    Criteria criteria = new Criteria();
    if(services!=null){
    for (long sid : services) {
      parsso.idman.models.services.Service ms = retrieveService.retrieveService(sid);
      Criteria temp= Criteria.where("resourceOperatedUpon").regex(ms.getServiceId());
      criteria.orOperator(criteria,temp);
    }
    query.addCriteria(criteria);
  }

    range = LogTime.rangeCreator(startDate, endDate);

    if (range != null) {
      query.addCriteria(Criteria.where("whenActionWasPerformed")
          .gte(new Time().convertEpochToDate(range[0]))
          .lte(new Time().convertEpochToDate(range[1])));
    }

    long size = mongoTemplate.count(query, Event.class, Variables.col_audit);

    query.skip((long) (p - 1) * n).limit(n).with(Sort.by(Sort.Direction.DESC, "_id"));
    List<Audit> audits = mongoTemplate.find(query, Audit.class, Variables.col_audit);

    for (Audit audit : audits) {
      for (parsso.idman.models.services.Service service : retrieveService.listServicesFull()) {
        if(audit.getResourceOperatedUpon().contains(service.getServiceId())){
          audit.setService(service.getName());
          break;
        }
      }
    }
  
    return new Audit.ListAudits(audits, size, (int) Math.ceil((double) size / (double) n));
  }

}
