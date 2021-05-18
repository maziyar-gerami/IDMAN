package parsso.idman.Repos;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Ticket;
import parsso.idman.Models.Users.User;

import java.util.List;

@Service
public interface TicketRepo {

    HttpStatus sendTicket(Ticket ticket, String userId);




    List<Ticket> retrieve(String cat, String subCat, String status);

    Ticket retrieveTicket(String ticketID);

    List<Ticket> retrieveTicketsReceived(String userId);

    List<Ticket> retrieveTicketsSend(String userId);

    HttpStatus reply(String ticketID, String user, Ticket ticket);




    List<Ticket> retrieveChat(String chatID);

    HttpStatus deleteTicket(String ticketID);

    HttpStatus deleteChat(String chatID);

    HttpStatus updateTicket(String ticketId, Ticket ticket);

    HttpStatus updateTicketStatus(int status, String ticketID);
}
