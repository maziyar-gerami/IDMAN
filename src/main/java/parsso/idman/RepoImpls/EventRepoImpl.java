package parsso.idman.RepoImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Event;
import parsso.idman.Models.ListEvents;
import parsso.idman.Models.Time;
import parsso.idman.Repos.EventRepo;
import parsso.idman.utils.Convertor.DateConverter;

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
    public List<Event> getListEvents()  {
        List<Event> events = analyze();
        return events;
    }

    @Override
    public ListEvents getListSizeEvents(int page, int n)  {
        List<Event> allEvents = getListEvents();
        return pagination(allEvents,page,n);
    }


    @Override
    public List<Event> getListEvents(int page, int number)  {
        return getListEvents();
    }

    @Override
    public ListEvents getListUserEvents(String userId, int page, int number)  {
        List<Event> events = analyze();
        List<Event> relatedEvents;
        relatedEvents = events.stream().filter(p -> p.getPrincipalId().equals(userId)).collect(Collectors.toList());
        return pagination(relatedEvents,page,number);
    }

    @Override
    public ListEvents getEventsByDate(String date, int page, int number) throws ParseException {
        List<Event> events = analyze();
        List<Event> relatedEvents = new LinkedList<>();
        int inDay = Integer.valueOf(date.substring(0, 2));
        int inMonth = Integer.valueOf(date.substring(2, 4));
        int inYear = Integer.valueOf(date.substring(4, 8));


        SimpleDateFormat newFormatter = new SimpleDateFormat("yyyy-MM-dd");

        for (Event event : events) {

            Date tempDate = newFormatter.parse(event.getCreationTime());

            Calendar myCal = new GregorianCalendar();
            myCal.setTime(tempDate);

            DateConverter dateConverter = new DateConverter();

            dateConverter.gregorianToPersian(myCal.get(Calendar.YEAR), myCal.get(Calendar.MONTH) + 1, myCal.get(Calendar.DAY_OF_MONTH));


            if (dateConverter.getYear() == inYear && dateConverter.getMonth() == inMonth && dateConverter.getDay() == inDay) {


                relatedEvents.add(event);

            }
        }
        return pagination(relatedEvents,page,number);
    }

    @Override
    public ListEvents getListUserEventByDate(String date, String userId, int page, int number) throws ParseException {
        List<Event> events = analyze();
        List<Event> relatedEvents = new LinkedList<>();
        int inJalaliDay = Integer.valueOf(date.substring(0, 2));
        int inJalaliMonth = Integer.valueOf(date.substring(2, 4));
        int inJalaliYear = Integer.valueOf(date.substring(4, 8));

        SimpleDateFormat newFormatter = new SimpleDateFormat("yyyy-MM-dd");

        for (Event event : events) {

            Date tempDate = newFormatter.parse(event.getCreationTime());

            Calendar myCal = new GregorianCalendar();
            myCal.setTime(tempDate);

            DateConverter dateConverter = new DateConverter();

            dateConverter.gregorianToPersian(myCal.get(Calendar.YEAR), myCal.get(Calendar.MONTH) + 1, myCal.get(Calendar.DAY_OF_MONTH));


            if (event.getPrincipalId().equals(userId)
                    &&dateConverter.getYear()==inJalaliYear&& dateConverter.getMonth()==inJalaliMonth && dateConverter.getDay()==inJalaliDay) {

                relatedEvents.add(event);
            }

        }

        return pagination(relatedEvents,page,number);

    }

    public List<Event> analyze(){

        List<Event> events;
        events = mongoTemplate.findAll(Event.class,collection);
        Collections.reverse(events);
        return events;
    }

    public ListEvents pagination(List<Event> events, int page, int number){
        int n = (page)*number;

        if (n>events.size())
            n = events.size();

        List<Event> relativeEvents= new LinkedList<>();

        int start = (page-1)*number;

        for (int i=start; i<n; i++)
            relativeEvents.add(events.get(i));

        return new ListEvents(events.size(),relativeEvents, (int) Math.ceil(events.size()/number));
    }
}


