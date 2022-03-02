package parsso.idman.models.tickets;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.models.other.Time;
import parsso.idman.models.users.User;

import java.util.Date;

@Setter
@Getter
public class Message {
    private String to;
    private String from;
    private String toDisplayName;
    private String fromDisplayName;
    private String body;
    private boolean close;
    private boolean reOpen;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Time closeTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Time reOpenTime;
    @JsonIgnore
    private long creationLong;
    @JsonIgnore
    private long closeLong;
    @JsonIgnore
    private long reOpenLong;

    public Message() {

    }

    public Message(User user, String body) {
        this.to = "SUPPORTER";
        this.toDisplayName = "پشتیبان";
        this.from = user.get_id().toString();
        this.fromDisplayName = user.getDisplayName();
        this.body = body;
        this.creationLong = new Date().getTime();
    }

    public Message(User from, User to, String body) {
        this.from = from.get_id().toString();
        this.fromDisplayName = from.getDisplayName();
        this.to = to.get_id().toString();
        this.toDisplayName = to.getDisplayName();
        this.body = body;
        this.creationLong = new Date().getTime();
    }

    public Message(User by, String action, boolean state) {
        this.from = by.get_id().toString();
        this.to = "SYSTEM";
        this.fromDisplayName = by.getDisplayName();
        long now = new Date().getTime();
        this.creationLong = now;
        if (action.equals("CLOSE")) {
            this.close = state;
            this.closeLong = now;
        } else if (action.equals("REOPEN")) {
            this.reOpen = true;
            this.reOpenLong = now;
        }
    }

    public Time getCreationTime() {
        return Time.longToPersianTime(creationLong);
    }

    public Time getCloseTime() {
        return Time.longToPersianTime(closeLong);
    }

    public Time getReOpenTime() {
        return Time.longToPersianTime(reOpenLong);
    }
}
