package parsso.idman.Models;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter

public class ListAudits {
    int size;
    int pages;
    List <Audit> auditList;


    public ListAudits(int size, List<Audit> relativeAudits, int pages) {
        this.size = size;
        this.pages = pages+1;
        this.auditList = relativeAudits;
    }
}
