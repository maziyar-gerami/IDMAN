package parsso.idman.RepoImpls;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Ticket;
import parsso.idman.Repos.TicketRepo;

import java.util.List;

@Service
public class TicketRepoImpl implements TicketRepo {

    @Autowired
    TicketRepo ticketRepo;


    @Override
    public Ticket retrieveTicket(long ticketID) {
        return null;
    }

    @Override
    public List<Ticket> retrieveTickets(String userId) {
        return null;
    }

    @Override
    public HttpStatus deleteTicket(long ticketID) {
        return null;
    }

    @Override
    public HttpStatus sendTicket(Ticket ticket) {
        return null;
    }

    @Override
    public HttpStatus updateTicket(long ticketId, Ticket ticket) {
        return null;
    }
}
