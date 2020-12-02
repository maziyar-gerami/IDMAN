package parsso.idman.Repos;

import parsso.idman.Models.Event;
import parsso.idman.Models.ListEvents;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface EventRepo {

    List<Event> getMainListEvents() throws ParseException, IOException, org.json.simple.parser.ParseException;

    List<Event> getSecondaryListEvents() throws ParseException, IOException, org.json.simple.parser.ParseException;

    ListEvents getListSizeEvents(int page, int n) throws IOException, org.json.simple.parser.ParseException;

    List<Event> getListEvents(int page, int n) throws IOException, org.json.simple.parser.ParseException;

    ListEvents getListUserEvents(String userId, int page, int n) throws IOException, org.json.simple.parser.ParseException;

    ListEvents getEventsByDate(String date, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

    ListEvents getListUserEventByDate(String date, String userId, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

}
