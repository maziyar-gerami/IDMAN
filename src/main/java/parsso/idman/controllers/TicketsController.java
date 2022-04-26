package parsso.idman.controllers;

import javax.servlet.http.HttpServletRequest;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import parsso.idman.helpers.Variables;
import parsso.idman.models.response.Response;
import parsso.idman.models.tickets.ListTickets;
import parsso.idman.models.tickets.Ticket;
import parsso.idman.models.users.User;
import parsso.idman.repos.TicketRepo;
import parsso.idman.repos.UserRepo;

@RestController
public class TicketsController {
  final UserRepo.UsersOp.Retrieve retrieveUsers;
  TicketRepo ticketRepo;

  @Autowired
  public TicketsController(UserRepo.UsersOp.Retrieve retrieveUsers, TicketRepo ticketRepo) {
    this.retrieveUsers = retrieveUsers;
    this.ticketRepo = ticketRepo;
  }

  @PostMapping("/api/user/ticket")
  public ResponseEntity<Response> sendTicket(@RequestBody Ticket ticket, HttpServletRequest request,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(
        new Response(null, Variables.MODEL_TICKETING,
            ticketRepo.sendTicket(ticket, request.getUserPrincipal().getName()).value(), lang),
        HttpStatus.CREATED);
  }

  @PutMapping("/api/user/ticket/reply/{ticketID}")
  public ResponseEntity<Response> replyTicket(@PathVariable("ticketID") String ticketID,
      @RequestBody Ticket ticket, HttpServletRequest request,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(
        new Response(null, Variables.MODEL_TICKETING,
            ticketRepo.reply(ticketID, request.getUserPrincipal().getName(), ticket).value(), lang),
        HttpStatus.OK);
  }

  @GetMapping("/api/user/ticket/{ticketID}")
  public ResponseEntity<Response> retrieveTicket(@PathVariable("ticketID") String ticketID,
      HttpServletRequest request,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    Ticket ticket = ticketRepo.retrieveTicket(ticketID);
    User user = retrieveUsers.retrieveUsers(request.getUserPrincipal().getName());
    if (user.getUsersExtraInfo().getRole().equalsIgnoreCase("USER")) {
      if (user.get_id().toString().equalsIgnoreCase(ticket.getTo())
          || user.get_id().toString().equalsIgnoreCase(ticket.getFrom())) {
        return new ResponseEntity<>(new Response(
            ticket, Variables.MODEL_TICKETING, HttpStatus.OK.value(), lang), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(new Response(
              null, Variables.MODEL_TICKETING, HttpStatus.FORBIDDEN.value(), lang), HttpStatus.OK);
      }
    } else if (ticket != null) {
      return new ResponseEntity<>(new Response(
          ticket, Variables.MODEL_TICKETING, HttpStatus.OK.value(), lang), HttpStatus.OK);
    }
    return new ResponseEntity<>(new Response(
      null, Variables.MODEL_TICKETING, HttpStatus.BAD_REQUEST.value(), lang), HttpStatus.OK);
  }

  @DeleteMapping("/api/user/tickets")
  public ResponseEntity<Response> deleteTicket(@RequestBody JSONObject jsonObject, HttpServletRequest request,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    if (ticketRepo.deleteTicket(request.getUserPrincipal().getName(), jsonObject) == HttpStatus.OK) {
      return new ResponseEntity<>(new Response(null, Variables.MODEL_TICKETING, HttpStatus.OK.value(), lang),
          HttpStatus.OK);
    }
    return new ResponseEntity<>(new Response(null, Variables.MODEL_TICKETING, HttpStatus.BAD_REQUEST.value(), lang),
        HttpStatus.OK);
  }

  @GetMapping("/api/user/tickets/sent/{page}/{count}")
  public ResponseEntity<Response> sendTicket(HttpServletRequest request, @PathVariable(name = "page") String page,
      @RequestParam(name = "date", defaultValue = "") String date,
      @PathVariable(name = "count") String count,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {

    return new ResponseEntity<>(
        new Response(ticketRepo.retrieveSentTickets(request.getUserPrincipal().getName(), page, count, date),
            Variables.MODEL_TICKETING, HttpStatus.OK.value(), lang),
        HttpStatus.OK);
  }

  @PutMapping("/api/supporter/ticket/status/{status}")
  public ResponseEntity<Response> updateTicketStatus(@PathVariable int status, @RequestBody JSONObject jsonObject,
      HttpServletRequest request,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    if (ticketRepo.updateTicketStatus(request.getUserPrincipal().getName(), status, jsonObject) == HttpStatus.OK) {
      return new ResponseEntity<>(new Response(null, Variables.MODEL_TICKETING, HttpStatus.OK.value(), lang),
          HttpStatus.OK);
    }
    return new ResponseEntity<>(new Response(null, Variables.MODEL_TICKETING, HttpStatus.BAD_REQUEST.value(), lang),
        HttpStatus.OK);

  }

  @GetMapping("/api/supporter/tickets/inbox/{page}/{count}")
  public ResponseEntity<Response> received(HttpServletRequest request, @PathVariable(name = "page") String page,
      @RequestParam(name = "from", defaultValue = "") String from,
      @RequestParam(name = "id", defaultValue = "") String id,
      @RequestParam(name = "date", defaultValue = "") String date,
      @PathVariable(name = "count") String count,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    ListTickets tickets = ticketRepo.retrieveTicketsReceived(request.getUserPrincipal().getName(), page, count, from,
        id, date);
    return new ResponseEntity<>(new Response(tickets, Variables.MODEL_TICKETING, HttpStatus.OK.value(), lang),
        HttpStatus.OK);
  }

  @PutMapping("/api/user/ticket/{ticketID}")
  public ResponseEntity<Response> updateTicket(@RequestBody Ticket ticket, @PathVariable("ticketID") String ticketID,
      HttpServletRequest request,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {

    if (ticketRepo.updateTicket(request.getUserPrincipal().getName(), ticketID, ticket) == HttpStatus.OK) {
      return new ResponseEntity<>(new Response(null, Variables.MODEL_TICKETING, HttpStatus.OK.value(), lang),
          HttpStatus.OK);
    }
    return new ResponseEntity<>(new Response(null, Variables.MODEL_TICKETING, HttpStatus.BAD_REQUEST.value(), lang),
        HttpStatus.OK);

  }

  @GetMapping("/api/supporter/tickets/{page}/{count}")
  public ResponseEntity<Response> pendingTickets(@RequestParam(name = "cat", defaultValue = "") String cat,
      @RequestParam(name = "subCat", defaultValue = "") String subCat,
      @RequestParam(name = "status", defaultValue = "") String status,
      @RequestParam(name = "from", defaultValue = "") String from,
      @RequestParam(name = "id", defaultValue = "") String id,
      @RequestParam(name = "date", defaultValue = "") String date,
      @PathVariable(name = "page") String page,
      @PathVariable(name = "count") String count,
      HttpServletRequest request,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    ListTickets ls = ticketRepo.retrieve(request.getUserPrincipal().getName(), cat, subCat, status, page, count,
        from, id, date);
    return new ResponseEntity<>(new Response(ls, Variables.MODEL_TICKETING, HttpStatus.OK.value(), lang),
        HttpStatus.OK);

  }

  @GetMapping("/api/superuser/tickets/archive/{page}/{count}")
  public ResponseEntity<Response> archiveTickets(@RequestParam(name = "cat", defaultValue = "") String cat,
      @RequestParam(name = "subCat", defaultValue = "") String subCat,
      @RequestParam(name = "status", defaultValue = "") String status,
      @RequestParam(name = "from", defaultValue = "") String from,
      @RequestParam(name = "id", defaultValue = "") String id,
      @RequestParam(name = "date", defaultValue = "") String date,
      @PathVariable(name = "page") String page,
      @PathVariable(name = "count") String count,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NoSuchFieldException, IllegalAccessException {
    ListTickets ls = ticketRepo.retrieveArchivedTickets(cat, subCat, page, count, from, id, date);
    return new ResponseEntity<>(new Response(ls, Variables.MODEL_TICKETING, HttpStatus.OK.value(), lang),
        HttpStatus.OK);

  }

}
