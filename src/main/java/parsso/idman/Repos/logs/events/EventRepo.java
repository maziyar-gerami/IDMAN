package parsso.idman.Repos.logs.events;


import parsso.idman.Models.Logs.Event;
import parsso.idman.Models.Logs.ListEvents;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface EventRepo {

	ListEvents retrieve(String userId, String date, int p, int n) throws ParseException;

	ListEvents retrieveListSizeEvents(int page, int n) throws IOException, org.json.simple.parser.ParseException;

	ListEvents retrieveListUserEvents(String userId, int page, int n) throws IOException, org.json.simple.parser.ParseException;

	ListEvents getEventsByDate(String date, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

	ListEvents getListUserEventByDate(String date, String userId, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

	List<Event> analyze(int skip, int limit);
}
