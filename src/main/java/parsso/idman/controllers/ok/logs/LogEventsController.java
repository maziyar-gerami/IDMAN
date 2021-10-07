package parsso.idman.controllers.ok.logs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.Models.Logs.Event;
import parsso.idman.RepoImpls.logs.EventsRepoImpl;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

@RestController
@RequestMapping("/api/logs/events")
public class LogEventsController {
	EventsRepoImpl eventRepo;

	@Autowired
	public LogEventsController(EventsRepoImpl eventRepo) {
		this.eventRepo = eventRepo;
	}

	@GetMapping("/users")
	public ResponseEntity<Event.ListEvents> getUsersEvents(@RequestParam(name = "userID", defaultValue = "") String userID,
	                                                       @RequestParam(name = "date", defaultValue = "") String date,
	                                                       @RequestParam(name = "page") String page,
	                                                       @RequestParam(name = "count") String count) throws ParseException {
		return new ResponseEntity<>(eventRepo.retrieve(userID, date, !page.equals("") ? Integer.parseInt(page) : 0,
				!count.equals("") ? Integer.parseInt(count) : 0), HttpStatus.OK);
	}

	@GetMapping("/user")
	public ResponseEntity<Event.ListEvents> getUserEvents(HttpServletRequest request,
	                                               @RequestParam(name = "date", defaultValue = "") String date,
	                                               @RequestParam(name = "page") String page,
	                                               @RequestParam(name = "count") String count) throws ParseException {
		return new ResponseEntity<>(eventRepo.retrieve(request.getUserPrincipal().getName(), date, !page.equals("") ? Integer.parseInt(page) : 0,
				!count.equals("") ? Integer.parseInt(count) : 0), HttpStatus.OK);

	}
}





