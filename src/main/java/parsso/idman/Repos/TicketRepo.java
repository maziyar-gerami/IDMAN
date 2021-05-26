package parsso.idman.Repos;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import parsso.idman.Models.Tickets.ListTickets;
import parsso.idman.Models.Tickets.Ticket;

import java.util.List;

@Service
public interface TicketRepo {

    HttpStatus sendTicket(Ticket ticket, String userId);

    Ticket retrieveTicket(String ticketID);

    HttpStatus reply(String ticketID, String user, Ticket ticket, String status);

    HttpStatus deleteTicket(String doer,JSONObject jsonObject);

    HttpStatus updateTicketStatus(String doer, int status, JSONObject jsonObject);

    ListTickets retrieveTicketsSend(String userId, String page, String count);

    ListTickets retrieveTicketsReceived(String userId, String page, String count, String from, String ticketId, String date);

    HttpStatus updateTicket(String userId, String ticketId, Ticket ticket);

    ListTickets retrieve(String cat, String subCat, String status, String page, String count, String from, String ticketId, String date);
}
