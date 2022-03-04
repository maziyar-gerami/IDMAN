package parsso.idman.repoImpls.tickets.subclass;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;

import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.tickets.Message;
import parsso.idman.models.tickets.Ticket;
import parsso.idman.repos.UserRepo;

public class CreateTickets {
    MongoTemplate mongoTemplate;
    UniformLogger uniformLogger;
    UserRepo.UsersOp.Retrieve usersOpRetrieve;

    Logger logger;

    public CreateTickets(MongoTemplate mongoTemplate,UniformLogger uniformLogger,UserRepo.UsersOp.Retrieve usersOpRetrieve){
        this.uniformLogger = uniformLogger;
        this.mongoTemplate = mongoTemplate;
        this.usersOpRetrieve = usersOpRetrieve;
    }

    public HttpStatus sendTicket(Ticket ticket, String userid) {
        try {
            logger = LogManager.getLogger(userid);

            List<Message> messages = new LinkedList<>();
            messages.add(new Message(usersOpRetrieve.retrieveUsers(userid), ticket.getMessage()));

            Ticket ticketToSave = new Ticket(userid, ticket.getSubject(), messages);

            mongoTemplate.save(ticketToSave, Variables.col_tickets);
            uniformLogger.info(userid, new ReportMessage(Variables.MODEL_TICKETING, ticketToSave.get_id(), "", Variables.ACTION_CREATE, Variables.RESULT_SUCCESS, ""));

            return HttpStatus.OK;
        } catch (Exception e) {
            uniformLogger.error(userid, new ReportMessage(Variables.MODEL_TICKETING, ticket.get_id(), "", Variables.ACTION_CREATE, Variables.RESULT_FAILED, ""));

            return HttpStatus.FORBIDDEN;
        }
    }


    public HttpStatus reply(String ticketID, String userid, Ticket replyTicket) {

        logger = LogManager.getLogger(userid);
        int st;

        try {
            st = replyTicket.getStatus();
        } catch (Exception e) {
            st = 1;
        }
        if (st == 0) st = 1;

        Ticket ticket = new RetrieveTickets(mongoTemplate).retrieve(ticketID);

        List<Message> messages = ticket.getMessages();
        ticket.setModifiedTime(new Date().getTime());
        String to;
        if (userid.equalsIgnoreCase(ticket.getLastFrom()))
            to = ticket.getLastTo();
        else
            to = ticket.getLastFrom();

        if (ticket.getTo().equalsIgnoreCase("SUPPORTER"))
            ticket.setTo(userid);
        else
            ticket.setTo(ticket.getTo());

        //check if closed or reopen, add new message
        if (ticket.getStatus() < st && st == 2) {
            messages.add(new Message(usersOpRetrieve.retrieveUsers(userid), usersOpRetrieve.retrieveUsers(to), replyTicket.getMessage()));
            messages.add(new Message(usersOpRetrieve.retrieveUsers(userid), "CLOSE", true));
        } else if (ticket.getStatus() > st) {
            messages.add(new Message(usersOpRetrieve.retrieveUsers(userid), "REOPEN", true));
            messages.add(new Message(usersOpRetrieve.retrieveUsers(userid), usersOpRetrieve.retrieveUsers(to), replyTicket.getMessage()));
        } else
            messages.add(new Message(usersOpRetrieve.retrieveUsers(userid), usersOpRetrieve.retrieveUsers(to), replyTicket.getMessage()));

        Ticket ticketToSave = new Ticket(ticket, messages);
        ticketToSave.setStatus(st);

        if (ticket.getTo().equals("SUPPORTER"))
            ticketToSave.setTo(userid);
        else
            ticketToSave.setTo(ticket.getTo());

        try {
            mongoTemplate.save(ticketToSave, Variables.col_tickets);
            uniformLogger.info(userid, new ReportMessage(Variables.MODEL_TICKETING, ticketToSave.get_id(), "", "reply", Variables.RESULT_SUCCESS, ""));
        } catch (Exception e) {

            e.printStackTrace();
            uniformLogger.warn(userid, new ReportMessage(Variables.MODEL_TICKETING, ticketToSave.get_id(), "", "reply", Variables.RESULT_FAILED, "writing to MongoDB"));

        }

        return HttpStatus.OK;
    }
    
}
