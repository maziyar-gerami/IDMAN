package parsso.idman.models.tickets;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.models.other.Time;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Setter
@Getter
public class Ticket {
    Object _id;
    String from;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String to;
    String lastFrom;
    String lastTo;
    String subject;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
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
    @JsonIgnore
    List<String> deleteFor;
    List<Message> messages;

    public Ticket() {
    }

    public Ticket(String from, String subject, List<Message> messages) {
        this.from = from;
        this.to = "SUPPORTER";
        this.creationTime = new Date().getTime();
        this._id = new Random().nextInt(899) + 100 + "-" + getCreationTime().toString().substring(7);
        this.subject = subject;
        this.messages = messages;
    }

    public Ticket(Ticket ticket, List<Message> messages) {
        this._id = ticket.get_id();
        this.from = ticket.getFrom();
        this.creationTime = ticket.getCreationTime();
        this.modifiedTime = new Date().getTime();
        this.subject = ticket.getSubject();
        if (ticket.getStatus() == 0)
            ticket.setStatus(1);
        else
            ticket.setStatus(ticket.getStatus());
        this.messages = messages;
    }

    public static Ticket ticketUpdate(Ticket oldTicket, Ticket newTicket) {
        try {
            oldTicket.setStatus(newTicket.getStatus());
        } catch (Exception ignored) {
        }
        oldTicket.setModifiedTime(new Date().getTime());
        try {
            oldTicket.setSubject(newTicket.getSubject());
        } catch (Exception ignored) {
        }

        return oldTicket;
    }

    public Time getCreationDateTime() {
        return Time.longToPersianTime(this.creationTime);
    }

    public Time getModifiedDateTime() {
        if (this.modifiedTime != null)
            return Time.longToPersianTime(this.modifiedTime);
        else
            return null;
    }

    public String getLastFrom() {
        return this.getMessages().get(this.getMessages().size() - 1).getFrom();
    }

    public String getLastTo() {
        return this.getMessages().get(this.getMessages().size() - 1).getTo();
    }

    public String getFrom() {
        return from.toLowerCase();
    }

    public void setFrom(String from) {
        this.from = from.toLowerCase();
    }

    public String getTo() {
        return to.toLowerCase();
    }

    public void setTo(String to) {
        this.to = to.toLowerCase();
    }
}
