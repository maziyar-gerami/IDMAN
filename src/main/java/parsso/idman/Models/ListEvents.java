package parsso.idman.Models;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter

public class ListEvents {
    long size;
    int pages;
    List <Event> eventList;


    public ListEvents(long size, int pages, List<Event> relativeEvents) {
        this.size = size;
        this.pages = pages+1;
        this.eventList = relativeEvents;
    }

    public ListEvents() {

    }
}
