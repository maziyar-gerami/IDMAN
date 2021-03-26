package parsso.idman.Models;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter

public class ListLogs {
    long size;
    int pages;
    List<Log> logList;


    public ListLogs(long size, int pages, List<Log> relativeEvents) {
        this.size = size;
        this.pages = pages + 1;
        this.logList = relativeEvents;
    }



    public ListLogs() {

    }
}
