package parsso.idman.Models.Users;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter

public class ListUsers {
    int size;
    int pages;
    List<UsersExtraInfo> userList;


    public ListUsers(int size, List<UsersExtraInfo> relativeEvents, int pages) {
        this.size = size;
        this.pages = pages;
        this.userList = relativeEvents;
    }
}
