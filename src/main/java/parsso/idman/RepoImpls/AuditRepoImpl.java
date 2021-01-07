package parsso.idman.RepoImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Audit;
import parsso.idman.Models.ListAudits;
import parsso.idman.Models.Time;
import parsso.idman.Repos.AuditRepo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AuditRepoImpl implements AuditRepo {

    private final String mainCollection = "MongoDbCasAuditRepository";

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ListAudits getListSizeAudits(int p, int n){
        List<Audit> allAuditts = analyze(mainCollection,(p-1)*n,n);
        long size =  mongoTemplate.getCollection(mainCollection).countDocuments();
        return new ListAudits(allAuditts,size,(int) Math.ceil(size/n));
    }

    @Override
    public ListAudits getListUserAudits(String userId, int p, int n) {
        Query query = new Query(Criteria.where("principal").is(userId))
                .with(Sort.by(Sort.Direction.DESC,"_id"));
        long size = mongoTemplate.count(query, Audit.class,mainCollection);

        query.skip((p-1)*(n)).limit(n);
        List<Audit> auditList =  mongoTemplate.find(query, Audit.class,mainCollection);
        ListAudits listAudits = new ListAudits(auditList, size,(int) Math.ceil(size/n));
        return listAudits;
    }
    @Override
    public ListAudits getListUserAuditByDate(String date, String userId, int skip, int limit) throws ParseException {


        String time = new Time(Integer.valueOf(date.substring(4)),Integer.valueOf(date.substring(2,4)),Integer.valueOf(date.substring(0,2))).toStringDate();
        String timeStart = time+"T00:00:00";
        String timeEnd = time+"T23:59:59";
        Query query = new Query(Criteria.where("principal").is(userId).and("whenActionWasPerformed")
                .gte(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(timeStart))
                .lte(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(timeEnd)));
        long size =  mongoTemplate.count(query, Audit.class,mainCollection);
        query.skip((skip-1)*(limit)).limit(limit).with(Sort.by(Sort.Direction.DESC,"_id"));
        List<Audit> auditList =  mongoTemplate.find(query, Audit.class,mainCollection);
        int pages = (int) Math.ceil(size/limit);
        return new ListAudits(auditList,size,pages);

    }

    @Override
    public ListAudits getAuditsByDate(String date, int p, int n) throws ParseException {


        String time = new Time(Integer.valueOf(date.substring(4)),Integer.valueOf(date.substring(2,4)),Integer.valueOf(date.substring(0,2))).toStringDate();
        String timeStart = time+"T00:00:00";
        String timeEnd = time+"T23:59:59";

        Query query = new Query(Criteria.where("whenActionWasPerformed")
                .gte(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(timeStart))
                .lte(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(timeEnd)));
        long size =  mongoTemplate.count(query,Audit.class,mainCollection);
        query.skip((p-1)*n).limit(n);

        List<Audit> allAudit = mongoTemplate.find(query, Audit.class, mainCollection);
        return new ListAudits(allAudit,size,(int) Math.ceil(size/n));

    }

    public List<Audit> analyze(String collection,int skip,int limit) {
        Query query = new Query().skip(skip).limit(limit).with(Sort.by(Sort.Direction.DESC,"_id"));
        return mongoTemplate.find(query, Audit.class,collection);
    }

}


