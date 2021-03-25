package parsso.idman.Models;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Ticket {
    String ID;
    String to;
    String from;
    String subject;
    String chatID;
    String message;
    String category;
    int status;
    String subCategory;
    Long creationTime;
    Long modifiedTime;
}
