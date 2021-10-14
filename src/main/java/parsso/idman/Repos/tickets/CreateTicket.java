package parsso.idman.Repos.tickets;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Tickets.Ticket;

@Service
public interface CreateTicket {
	HttpStatus sendTicket(Ticket ticket, String userId);
}
