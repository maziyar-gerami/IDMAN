package parsso.idman.Repos;

import parsso.idman.Models.Event;
import parsso.idman.Models.ListEvents;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

public interface EventRepo {

    List<Event> getListEvents() throws FileNotFoundException, ParseException;

    ListEvents getListSizeEvents(int page, int n) ;

    List<Event> getListEvents(int page, int n) ;

    ListEvents getListUserEvents(String userId, int page, int n) ;

    ListEvents getEventsByDate(String date, int page, int n) throws ParseException;

    ListEvents getListUserEventByDate(String date, String userId, int page, int n) throws ParseException;

}
