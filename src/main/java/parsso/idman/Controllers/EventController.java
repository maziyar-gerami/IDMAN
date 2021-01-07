package parsso.idman.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import parsso.idman.Helpers.Events.EventsExcelView;
import parsso.idman.Models.ListEvents;
import parsso.idman.Repos.EventRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;

@RestController
public class EventController {


    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private EventsExcelView eventsExcelView;

    //OK
    @GetMapping("/api/events/{page}/{n}")
    public ResponseEntity<ListEvents> retrieveAllEvents(@PathVariable("page") int page, @PathVariable("n") int n) throws IOException, ParseException, org.json.simple.parser.ParseException {
        return new ResponseEntity<>(eventRepo.getListSizeEvents(page, n), HttpStatus.OK);
    }

    //OK
    @GetMapping("/api/events/users/{userId}/{page}/{n}")
    public ResponseEntity<ListEvents> retrieveByUser(@PathVariable String userId, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, ParseException, org.json.simple.parser.ParseException {
        return new ResponseEntity<>(eventRepo.getListUserEvents(userId, page, n), HttpStatus.OK);
    }

    //OK
    @GetMapping("/api/events/date/{date}/{page}/{n}")
    public ResponseEntity<ListEvents> retrieveByDate(@PathVariable String date, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, ParseException, org.json.simple.parser.ParseException {
        return new ResponseEntity<>(eventRepo.getEventsByDate(date, page, n), HttpStatus.OK);
    }

    //OK
    @GetMapping("/api/events/users/{id}/date/{date}/{page}/{n}")
    public ResponseEntity<ListEvents> retrieveByUserDate(@PathVariable String id, @PathVariable String date, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, ParseException, org.json.simple.parser.ParseException {
        return new ResponseEntity<>(eventRepo.getListUserEventByDate(date, id, page, n), HttpStatus.OK);
    }

    //OK
    @GetMapping("/api/events/user/{page}/{n}")
    //@JsonView(Views.UserEvent.class)
    public ResponseEntity<ListEvents> retrieveCurrentUserEvents(HttpServletRequest request, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, ParseException, org.json.simple.parser.ParseException {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(eventRepo.getListUserEvents(principal.getName(), page, n), HttpStatus.OK);
    }

    //OK
    @GetMapping("/api/events/user/date/{date}/{page}/{n}")
    public ResponseEntity<ListEvents> retrieveCurrentUserEventsByDate(HttpServletRequest request, @PathVariable String date, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, ParseException, org.json.simple.parser.ParseException {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(eventRepo.getListUserEventByDate(date, principal.getName(), page, n), HttpStatus.OK);
    }


    @GetMapping("/api/events/export")
    public ModelAndView downloadExcel() throws ParseException, org.json.simple.parser.ParseException, IOException {

        // return a view which will be resolved by an excel view resolver
        return new ModelAndView(eventsExcelView, "listEvents", null);
    }

}
