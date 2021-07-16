package parsso.idman.RepoImpls;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.Event;
import parsso.idman.Models.Logs.ListEvents;
import parsso.idman.Models.Logs.Report;
import parsso.idman.Models.Time;
import parsso.idman.Repos.EventRepo;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class EventRepoImpl implements EventRepo {

    private static final String mainCollection = "MongoDbCasEventRepository";
    ZoneId zoneId = ZoneId.of(Variables.ZONE);


    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ListEvents getListSizeEvents(int p, int n) {
        List<Event> allEvents = analyze(mainCollection, (p - 1) * n, n);
        long size = mongoTemplate.getCollection(mainCollection).countDocuments();
        for (Event event : allEvents) {
            ZonedDateTime eventDate = OffsetDateTime.parse(event.getCreationTime()).atZoneSameInstant(zoneId);
            Time time1 = new Time(eventDate.getYear(), eventDate.getMonthValue(), eventDate.getDayOfMonth(),
                    eventDate.getHour(), eventDate.getMinute(), eventDate.getSecond());
            event.setTime(time1);
        }
        return new ListEvents(size, (int) Math.ceil((double)size /(double) n), allEvents);
    }

    @Override
    public ListEvents getListUserEvents(String userId, int p, int n) {
        Query query = new Query(Criteria.where("principalId").is(userId))
                .with(Sort.by(Sort.Direction.DESC, "_id"));
        long size = mongoTemplate.count(query, Event.class, mainCollection);

        query.skip((p - 1) * (n)).limit(n);
        List<Event> eventList = mongoTemplate.find(query, Event.class, mainCollection);
        for (Event event : eventList) {
            ZonedDateTime eventDate = OffsetDateTime.parse(event.getCreationTime()).atZoneSameInstant(zoneId);
            Time time1 = new Time(eventDate.getYear(), eventDate.getMonthValue(), eventDate.getDayOfMonth(),
                    eventDate.getHour(), eventDate.getMinute(), eventDate.getSecond());
            event.setTime(time1);
        }
        ListEvents listEvents = new ListEvents(size, (int) Math.ceil(size / n), eventList);
        return listEvents;
    }

    @Override
    public ListEvents getEventsByDate(String date, int p, int n) throws ParseException {


        int skip = (p-1)*n;
        int limit = n;

        Time time = new Time(Integer.valueOf(date.substring(4)),
                Integer.valueOf(date.substring(2, 4)),
                Integer.valueOf(date.substring(0, 2)));
        long [] range = Time.specificDateToEpochRange(time, zoneId);

        Query query = new Query(Criteria.where("_id").gte(range[0]).lte(range[1]));

        long size = mongoTemplate.find(query, Event.class,  mainCollection).size();

        List<Event> reportList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "_id")).skip(skip).limit(limit),Event.class, mainCollection);

        ListEvents listReports = new ListEvents(size, (int) Math.ceil(size / limit), reportList);
        return listReports;
    }

    @Override
    public ListEvents getListUserEventByDate(String date, String userId, int p, int n) throws ParseException {

        int skip = (p-1)*n;
        int limit = n;

        Time time = new Time(Integer.valueOf(date.substring(4)),
                Integer.valueOf(date.substring(2, 4)),
                Integer.valueOf(date.substring(0, 2)));
        long [] range = Time.specificDateToEpochRange(time, zoneId);

        Query query = new Query(Criteria.where("_id").gte(range[0]).lte(range[1]).and("principalId").is(userId));

        long size = mongoTemplate.find(query, Report.class,  mainCollection).size();

        List<Event> reportList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "_id")).skip(skip).limit(limit),Event.class, mainCollection);

        return new ListEvents(size, (int) Math.ceil(size / limit), reportList);
    }

    @Override
    public List<Event> analyze(String collection, int skip, int limit) {
        Query query = new Query().skip(skip).limit(limit).with(Sort.by(Sort.Direction.DESC, "_id"));
        List<Event> le = mongoTemplate.find(query, Event.class, collection);
        for (Event event : le) {
            ZonedDateTime eventDate = OffsetDateTime.parse(event.getCreationTime()).atZoneSameInstant(zoneId);
            Time time1 = new Time(eventDate.getYear(), eventDate.getMonthValue(), eventDate.getDayOfMonth(),
                    eventDate.getHour(), eventDate.getMinute(), eventDate.getSecond());
            event.setTime(time1);
        }

        return le;
    }
}


