package parsso.idman.Controllers;


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

    @GetMapping("/api/user/ticket/{ticketID}")
    public ResponseEntity<Ticket> retrieveTicket(@PathVariable("ticketID") String ticketID, HttpServletRequest request) {
        Ticket ticket = ticketRepo.retrieveTicket(ticketID);
        User user = userRepo.retrieveUsers(request.getUserPrincipal().getName());
        if (user.getUsersExtraInfo().getRole().equalsIgnoreCase("USER"))
            if (user.getUserId().equalsIgnoreCase(ticket.getTo()) || user.getUserId().equalsIgnoreCase(ticket.getFrom()))
                return new ResponseEntity<>(ticket, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        else
            return new ResponseEntity<>(ticket, ticket != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping("/api/user/ticket/{ticketID}")
    public ResponseEntity<HttpStatus> deleteTicket(@PathVariable("ticketID") String ticketID, HttpServletRequest request) {
        if (ticketRepo.retrieveTicket(ticketID).getFrom().equals(request.getUserPrincipal().getName()))
            return new ResponseEntity<>(ticketRepo.deleteTicket(ticketID) == HttpStatus.OK ? HttpStatus.OK : HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/api/user/ticket")
    public ResponseEntity<HttpStatus> sendTicket(@RequestBody Ticket ticket, HttpServletRequest request) {
        return new ResponseEntity<>(ticketRepo.sendTicket(ticket, request.getUserPrincipal().getName()));
    }

    @PostMapping("/api/supporter/ticket/reply/{ticketID}")
    public ResponseEntity<HttpStatus> replyTicket(@PathVariable ("ticketID") String ticketID,
                                            @RequestBody Ticket ticket, HttpServletRequest request){

        return new ResponseEntity<>(ticketRepo.reply(ticketID, request.getUserPrincipal().getName(),ticket));
    }

    @GetMapping("/api/user/tickets/sent")
    public ResponseEntity<List<Ticket>> sendTicket(HttpServletRequest request) {
        List<Ticket> tickets = ticketRepo.retrieveTicketsReceived(request.getUserPrincipal().getName());
        tickets.stream().filter(c -> (c.getTo().equals(request.getUserPrincipal())) ||
                c.getFrom().equals(request.getUserPrincipal().getName())).collect(Collectors.toList());
        return new ResponseEntity<>(ticketRepo.retrieveTicketsSend(request.getUserPrincipal().getName()), HttpStatus.OK);
    }

    @GetMapping("/api/supporter/tickets")
    public ResponseEntity<List<Ticket>> pendingTickets(@RequestParam (name = "cat", defaultValue = "") String cat,
                                                      @RequestParam (name = "subCat", defaultValue = "") String subCat,
                                                      @RequestParam (name = "status", defaultValue = "") String status,
                                                      @RequestParam (name = "page", defaultValue = "") String page,
                                                      @RequestParam (name = "count", defaultValue = "") String count)
    {
        return new ResponseEntity<>(ticketRepo.retrieve(cat,subCat, status) , HttpStatus.OK);
    }

    @GetMapping("/api/user/tickets/inbox")
    public ResponseEntity<List<Ticket>> received(HttpServletRequest request) {
        List<Ticket> tickets = ticketRepo.retrieveTicketsReceived(request.getUserPrincipal().getName());
        tickets.stream().filter(c -> (c.getFrom().equals(request.getUserPrincipal())) ||
                c.getFrom().equals(request.getUserPrincipal().getName())).collect(Collectors.toList());

        return new ResponseEntity<>(ticketRepo.retrieveTicketsReceived(request.getUserPrincipal().getName()), HttpStatus.OK);
    }

    @GetMapping("/api/user/tickets/chat/{chatID}")
    public ResponseEntity<List<Ticket>> getChat(@PathVariable(name = "chatID") String chatID) {
            return new ResponseEntity<>(ticketRepo.retrieveChat(chatID), HttpStatus.OK);
    }

    @DeleteMapping("/api/user/tickets/chat/{chatID}")
    public ResponseEntity<HttpStatus> deleteChat(@PathVariable(name = "chatID") String chatID, HttpServletRequest request) {
        if (ticketRepo.retrieveChat(chatID).get(0).getFrom().equals(request.getUserPrincipal().getName()) ||
                ticketRepo.retrieveChat(chatID).get(0).getTo().equals(request.getUserPrincipal().getName()))
            return new ResponseEntity<>(ticketRepo.deleteChat(chatID), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @PutMapping("/api/supporter/ticket/{ticketID}")
    public ResponseEntity<HttpStatus> updateTicket(@RequestBody Ticket ticket, @PathVariable("ticketID") String ticketID) {

        return new ResponseEntity<>(ticketRepo.updateTicket(ticketID, ticket) == HttpStatus.OK ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/api/supporter/ticket/status/{ticketID}/{status}")
    public ResponseEntity<HttpStatus> updateTicketStatus(@PathVariable int status, @PathVariable("ticketID") String ticketID,
                                                         HttpServletRequest request) {
        if (ticketRepo.retrieveTicket(ticketID).getFrom().equals(request.getUserPrincipal().getName()))
            return new ResponseEntity<>(ticketRepo.updateTicketStatus(status, ticketID) == HttpStatus.OK ? HttpStatus.OK : HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
