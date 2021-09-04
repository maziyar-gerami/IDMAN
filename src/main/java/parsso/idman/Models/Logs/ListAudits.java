package parsso.idman.Models.Logs;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter

public class ListAudits {
    long size;
    int pages;
    List<Audit> auditList;

    public ListAudits(List<Audit> relativeAudits, long size, int pages) {
        this.size = size;
        this.pages = pages;
        this.auditList = relativeAudits;
    }

    public ListAudits() {

    }
}
