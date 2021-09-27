package parsso.idman.Repos.logs.events;


import parsso.idman.Models.Logs.Event;
import parsso.idman.Models.Logs.ListEvents;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface EventRepo {
	ListEvents getListSizeEvents(int page, int n) throws IOException, org.json.simple.parser.ParseException;

	ListEvents getListUserEvents(String userId, int page, int n) throws IOException, org.json.simple.parser.ParseException;

	ListEvents getEventsByDate(String date, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

	ListEvents getListUserEventByDate(String date, String userId, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

	List<Event> analyze(String collection, int skip, int limit);
}
