package parsso.idman.Repos.logs.events;


import parsso.idman.Models.Logs.Event;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface EventRepo {

	Event.ListEvents retrieve(String userId, String date, int p, int n) throws ParseException;

	Event.ListEvents retrieveListSizeEvents(int page, int n) throws IOException, org.json.simple.parser.ParseException;

	Event.ListEvents retrieveListUserEvents(String userId, int page, int n) throws IOException, org.json.simple.parser.ParseException;

	Event.ListEvents getEventsByDate(String date, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

	Event.ListEvents getListUserEventByDate(String date, String userId, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

	List<Event> analyze(int skip, int limit);
}
