package parsso.idman.Controllers;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.Event;
import parsso.idman.Models.Group;
import parsso.idman.Models.User;
import parsso.idman.Repos.EventsRepo;
import parsso.idman.Repos.UserRepo;
import parsso.idman.Repos.GroupRepo;
import parsso.idman.utils.Convertor.DateConverter;

import java.io.*;
import java.security.Principal;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class EventController {

    DateConverter converter = new DateConverter();


    @Autowired
    private EventsRepo eventsRepo;


    @GetMapping("/api/events")
    public ResponseEntity<List<Event>> retrieveAllEvents() throws FileNotFoundException, ParseException {
        return new ResponseEntity<>(eventsRepo.getListUserEvents(), HttpStatus.OK);
    }

    @GetMapping("/api/events/users/")
    public ResponseEntity<List<Event>> retrieveAllEventsWithEmptyUserName() throws FileNotFoundException, ParseException {
        return new ResponseEntity<>(eventsRepo.getListUserEvents(), HttpStatus.OK);
    }

    @GetMapping("/api/events/date/")
    public ResponseEntity<List<Event>> retrieveAllEventsWithEmptyDate() throws FileNotFoundException, ParseException {
        return new ResponseEntity<>(eventsRepo.getListUserEvents(), HttpStatus.OK);
    }

    @GetMapping("/api/events/users/{userId}")
    public ResponseEntity<List<Event>> retrieveByUser(@PathVariable String userId) throws FileNotFoundException, ParseException {

        return new ResponseEntity<>(eventsRepo.getListUserEvents(userId), HttpStatus.OK);
    }

    @GetMapping("/api/events/date/{date}")
    public ResponseEntity<List<Event>> retrieveByDate(@PathVariable String date) throws FileNotFoundException, ParseException {
        return new ResponseEntity<>(eventsRepo.getEventsByDate(date), HttpStatus.OK);
    }

    @GetMapping("/api/events/users/{id}/date/{date}")
    public ResponseEntity<List<Event>> retrieveByUserDate(@PathVariable String id, @PathVariable String date) throws FileNotFoundException, ParseException {
        return new ResponseEntity<>(eventsRepo.getListUserEventByDate(date, id), HttpStatus.OK);
    }

    @GetMapping("/api/events/user")
    public ResponseEntity<List<Event>> retrieveCurrentUserEvents(HttpServletRequest request) throws IOException, ParseException {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(eventsRepo.getListUserEvents(principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/api/events/user/date/{date}")
    public ResponseEntity<List<Event>> retrieveCurrentUserEventsByDate(HttpServletRequest request,@PathVariable String date) throws IOException, ParseException {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(eventsRepo.getListUserEventByDate(date,principal.getName()), HttpStatus.OK);
    }

}
