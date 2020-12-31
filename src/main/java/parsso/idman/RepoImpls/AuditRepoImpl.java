package parsso.idman.RepoImpls;
import org.springframework.beans.factory.annotation.Autowired;
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
        p= inverseP(p,n);
        List<Audit> allAudits = analyze(mainCollection,(p-1)*n,n);
        Query query = new Query();
        long size = mongoTemplate.count(query,mainCollection);
        return new ListAudits(allAudits,size, (int) Math.ceil(size/n));
    }

    private int inverseP(int p,int n){
        long size =  mongoTemplate.getCollection(mainCollection).countDocuments();
        int pages = (int) Math.ceil(size/n);
        return pages-(p-1);
    }

    @Override
    public ListAudits getListUserAudits(String userId, int skip, int limit) {
        Query query = new Query(Criteria.where("principal").is(userId));
        long size =  mongoTemplate.count(query, mainCollection);
        query.skip((skip-1)*(limit));
        query.limit(limit);
        List<Audit> auditList =  mongoTemplate.find(query, Audit.class,mainCollection);
        int pages = (int) Math.ceil(size/limit);
        ListAudits listAudits = new ListAudits(auditList, size,pages);
        return listAudits;
    }
    @Override
    public ListAudits getListUserAuditByDate(String date, String userId, int skip, int limit) throws ParseException {

        Query query = new Query(Criteria.where("principal").is(userId));
        long size =  mongoTemplate.count(query, mainCollection);
        query.skip((skip-1)*(limit));
        query.limit(limit);
        List<Audit> auditList =  mongoTemplate.find(query, Audit.class,mainCollection);
        int pages = (int) Math.ceil(size/limit);
        ListAudits listAudits = new ListAudits(auditList, size,pages);
        return listAudits;
    }

    @Override
    public ListAudits getAuditsByDate(String date, int p, int n) throws ParseException{
        p= inverseP(p,n);
        List<Audit> audits = analyze(mainCollection,n*(p-1),n);
        ListAudits listAudits = new ListAudits();
        Query query = new Query(Criteria.where("principal").is(date));
        listAudits.setSize((int) mongoTemplate.count(query,mainCollection));
        listAudits.setPages((int) Math.ceil(listAudits.getSize()/n));
        listAudits.setAuditList(audits);
        return listAudits;
    }




    public List<Audit> analyze(String collection,int skip,int limit) {
        List<Audit> audits;
        Query query = new Query();
        query.skip(skip);
        query.limit(limit);
        audits = mongoTemplate.find(query, Audit.class,collection);
        Collections.reverse(audits);
        return audits;
    }

}


