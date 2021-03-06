package parsso.idman.Repos;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Ticket;

import java.util.List;

@Service
public interface TicketRepo {

    Ticket retrieveTicket(long ticketID);

    List<Ticket> retrieveTickets(String userId);

    HttpStatus deleteTicket(long ticketID);

    HttpStatus sendTicket(Ticket ticket);

    HttpStatus updateTicket(long ticketId, Ticket ticket);

}
