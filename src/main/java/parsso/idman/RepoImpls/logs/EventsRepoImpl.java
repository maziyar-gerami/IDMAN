package parsso.idman.RepoImpls.logs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.TimeHelper;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.Event;
import parsso.idman.Models.Logs.Report;
import parsso.idman.Models.other.Time;
import parsso.idman.repos.LogsRepo;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class EventsRepoImpl implements LogsRepo.EventRepo {
	final MongoTemplate mongoTemplate;

	@Override
	public Event.ListEvents retrieve(String userId, String date, int p, int n) throws ParseException {
		Query query = new Query();
		if (!userId.equals(""))
			query.addCriteria(Criteria.where("principalId").is(userId));

		if (!date.equals("")){
			long[] range = TimeHelper.specificDateToEpochRange(TimeHelper.stringInputToTime(date), ZoneId.of(Variables.ZONE));
			query.addCriteria(Criteria.where("_id")
					.gte(range[0]).lte(range[1]));
		}



		long size = mongoTemplate.count(query, Event.class, Variables.col_casEvent);

		query.skip((long) (p - 1) * n).limit(n).with(Sort.by(Sort.Direction.DESC, "_id"));

		List<Event> events =  mongoTemplate.find(query, Event.class, Variables.col_casEvent);

		return new Event.ListEvents(events, size, (int) Math.ceil( size / (float) n));
	}


	@Autowired
	public EventsRepoImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Event.ListEvents retrieveListSizeEvents(int p, int n) {
		List<Event> allEvents = analyze((p - 1) * n, n);
		long size = mongoTemplate.getCollection(Variables.col_casEvent).countDocuments();

		return new Event.ListEvents(size, (int) Math.ceil((double) size / (double) n), eventsSetTime(allEvents));
	}

	@Override
	public List<Event> analyze(int skip, int limit) {
		Query query = new Query().skip(skip).limit(limit).with(Sort.by(Sort.Direction.DESC, "_id"));
		List<Event> le = mongoTemplate.find(query, Event.class, Variables.col_casEvent);
		return eventsSetTime(le);
	}

	private List<Event> eventsSetTime(List<Event> le) {
		for (Event event : le) {
			ZonedDateTime eventDate = OffsetDateTime.parse(event.getCreationTime()).atZoneSameInstant(ZoneId.of(Variables.ZONE));
			Time time1 = new Time(eventDate.getYear(), eventDate.getMonthValue(), eventDate.getDayOfMonth(),
					eventDate.getHour(), eventDate.getMinute(), eventDate.getSecond());
			event.setTime(time1);
		}
		return le;
	}
}


