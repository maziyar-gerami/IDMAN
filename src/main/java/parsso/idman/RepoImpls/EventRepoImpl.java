package parsso.idman.RepoImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Event;
import parsso.idman.Models.Time;
import parsso.idman.Repos.EventRepo;
import parsso.idman.utils.Convertor.DateConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventRepoImpl implements EventRepo {


    public static String path;
    public static String collection = "MongoDbCasEventRepository";
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<Event> getListUserEvents()  {
        List<Event> events = analyze();
        return events;
    }

    @Override
    public List<Event> getListUserEvents(String userId)  {
        List<Event> events = analyze();
        List<Event> relatedEvents;
        relatedEvents = events.stream().filter(p -> p.getPrincipalId().equals(userId)).collect(Collectors.toList());
        return relatedEvents;
    }

    @Override
    public List<Event> getEventsByDate(String date) throws  ParseException {
        List<Event> events = analyze();
        List<Event> relatedEvents = new LinkedList<>();
        int inDay = Integer.valueOf(date.substring(0, 2));
        int inMonth = Integer.valueOf(date.substring(2, 4));
        int inYear = Integer.valueOf(date.substring(4, 8));

        DateConverter dc = new DateConverter();

        dc.persianToGregorian(inYear, inMonth, inDay);

        int inGregorianDay = dc.getDay();
        int inGregorianMonth = dc.getMonth();
        int inGregorianYear = dc.getYear();

        SimpleDateFormat newFormatter = new SimpleDateFormat("yyyy-MM-dd");

        for (Event event : events) {

            Date tempDate = newFormatter.parse(event.getCreationTime());

            if (tempDate.getYear()==inGregorianYear&& tempDate.getMonth()==inGregorianMonth && tempDate.getDay()==inGregorianDay)
                relatedEvents.add(event);

        }
        return relatedEvents;
    }

    @Override
    public List<Event> getListUserEventByDate(String date, String userId) throws FileNotFoundException, ParseException {
        List<Event> events = analyze();
        List<Event> relatedEvents = new LinkedList<>();
        int inGregorianDay = Integer.valueOf(date.substring(0, 2));
        int inGregorianMonth = Integer.valueOf(date.substring(2, 4));
        int inGregorianYear = Integer.valueOf(date.substring(4, 8));

        SimpleDateFormat newFormatter = new SimpleDateFormat("yyyy-MM-dd");

        for (Event event : events) {

            Date tempDate = newFormatter.parse(event.getCreationTime());

            if (event.getPrincipalId().equals(userId)
                    &&tempDate.getYear()==inGregorianYear&& tempDate.getMonth()==inGregorianMonth && tempDate.getDay()==inGregorianDay)
                relatedEvents.add(event);

        }

        return relatedEvents;
    }

    public List<Event> analyze(){

        List<Event> events;
        events = mongoTemplate.findAll(Event.class,collection);
        Collections.reverse(events);
        return events;
    }
}