package parsso.idman.Models.Tickets;


import parsso.idman.Models.Users.UsersExtraInfo;

import java.util.List;

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
