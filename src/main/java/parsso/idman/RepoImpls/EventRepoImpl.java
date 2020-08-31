package parsso.idman.RepoImpls;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Event;
import parsso.idman.Models.EventsSubModel.Time;
import parsso.idman.Repos.EventRepo;
import parsso.idman.utils.Convertor.DateConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EventRepoImpl implements EventRepo {



    public static String path;



    @Value("${events.file.path}")
    public void setPath(String value){
        path = value;
    }

    public static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }


    @Override
    public List<Event> getListUserEvents() throws FileNotFoundException, ParseException {
        List<Event> events = analyze();
        return events;
    }

    @Override
    public List<Event> getListUserEvents(String userId) throws FileNotFoundException, ParseException {
        List<Event> events = analyze();
        List<Event> relatedEvents = new LinkedList<>();
        for (Event event : events)
            if (event!=null && event.getUserId()!=null && (event.getUserId().equals(userId))) relatedEvents.add(event);
        return relatedEvents;
    }

    @Override
    public List<Event> getEventsByDate(String date) throws FileNotFoundException, ParseException {
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


        for (Event event : events) {
            //Calendar calendar = toCalendar(event.getTime());
            int day = event.getTime().getDay();
            int month = event.getTime().getMonth();
            int year = event.getTime().getYear();

            if ((inGregorianDay == day) && (inGregorianMonth == month) && (inGregorianYear == year))
                relatedEvents.add(event);

        }
        return relatedEvents;
    }

    @Override
    public List<Event> getListUserEventByDate(String date, String userId) throws FileNotFoundException, ParseException {
        List<Event> events = analyze();
        List<Event> relatedEvents = new LinkedList<>();
        int inDay = Integer.valueOf(date.substring(0, 2));
        int inMonth = Integer.valueOf(date.substring(2, 4));
        int inYear = Integer.valueOf(date.substring(4, 8));

        for (Event event : events) {
            //Calendar calendar = toCalendar(event.getTimeStamp());
            int day = event.getTime().getDay();
            int month = event.getTime().getMonth();
            int year = event.getTime().getYear();

            DateConverter dc = new DateConverter();

            dc.persianToGregorian(inYear, inMonth, inDay);

            int inGregorianDay = dc.getDay();
            int inGregorianMonth = dc.getMonth();
            int inGregorianYear = dc.getYear();

            if ((inGregorianDay == day) && (inGregorianMonth == month) && (inGregorianYear == year) && (userId.equals(event.getUserId())))
                relatedEvents.add(event);

        }
        return relatedEvents;
    }

    public List<Event> analyze() throws FileNotFoundException, ParseException {

        List<Event> events = new LinkedList<>();
        File myfile = new File(path);
        Scanner scanner = new Scanner(myfile);
        StringBuffer data = new StringBuffer();
        String temp = String.valueOf(data);
        Event event = null;
        String[] s = temp.split("=============================================================");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("=============================================================")) {
                if (!(event == null) && event.getAction()!=null) events.add(event);
                event = new Event();
            }
            if (line.contains("WHO: ")) {
                temp = line.substring(line.indexOf(":") + 2);
                event.setUserId(temp);
            } else if (line.contains("WHAT: ")) {
                temp = line.substring(line.indexOf(":") + 2);
                event.setDetails(temp);
            } else if (line.contains("ACTION: ")) {
                temp = line.substring(line.indexOf(":") + 2);
                event.setAction(temp);
            } else if (line.contains("APPLICATION: ")) {
                temp = line.substring(line.indexOf(":") + 2);
                event.setApplication(temp);
            } else if (line.contains("WHEN: ")) {
                temp = line.substring(line.indexOf(":") + 2);
                SimpleDateFormat parserSDF = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
                Date date = parserSDF.parse(temp);

                Calendar myCal = new GregorianCalendar();
                myCal.setTime(date);

                Time time = new Time(
                        myCal.get(Calendar.YEAR),
                        myCal.get(Calendar.MONTH),
                        myCal.get(Calendar.DAY_OF_MONTH),

                        myCal.get(Calendar.HOUR_OF_DAY),
                        myCal.get(Calendar.MINUTE),
                        myCal.get(Calendar.SECOND)


                );



                event.setTime(time);






            } else if (line.contains("CLIENT IP ADDRESS: ")) {
                temp = line.substring(line.indexOf(":") + 2);
                event.setClientIP(temp);
            } else if (line.contains("SERVER IP ADDRESS: ")) {
                temp = line.substring(line.indexOf(":") + 2);
                event.setServerIP(temp);
            }
        }
        return events;
    }
}