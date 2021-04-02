package parsso.idman.RepoImpls;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Logs.Event;
import parsso.idman.Models.Logs.ListEvents;
import parsso.idman.Models.Time;
import parsso.idman.Repos.EventRepo;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class EventRepoImpl implements EventRepo {

    private static final String mainCollection = "MongoDbCasEventRepository";
    ZoneId zoneId = ZoneId.of("UTC+03:30");


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
        return new ListEvents(size, (int) Math.ceil(size / n), allEvents);
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
    public ListEvents getEventsByDate(String date, int p, int n) {


        String time = new Time(Integer.valueOf(date.substring(4)), Integer.valueOf(date.substring(2, 4)), Integer.valueOf(date.substring(0, 2))).toStringDate();
        String timeStart = time + "T00:00:00.000000" + zoneId.toString().substring(3);
        String timeEnd = time + "T23:59:59.000000" + zoneId.toString().substring(3);

        long eventStartDate = OffsetDateTime.parse(timeStart).atZoneSameInstant(zoneId).toEpochSecond() * 1000;
        long eventEndDate = OffsetDateTime.parse(timeEnd).atZoneSameInstant(zoneId).toEpochSecond() * 1000;

        Query query = new Query(Criteria.where("_id").gte(eventStartDate).lte(eventEndDate));
        List<Event> allEvents = mongoTemplate.find(query, Event.class, mainCollection);

        for (Event event : allEvents) {
            ZonedDateTime eventDate = OffsetDateTime.parse(event.getCreationTime()).atZoneSameInstant(zoneId);
            Time time1 = new Time(eventDate.getYear(), eventDate.getMonthValue(), eventDate.getDayOfMonth(),
                    eventDate.getHour(), eventDate.getMinute(), eventDate.getSecond());
            event.setTime(time1);
        }


        long size = mongoTemplate.count(query, Event.class, mainCollection);

        return new ListEvents(size, (int) Math.ceil(size / n), allEvents);
    }

    @Override
    public ListEvents getListUserEventByDate(String date, String userId, int skip, int limit) {

        String time = new Time(Integer.valueOf(date.substring(4)), Integer.valueOf(date.substring(2, 4)), Integer.valueOf(date.substring(0, 2))).toStringDate();
        String timeStart = time + "T00:00:00.000000" + zoneId.toString().substring(3);
        String timeEnd = time + "T23:59:59.000000" + zoneId.toString().substring(3);

        long eventStartDate = OffsetDateTime.parse(timeStart).atZoneSameInstant(zoneId).toEpochSecond() * 1000;
        long eventEndDate = OffsetDateTime.parse(timeEnd).atZoneSameInstant(zoneId).toEpochSecond() * 1000;
        Query query = new Query(Criteria.where("principalId").is(userId).and("_id").gte(eventStartDate).lte(eventEndDate));
        long size = mongoTemplate.count(query, ListEvents.class, mainCollection);
        query.skip((skip - 1) * (limit)).limit(limit).with(Sort.by(Sort.Direction.DESC, "_id"));
        List<Event> eventList = mongoTemplate.find(query, Event.class, mainCollection);

        for (Event event : eventList) {
            ZonedDateTime eventDate = OffsetDateTime.parse(event.getCreationTime()).atZoneSameInstant(zoneId);
            Time time1 = new Time(eventDate.getYear(), eventDate.getMonthValue(), eventDate.getDayOfMonth(),
                    eventDate.getHour(), eventDate.getMinute(), eventDate.getSecond());
            event.setTime(time1);
        }

        int pages = (int) Math.ceil(size / limit) + 1;
        ListEvents listEvents = new ListEvents(size, pages, eventList);
        return listEvents;
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


