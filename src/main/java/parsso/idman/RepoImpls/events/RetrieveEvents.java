package parsso.idman.RepoImpls.events;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.TimeHelper;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.Event;
import parsso.idman.Models.Logs.ListEvents;
import parsso.idman.Models.Logs.Report;
import parsso.idman.Models.other.Time;
import parsso.idman.Repos.logs.events.EventRepo;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class RetrieveEvents implements EventRepo {
	MongoTemplate mongoTemplate;
	@Autowired
	public RetrieveEvents(MongoTemplate mongoTemplate){
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public ListEvents getListSizeEvents(int p, int n) {
		List<Event> allEvents = analyze(Variables.col_events, (p - 1) * n, n);
		long size = mongoTemplate.getCollection(Variables.col_casEvent).countDocuments();

		return new ListEvents(size, (int) Math.ceil((double) size / (double) n), eventsSetTime(allEvents));
	}

	@Override
	public ListEvents getListUserEvents(String userId, int p, int n) {
		Query query = new Query(Criteria.where("principalId").is(userId))
				.with(Sort.by(Sort.Direction.DESC, "_id"));
		long size = mongoTemplate.count(query, Event.class, Variables.col_casEvent);

		query.skip((p - 1) * (n)).limit(n);
		List<Event> le = mongoTemplate.find(query, Event.class);

		return new ListEvents(size, (int) Math.ceil( size / (float) n), eventsSetTime(le));
	}

	@Override
	public ListEvents getEventsByDate(String date, int p, int n) throws ParseException {

		int skip = calculateSkip(p,n);

		Time time = new Time(Integer.parseInt(date.substring(4)),
				Integer.parseInt(date.substring(2, 4)),
				Integer.parseInt(date.substring(0, 2)));
		long[] range = TimeHelper.specificDateToEpochRange(time, ZoneId.of(Variables.ZONE));

		Query query = new Query(Criteria.where("_id").gte(range[0]).lte(range[1]));

		long size = mongoTemplate.find(query, Event.class, Variables.col_casEvent).size();

		List<Event> reportList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "_id")).skip(skip).limit(n), Event.class, Variables.col_casEvent);

		return new ListEvents(size, (int) Math.ceil(size /(float) n), reportList);
	}

	private int calculateSkip(int p, int n) {
		return (p - 1) * n;
	}

	@Override
	public ListEvents getListUserEventByDate(String date, String userId, int p, int n) throws ParseException {

		int skip = calculateSkip(p,n);

		Time time = new Time(Integer.parseInt(date.substring(4)),
				Integer.parseInt(date.substring(2, 4)),
				Integer.parseInt(date.substring(0, 2)));
		long[] range = TimeHelper.specificDateToEpochRange(time, ZoneId.of(Variables.ZONE));

		Query query = new Query(Criteria.where("_id").gte(range[0]).lte(range[1]).and("principalId").is(userId));

		long size = mongoTemplate.find(query, Report.class, Variables.col_casEvent).size();

		List<Event> reportList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "_id")).skip(skip).limit(n), Event.class, Variables.col_casEvent);

		return new ListEvents(size, (int) Math.ceil(size / (float) n), reportList);
	}

	@Override
	public List<Event> analyze(String collection, int skip, int limit) {
		Query query = new Query().skip(skip).limit(limit).with(Sort.by(Sort.Direction.DESC, "_id"));
		List<Event> le = mongoTemplate.find(query, Event.class);


		return eventsSetTime(le);
	}

	private List<Event> eventsSetTime(List<Event> le){
		for (Event event : le) {
			ZonedDateTime eventDate = OffsetDateTime.parse(event.getCreationTime()).atZoneSameInstant(ZoneId.of(Variables.ZONE));
			Time time1 = new Time(eventDate.getYear(), eventDate.getMonthValue(), eventDate.getDayOfMonth(),
					eventDate.getHour(), eventDate.getMinute(), eventDate.getSecond());
			event.setTime(time1);
		}
		return le;
	}
}


