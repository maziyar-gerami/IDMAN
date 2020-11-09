package parsso.idman.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.Models.Event;
import parsso.idman.Repos.EventRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;

@RestController
public class EventController {


    @Autowired
    private EventRepo eventRepo;


    @GetMapping("/api/events")
    public ResponseEntity<List<Event>> retrieveAllEvents() throws FileNotFoundException, ParseException {
        return new ResponseEntity<>(eventRepo.getListUserEvents(), HttpStatus.OK);
    }

    @GetMapping("/api/events/users/")
    public ResponseEntity<List<Event>> retrieveAllEventsWithEmptyUserName() throws FileNotFoundException, ParseException {
        return new ResponseEntity<>(eventRepo.getListUserEvents(), HttpStatus.OK);
    }

    @GetMapping("/api/events/date/")
    public ResponseEntity<List<Event>> retrieveAllEventsWithEmptyDate() throws FileNotFoundException, ParseException {
        return new ResponseEntity<>(eventRepo.getListUserEvents(), HttpStatus.OK);
    }

    @GetMapping("/api/events/users/{userId}")
    public ResponseEntity<List<Event>> retrieveByUser(@PathVariable String userId) throws FileNotFoundException, ParseException {
        return new ResponseEntity<>(eventRepo.getListUserEvents(userId), HttpStatus.OK);
    }

    @GetMapping("/api/events/date/{date}")
    public ResponseEntity<List<Event>> retrieveByDate(@PathVariable String date) throws FileNotFoundException, ParseException {
        return new ResponseEntity<>(eventRepo.getEventsByDate(date), HttpStatus.OK);
    }

    @GetMapping("/api/events/users/{id}/date/{date}")
    public ResponseEntity<List<Event>> retrieveByUserDate(@PathVariable String id, @PathVariable String date) throws FileNotFoundException, ParseException {
        return new ResponseEntity<>(eventRepo.getListUserEventByDate(date, id), HttpStatus.OK);
    }

    @GetMapping("/api/events/user")
    public ResponseEntity<List<Event>> retrieveCurrentUserEvents(HttpServletRequest request) throws IOException, ParseException {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(eventRepo.getListUserEvents(principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/api/events/user/date/{date}")
    public ResponseEntity<List<Event>> retrieveCurrentUserEventsByDate(HttpServletRequest request, @PathVariable String date) throws IOException, ParseException {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(eventRepo.getListUserEventByDate(date, principal.getName()), HttpStatus.OK);
    }

}
