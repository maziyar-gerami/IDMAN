package parsso.idman.Controllers;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.Ticket;
import parsso.idman.Repos.TicketRepo;

import java.io.IOException;
import java.util.List;

public class TicketController {

    @Autowired
    TicketRepo ticketRepo;

    @GetMapping("/api/tickets/{chat}")
    public ResponseEntity<List<Ticket>> retrieveChat(@PathVariable("chat") String chatID){
        return new ResponseEntity<>(ticketRepo.retrieveChat(chatID) !=null?HttpStatus.OK:HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api/ticket/{ticket}")
    public ResponseEntity<Ticket> retrieveTicket(@PathVariable("ticket") long ticketID){
        return new ResponseEntity<>(ticketRepo.retrieveTicket(ticketID)!=null?HttpStatus.OK:HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/api/tickets/{chat}")
    public ResponseEntity<HttpStatus> deleteChat(@PathVariable("chat") String chatID) {
        return new ResponseEntity<>(ticketRepo.deleteChat(chatID)==HttpStatus.OK?HttpStatus.OK:HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/api/ticket/{ticket}")
    public ResponseEntity<HttpStatus> deleteTicket(@PathVariable("ticket") long ticketID) {
        return new ResponseEntity<>(ticketRepo.deleteTicket(ticketID) == HttpStatus.OK? HttpStatus.OK:HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/ticket")
    public ResponseEntity<HttpStatus> sendTicket(@RequestBody Ticket ticket) {
        return new ResponseEntity<>(ticketRepo.sendTicket(ticket) == HttpStatus.OK?HttpStatus.OK:HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/api/ticket/{ticketID}")
    public ResponseEntity<HttpStatus> updateTicket(@RequestBody Ticket ticket,@PathVariable("ticketID") long ticketID){
        return new ResponseEntity<>(ticketRepo.updateTicket(ticketID, ticket)==HttpStatus.OK? HttpStatus.OK:HttpStatus.BAD_REQUEST);
    }

}
