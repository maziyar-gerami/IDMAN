package parsso.idman.RepoImpls.logs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.TimeHelper;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.Audit;
import parsso.idman.Models.Logs.Event;
import parsso.idman.repos.LogsRepo;

import java.time.ZoneId;
import java.util.List;

@Service
public class AuditsRepoImpl implements LogsRepo.AuditRepo {
    final MongoTemplate mongoTemplate;

    @Autowired
    public AuditsRepoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Audit.ListAudits retrieve(String userId, String date, int p, int n) {
        Query query = new Query();
        if (!userId.equals(""))
            query.addCriteria(Criteria.where("principal").is(userId));

        if (!date.equals("")) {
            long[] range = TimeHelper.specificDateToEpochRange(TimeHelper.stringInputToTime(date), ZoneId.of(Variables.ZONE));
            query.addCriteria(Criteria.where("whenActionWasPerformed")
                    .gte(TimeHelper.convertEpochToDate(range[0])).lte(TimeHelper.convertEpochToDate(range[1])));
        }


        long size = mongoTemplate.count(query, Event.class, Variables.col_audit);

        query.skip((long) (p - 1) * n).limit(n).with(Sort.by(Sort.Direction.DESC, "_id"));
        List<Audit> audits = mongoTemplate.find(query, Audit.class, Variables.col_audit);
        return new Audit.ListAudits(audits, size, (int) Math.ceil((double) size / (double) n));
    }

}


