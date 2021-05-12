package parsso.idman.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Setter
@Getter
public class Ticket {
    @JsonIgnore
    ObjectId _id;
    String ID;
    String to;
    String from;
    String subject;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String chatID;
    String message;
    String category;
    int status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String subCategory;
    Long creationTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Long modifiedTime;
}
