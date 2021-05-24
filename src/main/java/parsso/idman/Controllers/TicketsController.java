package parsso.idman.Controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.Tickets.ListTickets;
import parsso.idman.Models.Tickets.Ticket;
import parsso.idman.Models.Users.User;
import parsso.idman.Repos.TicketRepo;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TicketsController {

    @Autowired
    TicketRepo ticketRepo;

    @Autowired
    UserRepo userRepo;

    //*************************************** Pages ***************************************

    @GetMapping("/ticketing")
    public String Reports() {
        return "ticketing";
    }

    //*************************************** APIs ***************************************

    @PostMapping("/api/user/ticket")
    public ResponseEntity<HttpStatus> sendTicket(@RequestBody Ticket ticket, HttpServletRequest request) {
        return new ResponseEntity<>(ticketRepo.sendTicket(ticket,  request.getUserPrincipal().getName()));
    }

    @PutMapping("/api/user/ticket/reply/{ticketID}")
    public ResponseEntity<HttpStatus> replyTicket(@PathVariable ("ticketID") String ticketID,
                                                  @RequestBody Ticket ticket, HttpServletRequest request){
        return new ResponseEntity<>(ticketRepo.reply(ticketID,  request.getUserPrincipal().getName(),ticket));
    }

    @GetMapping("/api/user/ticket/{ticketID}")
    public ResponseEntity<Ticket> retrieveTicket(@PathVariable("ticketID") String ticketID, HttpServletRequest request) {
        Ticket ticket = ticketRepo.retrieveTicket(ticketID);
        User user = userRepo.retrieveUsers( request.getUserPrincipal().getName());
        if (user.getUsersExtraInfo().getRole().equalsIgnoreCase("USER"))
            if (user.getUserId().equalsIgnoreCase(ticket.getTo()) || user.getUserId().equalsIgnoreCase(ticket.getFrom()))
                return new ResponseEntity<>(ticket, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        else
            return new ResponseEntity<>(ticket, ticket != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/api/user/ticket")
    public ResponseEntity<HttpStatus> deleteTicket(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
            return new ResponseEntity<>(ticketRepo.deleteTicket( request.getUserPrincipal().getName(),
                    jsonObject) == HttpStatus.OK ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api/user/tickets/sent/{page}/{count}")
    public ResponseEntity<List<Ticket>> sendTicket(HttpServletRequest request) {
        List<Ticket> tickets = ticketRepo.retrieveTicketsReceived( request.getUserPrincipal().getName());
        tickets.stream().filter(c -> (c.getTo().equals(request.getUserPrincipal())) ||
                c.getFrom().equals( request.getUserPrincipal().getName())).collect(Collectors.toList());
        return new ResponseEntity<>(ticketRepo.retrieveTicketsSend( request.getUserPrincipal().getName()), HttpStatus.OK);
    }

    @PutMapping("/api/supporter/ticket/status/{status}")
    public ResponseEntity<HttpStatus> updateTicketStatus(@PathVariable int status, @RequestBody JSONObject jsonObject,
                                                         HttpServletRequest request) {
            return new ResponseEntity<>(ticketRepo.updateTicketStatus(request.getUserPrincipal().getName(),status, jsonObject) == HttpStatus.OK ? HttpStatus.OK : HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/api/user/tickets/inbox/{page}/{count}")
    public ResponseEntity<List<Ticket>> received(HttpServletRequest request) {
        List<Ticket> tickets = ticketRepo.retrieveTicketsReceived( request.getUserPrincipal().getName());
        tickets.stream().filter(c -> (c.getFrom().equals(request.getUserPrincipal())) ||
                c.getFrom().equals( request.getUserPrincipal().getName())).collect(Collectors.toList());

        return new ResponseEntity<>(ticketRepo.retrieveTicketsReceived( request.getUserPrincipal().getName()), HttpStatus.OK);
    }

    @PutMapping("/api/user/ticket/{ticketID}")
    public ResponseEntity<HttpStatus> updateTicket(@RequestBody Ticket ticket, @PathVariable("ticketID") String ticketID,
                                                   HttpServletRequest request) {

        return new ResponseEntity<>(ticketRepo.updateTicket(request.getUserPrincipal().getName(), ticketID, ticket) == HttpStatus.OK ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api/supporter/tickets/{page}/{count}")
    public ResponseEntity<ListTickets> pendingTickets(@RequestParam (name = "cat", defaultValue = "") String cat,
                                                      @RequestParam (name = "subCat", defaultValue = "") String subCat,
                                                      @RequestParam (name = "status", defaultValue = "") String status,
                                                      @PathVariable (name = "page") String page,
                                                      @PathVariable (name = "count") String count) {
        return new ResponseEntity<>(ticketRepo.retrieve(cat,subCat, status,page,count),HttpStatus.OK);
    }

}
