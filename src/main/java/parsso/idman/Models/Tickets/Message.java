package parsso.idman.Models.Tickets;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.Models.Time;
import parsso.idman.Utils.Convertor.DateConverter;

import java.util.Date;

@Setter
@Getter
public class Message {
    private String to;
    private String from;
    private String body;
    private Time creationTime;
    @JsonIgnore
    private long creationLong;

    public Time getCreationTime() {
        return Time.longToPersianTime(creationLong);
    }

    public Message(){

    }
    public Message(String from, String body) {
        this.to = "SUPPORTER";
        this.from = from;
        this.body = body;
        this.creationLong = new Date().getTime();
    }

    public Message(String from, String to, String body) {
        this.from = from;
        this.to = to;
        this.body = body;
        this.creationLong = new Date().getTime();
    }


}
