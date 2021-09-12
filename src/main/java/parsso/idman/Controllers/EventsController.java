package parsso.idman.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import parsso.idman.Helpers.Events.EventsExcelView;
import parsso.idman.Models.Logs.ListEvents;
import parsso.idman.Repos.EventRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Controller
public class EventsController {
	@Autowired
	private EventRepo eventRepo;
	@Autowired
	private EventsExcelView eventsExcelView;

	//************************************* Pages ****************************************

	@SuppressWarnings("SameReturnValue")
	@GetMapping("/events")
	public String getPageEvents() {
		return "events";
	}

	//************************************* APIs ****************************************

	@GetMapping("/api/events/users/{page}/{n}")
	public ResponseEntity<ListEvents> retrieveAllEvents(@PathVariable("page") int page, @PathVariable("n") int n) throws IOException, org.json.simple.parser.ParseException {
		return new ResponseEntity<>(eventRepo.getListSizeEvents(page, n), HttpStatus.OK);
	}

	@GetMapping("/api/events/users/{userId}/{page}/{n}")
	public ResponseEntity<ListEvents> retrieveByUser(@PathVariable String userId, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, org.json.simple.parser.ParseException {
		return new ResponseEntity<>(eventRepo.getListUserEvents(userId, page, n), HttpStatus.OK);
	}

	@GetMapping("/api/events/users/date/{date}/{page}/{n}")
	public ResponseEntity<ListEvents> retrieveByDate(@PathVariable String date, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, ParseException, org.json.simple.parser.ParseException {
		return new ResponseEntity<>(eventRepo.getEventsByDate(date, page, n), HttpStatus.OK);
	}

	@GetMapping("/api/events/users/{id}/date/{date}/{page}/{n}")
	public ResponseEntity<ListEvents> retrieveByUserDate(@PathVariable String id, @PathVariable String date, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, ParseException, org.json.simple.parser.ParseException {
		return new ResponseEntity<>(eventRepo.getListUserEventByDate(date, id, page, n), HttpStatus.OK);
	}

	@GetMapping("/api/events/user/{page}/{n}")
	public ResponseEntity<ListEvents> retrieveCurrentUserEvents(HttpServletRequest request, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, org.json.simple.parser.ParseException {
		ListEvents listEvents = eventRepo.getListUserEvents(request.getUserPrincipal().getName(), page, n);
		for (int i = 0; i < listEvents.getEventList().size(); i++)
			listEvents.getEventList().get(i).getProperties().setServerip(null);
		return new ResponseEntity<>(listEvents, HttpStatus.OK);
	}

	@GetMapping("/api/events/user/date/{date}/{page}/{n}")
	public ResponseEntity<ListEvents> retrieveCurrentUserEventsByDate(HttpServletRequest request, @PathVariable String date, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, ParseException, org.json.simple.parser.ParseException {
		ListEvents listEvents = eventRepo.getListUserEventByDate(date, request.getUserPrincipal().getName(), page, n);
		for (int i = 0; i < listEvents.getEventList().size(); i++)
			listEvents.getEventList().get(i).getProperties().setServerip(null);

		return new ResponseEntity<>(listEvents, HttpStatus.OK);
	}

	@GetMapping("/api/events/users/export")
	public ModelAndView downloadExcel() {

		// return a view which will be resolved by an excel view resolver
		//noinspection ConstantConditions
		return new ModelAndView(eventsExcelView, "listEvents", null);
	}
}
