package parsso.idman.RepoImpls.logs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.TimeHelper;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.Report;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.other.Time;
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
            query.addCriteria(Criteria.where("doerID").is(userId));

        if (!date.equals("")) {
            long[] range = TimeHelper.specificDateToEpochRange(TimeHelper.stringInputToTime(date), ZoneId.of(Variables.ZONE));
            query.addCriteria(Criteria.where("millis")
                    .gte(range[0]).lte(range[1]));
        }

        long size = mongoTemplate.count(query, Report.class, Variables.col_Log);

        query.skip((long) (p - 1) * n).limit(n).with(Sort.by(Sort.Direction.DESC, "millis"));

        List<Report> reports = mongoTemplate.find(query, Report.class, Variables.col_Log);

        return new Report.ListReports(reports, size, (int) Math.ceil(size / (float) n));
    }

    private int getSkip(int p, int n) {
        return (p - 1) * n;
    }

    private Report.ListReports getListReports(int n, int skip, Query query) {
        long size = mongoTemplate.find(query, Report.class, Variables.col_Log).size();

        List<Report> reportList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "millis")).skip(skip).limit(n), Report.class, Variables.col_Log);

        return new Report.ListReports(size, (int) Math.ceil(size / (float) n), reportList);
    }

    @Override
    public List<ReportMessage> accessManaging(int page, int nRows, long id, String date, String doerId, String instanceName) {
        int skip;

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
            long[] range = TimeHelper.specificDateToEpochRange(time, ZoneId.of(Variables.ZONE));
            query.addCriteria(Criteria.where("millis").gte(range[0]).lte(range[1]));
        }
        if (page != 0 && nRows != 0) {
            skip = (page - 1) * nRows;
            query.skip(skip).limit(nRows);
        }

        return mongoTemplate.find(query, ReportMessage.class, Variables.col_Log);
    }
}


