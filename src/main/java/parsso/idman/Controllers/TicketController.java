package parsso.idman.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.Ticket;
import parsso.idman.Repos.TicketRepo;

import java.util.List;
@RestController
public class TicketController {

    @Autowired
    TicketRepo ticketRepo;

    @GetMapping("/api/ticket/{ticketID}")
    public ResponseEntity<Ticket> retrieveTicket(@PathVariable("ticket") String ticketID) {
        return new ResponseEntity<>(ticketRepo.retrieveTicket(ticketID) != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/api/ticket/{ticketID}")
    public ResponseEntity<HttpStatus> deleteTicket(@PathVariable("ticket") String ticketID) {
        return new ResponseEntity<>(ticketRepo.deleteTicket(ticketID) == HttpStatus.OK ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/ticket")
    public ResponseEntity<HttpStatus> sendTicket(@RequestBody Ticket ticket) {
        return new ResponseEntity<>(ticketRepo.sendTicket(ticket));
    }

    @GetMapping("/api/tickets/out")
    public ResponseEntity<List<Ticket>> sendTicket(@RequestBody String userId) {
        return new ResponseEntity<>(ticketRepo.retrieveTicketsSend(userId), HttpStatus.OK);
    }

    @GetMapping("/api/tickets/in")
    public ResponseEntity<List<Ticket>> received(@RequestBody String userId) {
        return new ResponseEntity<>(ticketRepo.retrieveTicketsReceived(userId), HttpStatus.OK);
    }

    @GetMapping("/api/tickets/chat")
    public ResponseEntity<List<Ticket>> getChat(@RequestBody String chatID) {
        return new ResponseEntity<>(ticketRepo.retrieveChat(chatID), HttpStatus.OK);
    }

    @DeleteMapping("/api/tickets/chat")
    public ResponseEntity<HttpStatus> deleteChat(@RequestBody String chatID) {
        return new ResponseEntity<>(ticketRepo.deleteChat(chatID), HttpStatus.OK);
    }

    @PutMapping("/api/ticket/{ticketID}")
    public ResponseEntity<HttpStatus> updateTicket(@RequestBody Ticket ticket, @PathVariable("ticketID") String ticketID) {
        return new ResponseEntity<>(ticketRepo.updateTicket(ticketID, ticket) == HttpStatus.OK ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

}
