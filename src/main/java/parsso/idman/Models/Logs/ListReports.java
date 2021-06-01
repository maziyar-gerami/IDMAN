package parsso.idman.Models.Logs;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter

public class ListReports {
    long size;
    int pages;
    List<Report> reportsList;


    public ListReports(long size, int pages, List<Report> relativeEvents) {
        this.size = size;
        this.pages = pages + 1;
        this.reportsList = relativeEvents;
    }


    public ListReports() {

    }
}
