package parsso.idman.repoImpls.logs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.Report;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.Time;
import parsso.idman.repos.LogsRepo;

import java.time.ZoneId;
import java.util.List;

@Service
public class ReportsRepoImpl implements LogsRepo.ReportRepo {
    //can be LocalDateTime
    final MongoTemplate mongoTemplate;

    @Autowired
    public ReportsRepoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Report.ListReports retrieve(String userId, String date, int p, int n) {
        Query query = new Query();
        if (!userId.equals(""))
            query.addCriteria(Criteria.where("loggerName").is(userId));

        if (!date.equals("")) {
            long[] range = new Time().specificDateToEpochRange(new Time().stringInputToTime(date), ZoneId.of(Variables.ZONE));
            query.addCriteria(Criteria.where("millis")
                    .gte(range[0]).lte(range[1]));
        }

        long size = mongoTemplate.count(query, Report.class, Variables.col_Log);

        query.skip((long) (p - 1) * n).limit(n).with(Sort.by(Sort.Direction.DESC, "millis"));

        List<Report> reports = mongoTemplate.find(query, Report.class, Variables.col_Log);

        return new Report.ListReports(reports, size, (int) Math.ceil(size / (float) n));
    }

    @SuppressWarnings("unused")
    private int getSkip(int p, int n) {
        return (p - 1) * n;
    }

    @SuppressWarnings("unused")
    private Report.ListReports getListReports(int n, int skip, Query query) {
        long size = mongoTemplate.find(query, Report.class, Variables.col_Log).size();

        List<Report> reportList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "millis")).skip(skip).limit(n), Report.class, Variables.col_Log);

        return new Report.ListReports(size, (int) Math.ceil(size / (float) n), reportList);
    }

    @Override
    public ReportMessage.ListReportMessage accessManaging(int page, int nRows, long id, String date, String doerId, String instanceName) {
        int skip = 0;

        Query query = new Query(Criteria.where("attribute").is("Access Strategy"));
        if (id != 0)
            query.addCriteria(Criteria.where("instance").is(id));
        if (!doerId.equals(""))
            query.addCriteria(Criteria.where("doerID").is(doerId));
        if (!instanceName.equals(""))
            query.addCriteria(Criteria.where("instanceName").is(instanceName));
        if (!date.equals("")) {
            Time time = new Time(Integer.parseInt(date.substring(4)),
                    Integer.parseInt(date.substring(2, 4)),
                    Integer.parseInt(date.substring(0, 2)));
            long[] range = new Time().specificDateToEpochRange(time, ZoneId.of(Variables.ZONE));
            query.addCriteria(Criteria.where("millis").gte(range[0]).lte(range[1]));
        }
        if (page != 0 && nRows != 0) {
            skip = (page - 1) * nRows;
        }

        int size = (int) mongoTemplate.count(query, ReportMessage.class, Variables.col_idmanLog);

        List<ReportMessage> reportMessageList = mongoTemplate.find(query.skip(skip).limit(nRows), ReportMessage.class, Variables.col_idmanLog);

        return new ReportMessage.ListReportMessage(size, (int) Math.ceil(size / (float) nRows), reportMessageList);

    }
}


