package parsso.idman.repoImpls.logs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.Audit;
import parsso.idman.models.logs.Event;
import parsso.idman.models.other.Time;
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
    public Audit.ListAudits retrieve(String userId, String startDate, String endDate, int p, int n) {
        long[] range = null;
        Query query = new Query();
        if (!userId.equals(""))
            query.addCriteria(Criteria.where("principal").is(userId));

        if (!startDate.equals("") && !endDate.equals("")) {
            range = new Time().dateRangeToEpochRange(new Time().stringInputToTime(startDate),
                    new Time().stringInputToTime(endDate), ZoneId.of(Variables.ZONE));

        } else if (!startDate.equals("") && endDate.equals("")) {
            range = new Time().dateRangeToEpochRange(new Time().stringInputToTime(startDate),
                    new Time(Integer.parseInt(endDate.substring(0, 2)),
                            Integer.parseInt(endDate.substring(2, 4)), Integer.parseInt(endDate.substring(4))),
                    ZoneId.of(Variables.ZONE));

        } else if (startDate.equals("") && !endDate.equals("")) {
            range = new Time().dateRangeToEpochRange(new Time(Integer.parseInt(startDate.substring(0, 2)),
                    Integer.parseInt(startDate.substring(2, 4)), Integer.parseInt(startDate.substring(4))),
                    new Time().stringInputToTime(endDate), ZoneId.of(Variables.ZONE));
        }

        if (range != null)
            query.addCriteria(Criteria.where("whenActionWasPerformed")
                    .gte(new Time().convertEpochToDate(range[0])).lte(new Time().convertEpochToDate(range[1])));

        long size = mongoTemplate.count(query, Event.class, Variables.col_audit);

        query.skip((long) (p - 1) * n).limit(n).with(Sort.by(Sort.Direction.DESC, "_id"));
        List<Audit> audits = mongoTemplate.find(query, Audit.class, Variables.col_audit);
        return new Audit.ListAudits(audits, size, (int) Math.ceil((double) size / (double) n));
    }

}
