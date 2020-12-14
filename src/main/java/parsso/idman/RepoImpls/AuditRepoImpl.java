package parsso.idman.RepoImpls;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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


    public static String path;
    public static String mainCollection = "MongoDbCasAuditRepository";

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    QueryDomain queryDomain;
    @Autowired
    ServiceRepo serviceRepo;

    @Override
    public List<Audit> getMainListAudits() throws IOException, org.json.simple.parser.ParseException {
        List<Audit> audits = analyze(mainCollection);
        return audits;
    }

    @Override
    public ListAudits getListSizeAudits(int page, int n) throws IOException, org.json.simple.parser.ParseException {
        List<Audit> allAudits = getMainListAudits();
        return pagination(allAudits,page,n);
    }


    @Override
    public List<Audit> getListAudits(int page, int number) throws IOException, org.json.simple.parser.ParseException {
        return getMainListAudits();
    }

    @Override
    public ListAudits getListUserAudits(String userId, int page, int number) throws IOException, org.json.simple.parser.ParseException {
        List<Audit> audits = getMainListAudits();
        List<Audit> relatedAudits;
        relatedAudits = audits.stream().filter(p -> p.getPrincipal().equals(userId)).collect(Collectors.toList());
        return pagination(relatedAudits,page,number);
    }

    @Override
    public ListAudits getAuditsByDate(String date, int page, int number) throws ParseException, IOException, org.json.simple.parser.ParseException {
        List<Audit> audits = getMainListAudits();

        return pagination(iterateAudits(audits, date,null),page,number);
    }


    @Override
    public ListAudits getListUserAuditByDate(String date, String userId, int page, int number) throws ParseException, IOException, org.json.simple.parser.ParseException {
        List<Audit> audits = getMainListAudits();
        List<Audit> relatedAudits= iterateAudits(audits, date,userId);
        relatedAudits = relatedAudits.stream().filter(p -> p.getPrincipal().equals(userId)).collect(Collectors.toList());

        return pagination(relatedAudits,page,number);

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

    public List<Audit> analyze(String collection) {

        List<Audit> audits;
        audits = mongoTemplate.findAll(Audit.class,collection);
        Collections.reverse(audits);
        return audits;
    }

    public ListAudits pagination(List<Audit> audits, int page, int number){
        int n = (page)*number;

        if (n>audits.size())
            n = audits.size();

        List<Audit> relativeAudits= new LinkedList<>();

        int start = (page-1)*number;

        for (int i=start; i<n; i++)
            relativeAudits.add(audits.get(i));

        return new ListAudits(audits.size(),relativeAudits, (int) Math.ceil(audits.size()/number));
    }
}


