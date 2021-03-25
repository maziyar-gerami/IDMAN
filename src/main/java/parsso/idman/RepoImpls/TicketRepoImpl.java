package parsso.idman.RepoImpls;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Ticket;
import parsso.idman.Models.User;
import parsso.idman.Repos.TicketRepo;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TicketRepoImpl implements TicketRepo {

    @Autowired
    TicketRepo ticketRepo;

    @Autowired
    MongoTemplate mongoTemplate;

    final String collection = "IDMAN_Tickets";


    @Override
    public Ticket retrieveTicket(String ticketID) {

        Query query = new Query(Criteria.where("ID").is(ticketID));
        try {
            return mongoTemplate.findOne(query, Ticket.class , collection);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<Ticket> retrieveTicketsSend(String userId) {

        Query query = new Query(Criteria.where("from").is(userId));
        try {
            return mongoTemplate.find(query, Ticket.class ,collection);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public HttpStatus reply(String ticketID, String supporter,Ticket ticket) {
        Ticket firstTicket = retrieveTicket(ticketID);
        firstTicket.setTo("supporter.getUserId()");
        firstTicket.setStatus(1);
        mongoTemplate.remove(new Query(Criteria.where("ID").is(ticketID)));
        mongoTemplate.save(firstTicket,collection);

        //2
        ticket.setStatus(1);
        ticket.setChatID(UUID.randomUUID().toString());
        ticket.setFrom(firstTicket.getTo());
        ticket.setTo(firstTicket.getFrom());
        mongoTemplate.save(ticket);

        return HttpStatus.OK;
    }

    @Override
    public List<Ticket> retrieveTicketsReceived (String userId) {

        Query query = new Query(Criteria.where("to").is(userId));
        try {
            return mongoTemplate.find(query, Ticket.class ,collection);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<Ticket> retrieveChat(String chatID) {
        Query query = new Query(Criteria.where("chatID").is(chatID));
        try {
            return mongoTemplate.find(query, Ticket.class ,collection);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public HttpStatus deleteTicket(String ticketID) {
        Query query = new Query(Criteria.where("ID").is(ticketID));
        try {
            mongoTemplate.remove(query, Ticket.class ,collection);
            return  HttpStatus.OK;
        }catch (Exception e){
            return HttpStatus.FORBIDDEN;
        }
    }

    @Override
    public HttpStatus deleteChat(String chatID) {
        Query query = new Query(Criteria.where("chatID").is(chatID));
        try {
            mongoTemplate.findAllAndRemove(query, Ticket.class ,collection);
            return  HttpStatus.OK;
        }catch (Exception e){
            return HttpStatus.FORBIDDEN;
        }
    }

    @Override
    public HttpStatus sendTicket(Ticket ticket, String userid) {
        try {
            ticket.setID(UUID.randomUUID().toString());
            ticket.setCreationTime(new Date().getTime());
            ticket.setFrom(userid);
            if(ticket.getChatID()==null)
                ticket.setTo("SUPPORTER");

            mongoTemplate.save(ticket,collection);
            return  HttpStatus.OK;
        }catch (Exception e) {
            return HttpStatus.FORBIDDEN;
        }
    }

    @Override
    public List<Ticket> pendingTickets(String cat, String subCat, User supporter) {
        Query query = new Query(Criteria.where("status").is(0));
        if(supporter!=null){
            if (supporter.getUsersExtraInfo().getCategory()!=null)
                query.addCriteria(Criteria.where("category").is(supporter.getUsersExtraInfo().getCategory()));
            if (supporter.getUsersExtraInfo().getCategory()!=null&&supporter.getUsersExtraInfo().getSubCategory()!=null)
                query.addCriteria(Criteria.where("subCategory").is(supporter.getUsersExtraInfo().getSubCategory()));
        }

        return mongoTemplate.find(query, Ticket.class,collection);
    }

    @Override
    public HttpStatus updateTicket(String ticketId, Ticket ticket) {
        Query query = new Query(Criteria.where("ID").is(ticketId));
        ticket.setModifiedTime(new Date().getTime());
        try {
            mongoTemplate.remove(query, collection);
            mongoTemplate.save(ticket, collection);
            return HttpStatus.OK;
        }catch (Exception e){
            return HttpStatus.FORBIDDEN;
        }
    }

    @Override
    public HttpStatus updateTicketStatus(int status, String ticketID) {
        Query query = new Query(Criteria.where("ID").is(ticketID));
        Ticket ticket = mongoTemplate.findOne(query,Ticket.class,collection);
        ticket.setStatus(status);
        try {
            mongoTemplate.remove(query, collection);
            mongoTemplate.save(ticket, collection);
            return HttpStatus.OK;
        }catch (Exception e){
            return HttpStatus.FORBIDDEN;
        }
    }
}
