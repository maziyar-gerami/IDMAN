package parsso.idman.Repos.tickets;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Tickets.ListTickets;
import parsso.idman.Models.Tickets.Ticket;

import java.io.IOException;

@Service
public interface CreateTicket {
	HttpStatus sendTicket(Ticket ticket, String userId);
}
