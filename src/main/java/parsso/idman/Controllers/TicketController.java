package parsso.idman.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.Ticket;
import parsso.idman.Repos.TicketRepo;

import java.util.List;

public class TicketController {

    @Autowired
    TicketRepo ticketRepo;


    @GetMapping("/api/ticket/{ticketID}")
    public ResponseEntity<Ticket> retrieveTicket(@PathVariable("ticket") long ticketID){
        return new ResponseEntity<>(ticketRepo.retrieveTicket(ticketID)!=null?HttpStatus.OK:HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/api/ticket/{ticketID}")
    public ResponseEntity<HttpStatus> deleteTicket(@PathVariable("ticket") long ticketID) {
        return new ResponseEntity<>(ticketRepo.deleteTicket(ticketID) == HttpStatus.OK? HttpStatus.OK:HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/ticket")
    public ResponseEntity<HttpStatus> sendTicket(@RequestBody Ticket ticket) {
        return new ResponseEntity<>(ticketRepo.sendTicket(ticket) == HttpStatus.OK?HttpStatus.OK:HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api/tickets")
    public ResponseEntity<List<Ticket>> sendTicket(@RequestBody String userId) {
        return new ResponseEntity<>(ticketRepo.retrieveTickets(userId), HttpStatus.OK );
    }

    @PutMapping("/api/ticket/{ticketID}")
    public ResponseEntity<HttpStatus> updateTicket(@RequestBody Ticket ticket,@PathVariable("ticketID") long ticketID){
        return new ResponseEntity<>(ticketRepo.updateTicket(ticketID, ticket)==HttpStatus.OK? HttpStatus.OK:HttpStatus.BAD_REQUEST);
    }

}
