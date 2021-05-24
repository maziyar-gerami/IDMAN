package parsso.idman.Models.Tickets;


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
        this.pages = pages+1;
        this.ticketList = relativeEvents;
    }
}