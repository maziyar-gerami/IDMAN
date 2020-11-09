package parsso.idman.Repos;

import org.springframework.http.HttpStatus;
import parsso.idman.Models.Ticket;

import java.util.List;

public interface TicketRepo {

    List<Ticket> retrieveChat(String chatID);
    Ticket retrieveTicket (long ticketID);

    HttpStatus deleteChat (String chatID);
    HttpStatus deleteTicket (long ticketID);

    HttpStatus sendTicket (Ticket ticket);
    HttpStatus updateTicket (long ticketId, Ticket ticket);

}
