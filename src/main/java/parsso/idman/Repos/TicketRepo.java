package parsso.idman.Repos;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Ticket;

import java.util.List;

@Service
public interface TicketRepo {

    Ticket retrieveTicket(String ticketID);

    List<Ticket> retrieveTicketsReceived(String userId);

    List<Ticket> retrieveChat(String chatID);

    List<Ticket> retrieveTicketsSend(String userId);

    HttpStatus deleteTicket(String ticketID);

    HttpStatus deleteChat(String chatID);

    HttpStatus sendTicket(Ticket ticket);

    HttpStatus updateTicket(String ticketId, Ticket ticket);
}
