package parsso.idman.RepoImpls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Audit;
import parsso.idman.Models.Event;
import parsso.idman.Models.ListAudits;
import parsso.idman.Models.ListEvents;
import parsso.idman.Repos.AuditRepo;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.utils.Convertor.DateConverter;
import parsso.idman.utils.Query.QueryDomain;
import java.text.ParseException;
import java.util.*;

@Service
public class AuditRepoImpl implements AuditRepo {

    private String mainCollection = "MongoDbCasAuditRepository";

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    QueryDomain queryDomain;
    @Autowired
    ServiceRepo serviceRepo;

    @Override
    public ListAudits getListSizeAudits(int p, int n){
        Query query = new Query().skip((p-1)*(n)).limit(n);
        query.with(Sort.by(Sort.Direction.DESC,"_id"));
        List<Audit> allAudits = mongoTemplate.find(query,Audit.class,mainCollection);
        long size =  mongoTemplate.getCollection(mainCollection).countDocuments();
        return new ListAudits(allAudits,size, (int) Math.ceil(size/n));
    }

    private int inverseP(int p,int n,Query query){
        long size =  mongoTemplate.count(query,Audit.class,mainCollection);
        int pages = (int) Math.ceil(size/n);
        return pages-(p-1);
    }

    @Override
    public ListAudits getListUserAudits(String userId, int skip, int limit) {
        Query query = new Query(Criteria.where("principal").is(userId));
        query.skip((skip-1)*(limit)).limit(limit);
        query.with(Sort.by(Sort.Direction.DESC,"_id"));
        List<Audit> auditList =  mongoTemplate.find(query, Audit.class,mainCollection);
        ListAudits listAudits = new ListAudits(auditList, auditList.size(),(int) Math.ceil(auditList.size()/limit));
        return listAudits;
    }
    @Override
    public ListAudits getListUserAuditByDate(String date, String userId, int skip, int limit) throws ParseException {

        Query query = new Query(Criteria.where("principal").is(userId));
        long size =  mongoTemplate.count(query, mainCollection);
        query.skip((skip-1)*(limit));
        query.limit(limit).with(Sort.by(Sort.Direction.DESC,"_id"));
        List<Audit> auditList =  mongoTemplate.find(query, Audit.class,mainCollection);
        int pages = (int) Math.ceil(size/limit);
        ListAudits listAudits = new ListAudits(auditList, size,pages);
        return listAudits;
    }

    @Override
    public ListAudits getAuditsByDate(String date, int p, int n){
        Query query = new Query(Criteria.where("principal").is(date)).with(Sort.by(Sort.Direction.DESC,"_id"))
                .skip((p-1)*n).limit(n);
        List<Audit> audits = mongoTemplate.find(query,Audit.class,mainCollection);
        return new ListAudits(audits, (int) mongoTemplate.count(query,mainCollection), (int) Math.ceil(audits.size()/n));
    }

    public List<Audit> analyze(String collection,int skip,int limit) {
        List<Audit> audits;
        Query query = new Query().skip(skip).limit(limit).with(Sort.by(Sort.Direction.ASC,"_id"));
        audits = mongoTemplate.find(query, Audit.class,collection);
        Collections.reverse(audits);
        return audits;
    }

}


