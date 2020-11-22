package parsso.idman.Models;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter

public class ListEvents {
    int size;
    int pages;
    List <Event> eventList;


    public ListEvents(int size, List<Event> relativeEvents, int pages) {
        this.size = size;
        this.pages = pages+1;
        this.eventList = relativeEvents;
    }
}
