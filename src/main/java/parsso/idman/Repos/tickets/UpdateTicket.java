package parsso.idman.Repos.tickets;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Tickets.Ticket;

import java.io.IOException;

@Service
public interface UpdateTicket {

	HttpStatus reply(String ticketID, String user, Ticket ticket) throws IOException, ParseException;

	HttpStatus updateTicketStatus(String doer, int status, JSONObject jsonObject) throws IOException, ParseException;

	HttpStatus updateTicket(String userId, String ticketId, Ticket ticket);

}
