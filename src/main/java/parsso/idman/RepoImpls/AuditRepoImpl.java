package parsso.idman.RepoImpls;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.Audit;
import parsso.idman.Models.Logs.ListAudits;
import parsso.idman.Models.Logs.Report;
import parsso.idman.Models.Time;
import parsso.idman.Repos.AuditRepo;

import java.text.ParseException;
import java.time.ZoneId;
import java.util.List;

@Service
public class AuditRepoImpl implements AuditRepo {

    private final String mainCollection = "MongoDbCasAuditRepository";

    @Autowired
    MongoTemplate mongoTemplate;

    ZoneId zoneId = ZoneId.of(Variables.ZONE);


    @Override
    public ListAudits getListSizeAudits(int p, int n) {
        List<Audit> allAudits = analyze(mainCollection, (p - 1) * n, n);
        long size = mongoTemplate.getCollection(mainCollection).countDocuments();
        return new ListAudits(allAudits, size, (int) Math.ceil((double)size / (double)n));
    }

    @Override
    public ListAudits getListUserAudits(String userId, int p, int n) {
        Query query = new Query(Criteria.where("principal").is(userId))
                .with(Sort.by(Sort.Direction.DESC, "_id"));
        long size = mongoTemplate.count(query, Audit.class, mainCollection);

        query.skip((p - 1) * (n)).limit(n);
        List<Audit> auditList = mongoTemplate.find(query, Audit.class, mainCollection);
        ListAudits listAudits = new ListAudits(auditList, size, (int) Math.ceil(size / n));
        return listAudits;
    }

    @Override
    public ListAudits getListUserAuditByDate(String date, String userId, int p, int n) throws ParseException {

        int skip = (p-1)*n;
        int limit = n;

        Time time = new Time(Integer.valueOf(date.substring(4)),
                Integer.valueOf(date.substring(2, 4)),
                Integer.valueOf(date.substring(0, 2)));
        long [] range = Time.specificDateToEpochRange(time, zoneId);

        Query query = new Query(Criteria.where("whenActionWasPerformed")
                .gte(Time.convertEpochToDate(range[0])).lte(Time.convertEpochToDate(range[1])).and("principal").is(userId));

        long size = mongoTemplate.find(query, Report.class,  mainCollection).size();

        List<Audit> reportList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "whenActionWasPerformed")).skip(skip).limit(limit),Audit.class, mainCollection);

        return new ListAudits(reportList, size, (int) Math.ceil(size / limit));

    }

    @Override
    public ListAudits getAuditsByDate(String date, int p, int n) throws ParseException {


        int skip = (p-1)*n;
        int limit = n;

        Time time = new Time(Integer.valueOf(date.substring(4)),
                Integer.valueOf(date.substring(2, 4)),
                Integer.valueOf(date.substring(0, 2)));
        long [] range = Time.specificDateToEpochRange(time, zoneId);

        Query query = new Query(Criteria.where("whenActionWasPerformed")
                .gte(Time.convertEpochToDate(range[0])).lte(Time.convertEpochToDate(range[1])));

        long size = mongoTemplate.find(query, Report.class,  mainCollection).size();

        List<Audit> reportList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "whenActionWasPerformed")).skip(skip).limit(limit),Audit.class, mainCollection);

        return new ListAudits(reportList, size, (int) Math.ceil(size / limit));

    }

    public List<Audit> analyze(String collection, int skip, int limit) {
        Query query = new Query().skip(skip).limit(limit).with(Sort.by(Sort.Direction.DESC, "_id"));
        return mongoTemplate.find(query, Audit.class, collection);
    }

}


