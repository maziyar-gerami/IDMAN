package parsso.idman.models.tickets;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListTickets {
    int size;
    int pages;
    List<Ticket> ticketList;

    public ListTickets(int size, List<Ticket> relativeEvents, int pages) {
        this.size = size;
        this.pages = pages;
        this.ticketList = relativeEvents;
    }
}
