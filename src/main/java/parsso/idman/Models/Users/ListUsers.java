package parsso.idman.Models.Users;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter

public class ListUsers {
    int size;
    int pages;
    List<SimpleUser> userList;


    public ListUsers(int size, List<SimpleUser> relativeEvents, int pages) {
        this.size = size;
        this.pages = pages + 1;
        this.userList = relativeEvents;
    }
}
