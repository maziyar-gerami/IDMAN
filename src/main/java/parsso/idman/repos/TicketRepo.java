package parsso.idman.repos;

import net.minidev.json.JSONObject;
import parsso.idman.models.tickets.ListTickets;
import parsso.idman.models.tickets.Ticket;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@SuppressWarnings("SameReturnValue")
@Service
public interface TicketRepo {
  HttpStatus sendTicket(Ticket ticket, String userId);

  Ticket retrieveTicket(String ticketID);

  HttpStatus reply(String ticketID, String user, Ticket ticket);

  HttpStatus deleteTicket(String doer, JSONObject jsonObject);

  HttpStatus updateTicketStatus(String doer, int status, JSONObject jsonObject);

  ListTickets retrieveSentTickets(String userId, String page, String count, String date);

  ListTickets retrieveTicketsReceived(String userId, String page, String count, String from, String ticketId,
      String date);

  HttpStatus updateTicket(String userId, String ticketId, Ticket ticket);

  ListTickets retrieve(String doer, String cat, String subCat, String status, String page, String count, String from,
      String ticketId, String date);

  ListTickets retrieveArchivedTickets(String cat, String subCat, String page, String count, String from,
      String ticketId, String date);
}
