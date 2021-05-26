package parsso.idman.Models.Tickets;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sun.xml.ws.developer.Serialization;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import parsso.idman.Models.Time;
import parsso.idman.Utils.Convertor.DateConverter;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Setter
@Getter
public class Ticket {
    @JsonIgnore
    ObjectId _id;
    String ID;
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



    public Ticket(){

    }

    public Ticket(String from, String subject, List<Message> messages) {
        this.from = from;
        this.to = "SUPPORTER";
        this.creationTime = new Date().getTime();
        this.ID = new Random().nextInt(899)+100+ "-"+ getCreationTime().toString().substring(7);
        this.subject = subject;
        this.messages = messages;
    }

    public Ticket(Ticket ticket,  List<Message> messages) {
        this.ID = ticket.getID();
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

    public static Ticket ticketUpdate(Ticket oldTicket, Ticket newTicket){
        try {
            oldTicket.setStatus(newTicket.getStatus());
        }catch (Exception e){}
            oldTicket.setModifiedTime(new Date().getTime());
        try {
            oldTicket.setSubject(newTicket.getSubject());
        }catch (Exception e){}

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
        return this.getMessages().get(this.getMessages().size()-1).getFrom();
    }

    public String getLastTo() {
        return this.getMessages().get(this.getMessages().size()-1).getTo();
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
