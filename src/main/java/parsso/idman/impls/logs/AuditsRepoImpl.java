package parsso.idman.impls.logs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.LogTime;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.Audit;
import parsso.idman.models.logs.Event;
import parsso.idman.models.other.Time;
import parsso.idman.repos.LogsRepo;
import parsso.idman.repos.ServiceRepo;

@Service
public class AuditsRepoImpl implements LogsRepo.AuditRepo {
  final MongoTemplate mongoTemplate;
  final ServiceRepo serviceRepo;

  @Autowired
  public AuditsRepoImpl(MongoTemplate mongoTemplate, ServiceRepo serviceRepo) {
    this.mongoTemplate = mongoTemplate;
    this.serviceRepo = serviceRepo;
  }

  @Override
  public Audit.ListAudits retrieve(String userId, String startDate, String endDate, int p, int n, long sid) {
    long[] range = null;
    Query query = new Query(Criteria.where("actionPerformed")
        .is("SERVICE_ACCESS_ENFORCEMENT_TRIGGERED").and("principal").ne("audit:unknown").and("resourceOperatedUpon").exists(true));
    

    if (!userId.equals("")) {
      query.addCriteria(Criteria.where("principal").is(userId));
    }

    if(sid!=(0)){
      parsso.idman.models.services.Service ms = serviceRepo.retrieveService(sid);
      query.addCriteria(Criteria.where(
            "resourceOperatedUpon").regex(ms.getServiceId()));
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
      for (parsso.idman.models.services.Service service : serviceRepo.listServicesFull()) {
        if(audit.getResourceOperatedUpon().contains(service.getServiceId())){
          audit.setService(service.getName());
          break;
        }
      }
    }
  
    return new Audit.ListAudits(audits, size, (int) Math.ceil((double) size / (double) n));
  }

}
