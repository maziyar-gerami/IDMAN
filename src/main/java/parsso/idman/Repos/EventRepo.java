package parsso.idman.Repos;

import parsso.idman.Models.Event;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

public interface EventRepo {

    List<Event> getListUserEvents() throws FileNotFoundException, ParseException;

    List<Event> analyze() throws FileNotFoundException, ParseException;

    List<Event> getListUserEvents(String userId) throws FileNotFoundException, ParseException;

    List<Event> getEventsByDate (String date) throws FileNotFoundException, ParseException;

    List<Event> getListUserEventByDate (String date, String userId) throws FileNotFoundException, ParseException;

}
