package parsso.idman.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.Ticket;
import parsso.idman.Models.Users.User;
import parsso.idman.Repos.TicketRepo;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
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

    @GetMapping("/api/ticket/{ticketID}")
    public ResponseEntity<Ticket> retrieveTicket(@PathVariable("ticketID") String ticketID) {
        Ticket ticket = ticketRepo.retrieveTicket(ticketID);
        return new ResponseEntity<>(ticket, ticket != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/api/ticket/{ticketID}")
    public ResponseEntity<HttpStatus> deleteTicket(@PathVariable("ticketID") String ticketID) {
        return new ResponseEntity<>(ticketRepo.deleteTicket(ticketID) == HttpStatus.OK ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/ticket")
    public ResponseEntity<HttpStatus> sendTicket(@RequestBody Ticket ticket, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(ticketRepo.sendTicket(ticket, principal.getName()));
    }

    @PostMapping("/api/ticket/reply/{ticketID}")
    public ResponseEntity<HttpStatus> replyTicket(@PathVariable ("ticketID") String ticketID,
                                                  @PathVariable ("supporter") String supporter,
                                            @RequestBody Ticket ticket, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();

        return new ResponseEntity<>(ticketRepo.reply(ticketID, principal.getName(),ticket));
    }

    @GetMapping("/api/tickets/outbox")
    public ResponseEntity<List<Ticket>> sendTicket(HttpServletRequest request) {
        return new ResponseEntity<>(ticketRepo.retrieveTicketsSend(request.getUserPrincipal().getName()), HttpStatus.OK);
    }

    @GetMapping("/api/tickets/pending")
    public ResponseEntity<List<Ticket>> pendingTickets(@RequestParam (name = "cat", defaultValue = "") String cat, @RequestParam (name = "subCat", defaultValue = "") String subCat, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();
        User user = userRepo.retrieveUsers(principal.getName());

        return new ResponseEntity<>(ticketRepo.pendingTickets(cat,subCat,user), HttpStatus.OK);
    }

    @GetMapping("/api/tickets/inbox")
    public ResponseEntity<List<Ticket>> received(HttpServletRequest request) {
        return new ResponseEntity<>(ticketRepo.retrieveTicketsReceived(request.getUserPrincipal().getName()), HttpStatus.OK);
    }

    @GetMapping("/api/tickets/chat/{chatID}")
    public ResponseEntity<List<Ticket>> getChat(@PathVariable(name = "chatID") String chatID) {
        return new ResponseEntity<>(ticketRepo.retrieveChat(chatID), HttpStatus.OK);
    }

    @DeleteMapping("/api/tickets/chat/{chatID}")
    public ResponseEntity<HttpStatus> deleteChat(@PathVariable(name = "chatID") String chatID) {
        return new ResponseEntity<>(ticketRepo.deleteChat(chatID), HttpStatus.OK);
    }

    @PutMapping("/api/ticket/{ticketID}")
    public ResponseEntity<HttpStatus> updateTicket(@RequestBody Ticket ticket, @PathVariable("ticketID") String ticketID) {
        return new ResponseEntity<>(ticketRepo.updateTicket(ticketID, ticket) == HttpStatus.OK ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/api/ticket/status/{ticketID}/{status}")
    public ResponseEntity<HttpStatus> updateTicketStatus(@PathVariable int status, @PathVariable("ticketID") String ticketID) {
        return new ResponseEntity<>(ticketRepo.updateTicketStatus(status, ticketID) == HttpStatus.OK ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
