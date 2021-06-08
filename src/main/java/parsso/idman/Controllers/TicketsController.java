package parsso.idman.Controllers;


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
        return new ResponseEntity<>(ticketRepo.sendTicket(ticket, request.getUserPrincipal().getName().toLowerCase()));
    }

    @PutMapping("/api/user/ticket/reply/{ticketID}")
    public ResponseEntity<HttpStatus> replyTicket(@PathVariable("ticketID") String ticketID,
                                                  @RequestBody Ticket ticket, HttpServletRequest request) {
        return new ResponseEntity<>(ticketRepo.reply(ticketID, request.getUserPrincipal().getName().toLowerCase(), ticket));
    }

    @GetMapping("/api/user/ticket/{ticketID}")
    public ResponseEntity<Ticket> retrieveTicket(@PathVariable("ticketID") String ticketID, HttpServletRequest request) {
        Ticket ticket = ticketRepo.retrieveTicket(ticketID);
        User user = userRepo.retrieveUsers(request.getUserPrincipal().getName().toLowerCase());
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
        return new ResponseEntity<>(ticketRepo.deleteTicket(request.getUserPrincipal().getName().toLowerCase(),
                jsonObject) == HttpStatus.OK ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api/user/tickets/sent/{page}/{count}")
    public ResponseEntity<ListTickets> sendTicket(HttpServletRequest request, @PathVariable(name = "page") String page,
                                                  @RequestParam(name = "date", defaultValue = "") String date,
                                                  @PathVariable(name = "count") String count) {

        return new ResponseEntity<>(ticketRepo.retrieveSentTickets(request.getUserPrincipal().getName().toLowerCase(), page, count,date), HttpStatus.OK);
    }

    @PutMapping("/api/supporter/ticket/status/{status}")
    public ResponseEntity<HttpStatus> updateTicketStatus(@PathVariable int status, @RequestBody JSONObject jsonObject,
                                                         HttpServletRequest request) {
        return new ResponseEntity<>(ticketRepo.updateTicketStatus(request.getUserPrincipal().getName().toLowerCase(), status, jsonObject) == HttpStatus.OK ? HttpStatus.OK : HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/api/supporter/tickets/inbox/{page}/{count}")
    public ResponseEntity<ListTickets> received(HttpServletRequest request, @PathVariable(name = "page") String page,
                                                @RequestParam(name = "from", defaultValue = "") String from,
                                                @RequestParam(name = "id", defaultValue = "") String id,
                                                @RequestParam(name = "date", defaultValue = "") String date,
                                                @PathVariable(name = "count") String count) {
        ListTickets tickets = ticketRepo.retrieveTicketsReceived(request.getUserPrincipal().getName().toLowerCase(), page, count, from, id, date);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @PutMapping("/api/user/ticket/{ticketID}")
    public ResponseEntity<HttpStatus> updateTicket(@RequestBody Ticket ticket, @PathVariable("ticketID") String ticketID,
                                                   HttpServletRequest request) {

        return new ResponseEntity<>(ticketRepo.updateTicket(request.getUserPrincipal().getName().toLowerCase(), ticketID, ticket) == HttpStatus.OK ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api/supporter/tickets/{page}/{count}")
    public ResponseEntity<ListTickets> pendingTickets(@RequestParam(name = "cat", defaultValue = "") String cat,
                                                      @RequestParam(name = "subCat", defaultValue = "") String subCat,
                                                      @RequestParam(name = "status", defaultValue = "") String status,
                                                      @RequestParam(name = "from", defaultValue = "") String from,
                                                      @RequestParam(name = "id", defaultValue = "") String id,
                                                      @RequestParam(name = "date", defaultValue = "") String date,
                                                      @PathVariable(name = "page") String page,
                                                      @PathVariable(name = "count") String count,
                                                      HttpServletRequest request) {
        ListTickets ls = ticketRepo.retrieve(request.getUserPrincipal().getName(), cat, subCat, status, page, count, from, id, date);
        return new ResponseEntity<>(ls, HttpStatus.OK);

    }

    @GetMapping("/api/superuser/tickets/archive/{page}/{count}")
    public ResponseEntity<ListTickets> archiveTickets(@RequestParam(name = "cat", defaultValue = "") String cat,
                                                      @RequestParam(name = "subCat", defaultValue = "") String subCat,
                                                      @RequestParam(name = "status", defaultValue = "") String status,
                                                      @RequestParam(name = "from", defaultValue = "") String from,
                                                      @RequestParam(name = "id", defaultValue = "") String id,
                                                      @RequestParam(name = "date", defaultValue = "") String date,
                                                      @PathVariable(name = "page") String page,
                                                      @PathVariable(name = "count") String count,
                                                      HttpServletRequest request) {
        ListTickets ls = ticketRepo.retrieveArchivedTickets("maziyar", cat, subCat, status, page, count, from, id, date);
        return new ResponseEntity<>(ls, HttpStatus.OK);

    }

}
