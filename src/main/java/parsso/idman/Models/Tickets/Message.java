package parsso.idman.Models.Tickets;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.Models.Time;
import parsso.idman.Models.Users.User;

import java.util.Date;

@Setter
@Getter
public class Message {
    private String to;
    private String from;
    private String toDisplayName;
    private String fromDisplayName;
    private String body;
    private Time creationTime;
    @JsonIgnore
    private long creationLong;

    public Message() {

    }

    public Message(User user, String body) {
        this.to = "SUPPORTER";
        this.toDisplayName = "پشتیبان";
        this.from = user.getUserId();
        this.fromDisplayName = user.getDisplayName();
        this.body = body;
        this.creationLong = new Date().getTime();
    }

    public Message(User from, User to, String body) {
        this.from = from.getUserId();
        this.fromDisplayName = from.getDisplayName();
        this.to = to.getUserId();
        this.toDisplayName = to.getDisplayName();
        this.body = body;
        this.creationLong = new Date().getTime();
    }

    public Time getCreationTime() {
        return Time.longToPersianTime(creationLong);
    }
}
