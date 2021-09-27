package parsso.idman.Repos.tickets;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Tickets.ListTickets;
import parsso.idman.Models.Tickets.Ticket;

import java.io.IOException;

@Service
public interface RetrieveTicket {

	Ticket retrieveTicket(String ticketID);

	ListTickets retrieveSentTickets(String userId, String page, String count, String date);

	ListTickets retrieveTicketsReceived(String userId, String page, String count, String from, String ticketId, String date);

	ListTickets retrieve(String doer, String cat, String subCat, String status, String page, String count, String from, String ticketId, String date);

	ListTickets retrieveArchivedTickets(String doer, String cat, String subCat, String status, String page, String count, String from, String ticketId, String date);
}
