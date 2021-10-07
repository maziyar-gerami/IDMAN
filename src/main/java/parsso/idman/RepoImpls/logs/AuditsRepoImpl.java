package parsso.idman.RepoImpls.logs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.TimeHelper;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.Audit;
import parsso.idman.Models.Logs.Event;
import parsso.idman.Models.Logs.Report;
import parsso.idman.Models.other.Time;
import parsso.idman.Repos.logs.audits.AuditRepo;

import java.text.ParseException;
import java.time.ZoneId;
import java.util.List;

@Service
public class AuditsRepoImpl implements AuditRepo {
	final MongoTemplate mongoTemplate;

	@Autowired
	public AuditsRepoImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Audit.ListAudits retrieve(String userId, String date, int p, int n) throws ParseException {
		Query query = new Query();
		if (!userId.equals(""))
			query.addCriteria(Criteria.where("principal").is(userId));

		if (!date.equals("")){
			long[] range = TimeHelper.specificDateToEpochRange(TimeHelper.stringInputToTime(date), ZoneId.of(Variables.ZONE));
			query.addCriteria(Criteria.where("whenActionWasPerformed")
					.gte(TimeHelper.convertEpochToDate(range[0])).lte(TimeHelper.convertEpochToDate(range[1])));
		}


		long size = mongoTemplate.count(query, Event.class, Variables.col_audit);

		query.skip((p - 1) * n).limit(n).with(Sort.by(Sort.Direction.DESC, "_id"));
		List<Audit> audits =  mongoTemplate.find(query, Audit.class, Variables.col_audit);
		return new Audit.ListAudits(audits, size, (int) Math.ceil((double) size / (double) n));
	}

	@Override
	public Audit.ListAudits retrieveListSizeAudits(int p, int n) {
		List<Audit> allAudits = Audit.analyze( mongoTemplate,(p - 1) * n, n);
		long size = mongoTemplate.getCollection(Variables.col_audit).countDocuments();
		return new Audit.ListAudits(allAudits, size, (int) Math.ceil((double) size / (double) n));
	}

	@Override
	public Audit.ListAudits retrieveUserAudits(String userId, int p, int n) {
		Query query = new Query(Criteria.where("principal").is(userId))
				.with(Sort.by(Sort.Direction.DESC, "_id"));
		long size = mongoTemplate.count(query, Audit.class, Variables.col_audit);

		query.skip((p - 1) * (n)).limit(n);
		List<Audit> auditList = mongoTemplate.find(query, Audit.class, Variables.col_audit);
		return new Audit.ListAudits(auditList, size, (int) Math.ceil(size / (double) n));
	}

	@Override
	public Audit.ListAudits retrieveUserAuditsByDate(String date, String userId, int p, int limit) throws ParseException {

		int skip = (p - 1) * limit;

		Time time = new Time(Integer.parseInt(date.substring(4)),
				Integer.parseInt(date.substring(2, 4)),
				Integer.parseInt(date.substring(0, 2)));
		long[] range = TimeHelper.specificDateToEpochRange(time, ZoneId.of(Variables.ZONE));

		Query query = new Query(Criteria.where("whenActionWasPerformed")
				.gte(TimeHelper.convertEpochToDate(range[0])).lte(TimeHelper.convertEpochToDate(range[1])).and("principal").is(userId));

		long size = mongoTemplate.find(query, Report.class, Variables.col_audit).size();

		List<Audit> reportList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "whenActionWasPerformed"))
				.skip(skip).limit(limit), Audit.class, Variables.col_audit);

		return new Audit.ListAudits(reportList, size, (int) Math.ceil(size / (double) limit));

	}

	@Override
	public Audit.ListAudits retrieveAuditsByDate(String date, int p, int limit) throws ParseException {

		Time time = new Time(Integer.parseInt(date.substring(4)),
				Integer.parseInt(date.substring(2, 4)),
				Integer.parseInt(date.substring(0, 2)));
		long[] range = TimeHelper.specificDateToEpochRange(time, ZoneId.of(Variables.ZONE));

		Query query = new Query(Criteria.where("whenActionWasPerformed")
				.gte(TimeHelper.convertEpochToDate(range[0])).lte(TimeHelper.convertEpochToDate(range[1])));

		long size = mongoTemplate.find(query, Report.class, Variables.col_audit).size();

		List<Audit> reportList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC,
				"whenActionWasPerformed")).skip((p - 1) * limit).limit(limit), Audit.class, Variables.col_audit);

		return new Audit.ListAudits(reportList, size, (int) Math.ceil(size / (double) limit));

	}

}


