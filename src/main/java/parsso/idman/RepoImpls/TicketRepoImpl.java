package parsso.idman.RepoImpls;


import com.google.gson.JsonObject;
import net.minidev.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.Tickets.ListTickets;
import parsso.idman.Models.Tickets.Message;
import parsso.idman.Models.Tickets.Ticket;
import parsso.idman.Models.Users.User;
import parsso.idman.Repos.TicketRepo;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

@Service
public class TicketRepoImpl implements TicketRepo {

    @Autowired
    TicketRepo ticketRepo;

    @Autowired
    MongoTemplate mongoTemplate;

    final String collection =  Variables.col_tickets;

    Logger logger ;
    String model = "Tickets";


    @Override
    public HttpStatus sendTicket(Ticket ticket, String userid) {
        try {
            logger = LogManager.getLogger(userid);

            List<Message> messages = new LinkedList<>();
            messages.add(new Message(userid, ticket.getMessage()));

            Ticket ticketToSave = new Ticket(userid, ticket.getSubject(),messages);

            mongoTemplate.save(ticketToSave,collection);
            logger.warn(new ReportMessage(model,ticketToSave.getID(),"", "create", "success", ""));

            return  HttpStatus.OK;
        }catch (Exception e) {
            logger.warn(new ReportMessage(model,ticket.getID(),"", "create", "failed", ""));

            return HttpStatus.FORBIDDEN;
        }
    }

    @Override
    public Ticket retrieveTicket(String ticketID) {

        Query query = new Query(Criteria.where("ID").is(ticketID));
        try {
            return mongoTemplate.findOne(query, Ticket.class , collection);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HttpStatus reply(String ticketID, String userid,Ticket replyTicket, String status) {

        logger = LogManager.getLogger(userid);
        int st=-1;

        try{
            st = Integer.valueOf(status);
        }catch (Exception e){
        }

        Ticket ticket = retrieveTicket(ticketID);

        List<Message> messages = ticket.getMessages();
        ticket.setModifiedTime(new Date().getTime());
        String to = "";
        if (userid.equalsIgnoreCase(ticket.getLastFrom()))
            to = ticket.getLastTo();
        else
            to = ticket.getLastFrom();

        ticket.setTo(userid);
        messages.add(new Message(userid, to, replyTicket.getMessage()));

        Ticket ticketToSave = new Ticket(ticket,messages);
        if (st!=-1)
        ticketToSave.setStatus(st);

        if(ticket.getTo().equals("SUPPORTER"))
            ticketToSave.setTo(userid);

        try {
            mongoTemplate.remove(new Query(Criteria.where("ID").is(ticketID)), collection);
            mongoTemplate.save(ticketToSave, collection);
            logger.warn(new ReportMessage(model,ticketToSave.getID(),"", "reply", "success", ""));
        }catch (Exception e){

            logger.warn(new ReportMessage(model,ticketToSave.getID(),"", "reply", "failed", "writing to MongoDB"));

        }

        return HttpStatus.OK;
    }

    private int ticketsCount(String cat, String subcat, int status){
        Query query = new Query();
        if (status != -1)
            query.addCriteria(Criteria.where("status").is(status));

        if (!cat.equals("")) {
            query.addCriteria(Criteria.where("category").is(cat));
            if (!subcat.equals(""))
                query.addCriteria(Criteria.where("subCategory").is(subcat));
        }
        return (int) mongoTemplate.count(query, collection);
    }

    @Override
    public HttpStatus deleteTicket(String doer, @RequestBody JSONObject jsonObject) {
        List<String> deleteFor = new LinkedList<>();


        ArrayList jsonArray = (ArrayList) jsonObject.get("names");
        Iterator<String> iterator = jsonArray.iterator();
        int i=0;
        while (iterator.hasNext()) {
            Ticket ticket = retrieveTicket(iterator.next());

            if (ticket.getDeleteFor() != null)
                deleteFor = ticket.getDeleteFor();

            //if (ticket.getStatus() == 0) {
                mongoTemplate.remove(ticket, collection);
                //continue;
            //}

            deleteFor.add(doer);
            ticket.setDeleteFor(deleteFor);

            try {
                mongoTemplate.save(ticket, collection);
                logger.warn(new ReportMessage(model,ticket.getID(),"", "remove", "success", ""));
            } catch (Exception e) {
                logger.warn(new ReportMessage(model,ticket.getID(),"", "remove", "failed", "writing to MongoDB"));
                i++;
            }
        }
        if (i==0)
            return HttpStatus.OK;

        else
            return HttpStatus.PARTIAL_CONTENT;
    }

    @Override
    public HttpStatus updateTicketStatus(String doer, int status, @RequestBody JSONObject jsonObject) {

        logger = LogManager.getLogger(doer);


        ArrayList jsonArray = (ArrayList) jsonObject.get("names");
        Iterator<String> iterator = jsonArray.iterator();
        int i = 0;
        String ticketID;
        while (iterator.hasNext()) {
            ticketID = iterator.next();
            Query query = new Query(Criteria.where("ID").is(ticketID));
            Ticket ticket = mongoTemplate.findOne(query, Ticket.class, collection);
            ticket.setStatus(status);
            try {
                mongoTemplate.remove(query, collection);
                mongoTemplate.save(ticket, collection);
                logger.warn(new ReportMessage(model,ticket.getID(),"", "update status", "success", ""));

            } catch (Exception e) {
                logger.warn(new ReportMessage(model,ticket.getID(),"", "update status", "failed", "writing to MongoDB"));

                i++;
            }
        }

        if (i==0)
            return HttpStatus.OK;
        else
            return HttpStatus.PARTIAL_CONTENT;

    }

    @Override
    public ListTickets retrieveTicketsSend(String userId, String page, String count) {
        int skip = (Integer.valueOf(page)-1) * Integer.valueOf(count);

        int limit = Integer.valueOf(count);

        Query query = new Query(Criteria.where("from").is(userId)).skip(skip).limit(limit);
        List<Ticket> ticketList = null;

            ticketList =  mongoTemplate.find(query, Ticket.class ,collection);

            int size = (int)mongoTemplate.count(query,collection);
            return new ListTickets(size,ticketList, (int) Math.floor(size/limit));

    }

    @Override
    public ListTickets retrieveTicketsReceived (String userId, String page, String count) {

        int skip = (Integer.valueOf(page)-1) * Integer.valueOf(count);

        int limit = Integer.valueOf(count);

        Query query = new Query(Criteria.where("to").is(userId).and("status").is(1)).skip(skip).limit(limit);

        List<Ticket> ticketList = null;

        ticketList =  mongoTemplate.find(query, Ticket.class ,collection);

        int size = (int)mongoTemplate.count(query,collection);

        return new ListTickets(size,ticketList, (int) Math.floor(size/limit));
    }

    @Override
    public HttpStatus updateTicket(String userId, String ticketId, Ticket newTicket) {
        logger = LogManager.getLogger(userId);
        Query query = new Query(Criteria.where("ID").is(ticketId));
        Ticket oldTicket = mongoTemplate.findOne(query, Ticket.class,collection);
        Ticket ticketToSave = Ticket.ticketUpdate(oldTicket, newTicket);
        try {
            mongoTemplate.remove(query, collection);
            mongoTemplate.save(ticketToSave, collection);
            logger.warn(new ReportMessage(model,ticketId,"", "update", "success", ""));
            return HttpStatus.OK;
        }catch (Exception e){
            logger.warn(new ReportMessage(model,ticketId,"", "update", "failed", "writing to MongoDB"));
            return HttpStatus.FORBIDDEN;
        }
    }

    @Override
    public ListTickets retrieve(String cat, String subCat, String status, String page, String count) {
        int skip = (Integer.valueOf(page)-1) * Integer.valueOf(count);
        int limit = Integer.valueOf(count);
        int st;
        try {
            st = Integer.valueOf(status);
        } catch (Exception e){
            st = -1;
        }
        Query query;
        if (status.equals(""))
            query = new Query(new Criteria());
        else
            query = new Query(Criteria.where("status").is(st));

        query.skip(skip).limit(limit);

        if (!cat.equals(""))
                query.addCriteria(Criteria.where("category").is(cat));
            if (!subCat.equals(""))
                query.addCriteria(Criteria.where("subCategory").is(subCat));

            int ticketCount  = ticketsCount(cat,subCat,st);
        List<Ticket> ticketList =  mongoTemplate.find(query, Ticket.class,collection);
        return  new ListTickets(ticketCount,ticketList, (int) Math.floor(ticketCount/limit));
    }
}
