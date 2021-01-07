package parsso.idman.Repos;


import org.springframework.http.HttpStatus;
import parsso.idman.Models.Ticket;

import java.util.List;


public interface TicketRepo {

    Ticket retrieveTicket(long ticketID);

    List<Ticket> retrieveTickets(String userId);

    HttpStatus deleteTicket(long ticketID);

    HttpStatus sendTicket(Ticket ticket);

    HttpStatus updateTicket(long ticketId, Ticket ticket);

}
