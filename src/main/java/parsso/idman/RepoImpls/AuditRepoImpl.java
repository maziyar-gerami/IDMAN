package parsso.idman.RepoImpls;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Audit;
import parsso.idman.Models.ListAudits;
import parsso.idman.Repos.AuditRepo;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.utils.Convertor.DateConverter;
import parsso.idman.utils.Query.QueryDomain;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    public List<Audit> getMainListAudits(){
        List<Audit> audits = analyze(mainCollection);
        return audits;
    }

    @Override
    public List<Audit> getMainListAudits(int page,int n){
        List<Audit> audits = analyze(mainCollection ,page*(n-1),n);
        return audits;
    }

    @Override
    public ListAudits getListSizeAudits(int p, int n){
        List<Audit> allAudits = getMainListAudits(p, n);
        Query query = new Query();
        long size = mongoTemplate.count(query,mainCollection);
        ListAudits listAudits = new ListAudits(allAudits,size, (int) Math.ceil(size/n));
        return listAudits;
    }


    @Override
    public List<Audit> getListAudits(int page, int n) {
        return getMainListAudits(page, n);
    }

    @Override
    public ListAudits getListUserAudits(String userId, int p, int n) {
        List<Audit> audits = getMainListAudits(p,n);
        audits = audits.stream().filter(q -> q.getPrincipal().equals(userId)).collect(Collectors.toList());
        Query query = new Query(Criteria.where("principal").is(userId));
        long size = mongoTemplate.count(query,mainCollection);
        return new ListAudits(audits,size, (int) Math.ceil(size/n));
    }

    @Override
    public ListAudits getAuditsByDate(String date, int p, int n) throws ParseException{
        List<Audit> audits = getMainListAudits(p,n);

        Query query = new Query();
        long size = mongoTemplate.count(query,mainCollection);
        return new ListAudits(audits,size, (int) Math.ceil(size/n));
    }


    @Override
    public ListAudits getListUserAuditByDate(String date, String userId, int p, int n) throws ParseException {
        List<Audit> audits = getMainListAudits(p,n);
        List<Audit> relatedAudits= iterateAudits(audits, date,userId);
        relatedAudits = relatedAudits.stream().filter(q -> q.getPrincipal().equals(userId)).collect(Collectors.toList());

        Query query = new Query();
        long size = mongoTemplate.count(query,mainCollection);
        return new ListAudits(relatedAudits,size, (int) Math.ceil(size/n));

    }

    private List<Audit> iterateAudits(List<Audit> audits, String date, String userId) throws ParseException {

        List<Audit> relatedAudits = new LinkedList<>();
        int inDay = Integer.valueOf(date.substring(0, 2));
        int inMonth = Integer.valueOf(date.substring(2, 4));
        int inYear = Integer.valueOf(date.substring(4, 8));

        SimpleDateFormat newFormatter = new SimpleDateFormat("yyyy-MM-dd");

        for (Audit audit : audits) {

            Date tempDate = audit.getWhenActionWasPerformed();

            Calendar myCal = new GregorianCalendar();
            myCal.setTime(tempDate);

            DateConverter dateConverter = new DateConverter();

            dateConverter.gregorianToPersian(myCal.get(Calendar.YEAR), myCal.get(Calendar.MONTH) + 1, myCal.get(Calendar.DAY_OF_MONTH));

            if (dateConverter.getYear() == inYear && dateConverter.getMonth() == inMonth && dateConverter.getDay() == inDay) {

                if (userId!=null) {

                    if (audit.getPrincipal().equals(userId))

                        relatedAudits.add(audit);
                }
                else
                    relatedAudits.add(audit);
            }
        }

        return relatedAudits;

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

    public List<Audit> analyze(String collection) {
        List<Audit> audits;
        audits = mongoTemplate.findAll(Audit.class,collection);
        Collections.reverse(audits);
        return audits;
    }
}


