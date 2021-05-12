package parsso.idman.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import parsso.idman.Utils.Convertor.DateConverter;

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
    @JsonIgnore
    Long creationTime;
    @JsonIgnore
    Long modifiedTime;
    Time creationDateTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Time modifiedDateTime;

    public Time getCreationDateTime() {
        return Time.longToPersianTime(this.creationTime);
    }


    public Time getModifiedDateTime() {
        if (this.modifiedTime != null)
            return Time.longToPersianTime(this.modifiedTime);
        else
            return null;
    }

}
