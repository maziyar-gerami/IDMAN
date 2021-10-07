package parsso.idman.RepoImpls.logs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.TimeHelper;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.*;
import parsso.idman.Models.other.Time;
import parsso.idman.Repos.logs.reports.ReportRepo;

import java.text.ParseException;
import java.time.ZoneId;
import java.util.List;

@Service
public class ReportsRepoImpl implements ReportRepo {
	//can be LocalDateTime
	final MongoTemplate mongoTemplate;

	@Override
	public Report.ListReports retrieve(String userId, String date, int p, int n) throws ParseException {
		Query query = new Query();
		if (!userId.equals(""))
			query.addCriteria(Criteria.where("doerID").is(userId));

		if (!date.equals("")){
			long[] range = TimeHelper.specificDateToEpochRange(TimeHelper.stringInputToTime(date), ZoneId.of(Variables.ZONE));
			query.addCriteria(Criteria.where("millis")
					.gte(range[0]).lte(range[1]));
		}

		long size = mongoTemplate.count(query, Report.class, Variables.col_idmanLog);

		query.skip((p - 1) * n).limit(n).with(Sort.by(Sort.Direction.DESC, "millis"));

		List<Report> reports = mongoTemplate.find(query, Report.class,  Variables.col_idmanLog);

		return new Report.ListReports(reports, size, (int) Math.ceil( size / (float) n));
	}


	@Autowired
	public ReportsRepoImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Report.ListReports getListSizeLogs(int p, int n) {

		List<Report> allReports = Report.analyze(mongoTemplate, (p - 1) * n, n);
		long size = mongoTemplate.getCollection(Variables.col_idmanLog).countDocuments();
		return new Report.ListReports(size, (int) Math.ceil((double) size / (double) n), TimeHelper.reportSetDate(allReports));
	}

	@Override
	public Report.ListReports getListUserLogs(String userId, int p, int n) {
		Query query = new Query(Criteria.where("loggerName").is(userId))
				.with(Sort.by(Sort.Direction.DESC, "millis"));
		long size = mongoTemplate.count(query, Report.class, Variables.col_idmanLog);

		query.skip((p - 1) * (n)).limit(n);
		List<Report> reportList = mongoTemplate.find(query, Report.class, Variables.col_idmanLog);
		return new Report.ListReports(size, (int) Math.ceil(size / (float) n), TimeHelper.reportSetDate(reportList));
	}

	@Override
	public Report.ListReports getLogsByDate(String date, int p, int n) throws ParseException {

		int skip = getSkip(p, n);

		long[] range = TimeHelper.specificDateToEpochRange(TimeHelper.stringInputToTime(date), ZoneId.of(Variables.ZONE));

		Query query = new Query(Criteria.where("millis").gte(range[0]).lte(range[1]));

		return getListReports(n, skip, query);
	}

	@Override
	public Report.ListReports getListUserLogByDate(String date, String userId, int p, int n) throws ParseException {

		long[] range = TimeHelper.specificDateToEpochRange(TimeHelper.stringInputToTime(date), ZoneId.of(Variables.ZONE));

		Query query = new Query(Criteria.where("millis").gte(range[0]).lte(range[1]).and("loggerName").is(userId));

		return getListReports(n, getSkip(p, n), query);
	}

	private int getSkip(int p, int n) {
		return (p - 1) * n;
	}

	private Report.ListReports getListReports(int n, int skip, Query query) {
		long size = mongoTemplate.find(query, Report.class, Variables.col_idmanLog).size();

		List<Report> reportList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "millis")).skip(skip).limit(n), Report.class, Variables.col_idmanLog);

		return new Report.ListReports(size, (int) Math.ceil(size / (float) n), reportList);
	}

	@Override
	public List<ReportMessage> accessManaging(int page, int nRows, long id, String date, String doerId, String instanceName) throws java.text.ParseException {
		int skip;

		Query query = new Query(Criteria.where("attribute").is("Access Strategy"));
		if(id != 0)
			query.addCriteria(Criteria.where("instance").is(id));
		if(!doerId.equals(""))
			query.addCriteria(Criteria.where("doerID").is(doerId));
		if (!instanceName.equals(""))
			query.addCriteria(Criteria.where("instanceName").is(instanceName));
		if (!date.equals("")){
			Time time = new Time(Integer.parseInt(date.substring(4)),
					Integer.parseInt(date.substring(2, 4)),
					Integer.parseInt(date.substring(0, 2)));
			long[] range = TimeHelper.specificDateToEpochRange(time, ZoneId.of(Variables.ZONE));
			query.addCriteria(Criteria.where("millis").gte(range[0]).lte(range[1]));
		}
		if (page!=0 && nRows !=0) {
			skip = (page - 1) * nRows;
			query.skip(skip).limit(nRows);
		}

		return mongoTemplate.find(query, ReportMessage.class, Variables.col_idmanLog);
	}
}


