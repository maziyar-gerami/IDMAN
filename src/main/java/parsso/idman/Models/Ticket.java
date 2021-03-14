package parsso.idman.Models;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Ticket {
    String ID;
    String to;
    String from;
    String chatID;
    String message;
    String category;
    String subCategory;
    Long creationTime;
    Long modifiedTime;
}
