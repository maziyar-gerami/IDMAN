package parsso.idman.RepoImpls.reports;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.TimeHelper;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.ListReports;
import parsso.idman.Models.Logs.Report;
import parsso.idman.Models.other.Time;
import parsso.idman.Repos.logs.reports.ReportRepo;

import java.text.ParseException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class ReportRepoImpl implements ReportRepo {
	private static final String mainCollection = Variables.col_log;
	ZoneId zoneId = ZoneId.of(Variables.ZONE);
	Instant instant = Instant.now(); //can be LocalDateTime
	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public ListReports getListSizeLogs(int p, int n) {

		List<Report> allReports = analyze(mainCollection, (p - 1) * n, n);
		long size = mongoTemplate.getCollection(mainCollection).countDocuments();
		for (Report report : allReports) {
			OffsetDateTime logDate = report.getDate().toInstant()
					.atOffset(zoneId.getRules().getOffset(instant));
			Time time1 = new Time(logDate.getYear(), logDate.getMonthValue(), logDate.getDayOfMonth(),
					logDate.getHour(), logDate.getMinute(), logDate.getSecond());
			report.setDateTime(time1);
		}
		return new ListReports(size, (int) Math.ceil((double) size / (double) n), allReports);
	}

	@Override
	public ListReports getListUserLogs(String userId, int p, int n) {
		Query query = new Query(Criteria.where("loggerName").is(userId))
				.with(Sort.by(Sort.Direction.DESC, "millis"));
		long size = mongoTemplate.count(query, Report.class, mainCollection);

		query.skip((p - 1) * (n)).limit(n);
		List<Report> reportList = mongoTemplate.find(query, Report.class, mainCollection);
		for (Report report : reportList) {
			OffsetDateTime logDate = report.getDate().toInstant()
					.atOffset(zoneId.getRules().getOffset(instant));
			Time time1 = new Time(logDate.getYear(), logDate.getMonthValue(), logDate.getDayOfMonth(),
					logDate.getHour(), logDate.getMinute(), logDate.getSecond());
			report.setDateTime(time1);
		}
		ListReports listReports = new ListReports(size, (int) Math.ceil(size / n), reportList);
		return listReports;
	}

	@Override
	public ListReports getLogsByDate(String date, int p, int n) throws ParseException {

		int skip = (p - 1) * n;
		int limit = n;

		Time time = new Time(Integer.parseInt(date.substring(4)),
				Integer.parseInt(date.substring(2, 4)),
				Integer.parseInt(date.substring(0, 2)));
		long[] range = TimeHelper.specificDateToEpochRange(time, zoneId);

		Query query = new Query(Criteria.where("millis").gte(range[0]).lte(range[1]));

		long size = mongoTemplate.find(query, Report.class, mainCollection).size();

		List<Report> reportList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "millis")).skip(skip).limit(limit), Report.class, mainCollection);

		ListReports listReports = new ListReports(size, (int) Math.ceil(size / limit), reportList);
		return listReports;
	}

	@Override
	public ListReports getListUserLogByDate(String date, String userId, int p, int n) throws ParseException {

		int skip = (p - 1) * n;
		int limit = n;

		Time time = new Time(Integer.parseInt(date.substring(4)),
				Integer.parseInt(date.substring(2, 4)),
				Integer.parseInt(date.substring(0, 2)));
		long[] range = TimeHelper.specificDateToEpochRange(time, zoneId);

		Query query = new Query(Criteria.where("millis").gte(range[0]).lte(range[1]).and("loggerName").is(userId));

		long size = mongoTemplate.find(query, Report.class, mainCollection).size();

		List<Report> reportList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "millis")).skip(skip).limit(limit), Report.class, mainCollection);

		return new ListReports(size, (int) Math.ceil(size / limit), reportList);
	}

	@Override
	public List<Report> analyze(String collection, int skip, int limit) {
		Query query = new Query().skip(skip).limit(limit).with(Sort.by(Sort.Direction.DESC, "millis"));
		List<Report> le = mongoTemplate.find(query, Report.class, collection);
		for (Report report : le) {
			OffsetDateTime logDate = report.getDate().toInstant()
					.atOffset(zoneId.getRules().getOffset(instant));
			Time time1 = new Time(logDate.getYear(), logDate.getMonthValue(), logDate.getDayOfMonth(),
					logDate.getHour(), logDate.getMinute(), logDate.getSecond());
			report.setDateTime(time1);
		}

		return le;
	}
}


