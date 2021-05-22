package parsso.idman.RepoImpls;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Tickets.ListTickets;
import parsso.idman.Models.Tickets.Ticket;
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

    final String collection =  Variables.col_tickets;


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
    public HttpStatus reply(String ticketID, String supporter,Ticket replyTicket) {

        Ticket priorTicket = retrieveTicket(ticketID);
        priorTicket.setTo(supporter);
        priorTicket.setStatus(1);
        if (priorTicket.getChatID()== null)
            priorTicket.setChatID(UUID.randomUUID().toString());

        mongoTemplate.save(priorTicket,collection);

        //2
        replyTicket.setID(UUID.randomUUID().toString());
        replyTicket.setCategory(priorTicket.getCategory());
        replyTicket.setSubCategory(priorTicket.getSubCategory());
        replyTicket.setStatus(1);
        replyTicket.setSubject(priorTicket.getSubject());
        replyTicket.setChatID(priorTicket.getChatID());
        replyTicket.setFrom(priorTicket.getTo());
        replyTicket.setTo(priorTicket.getFrom());
        replyTicket.setCreationTime(new Date().getTime());
        mongoTemplate.save(replyTicket,collection);

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

    private int ticketsCount(){
        return (int) mongoTemplate.count(new Query(), collection);
    }

    @Override
    public List<Ticket> retrieve(String cat, String subCat, String status) {
        //int page,count;



        Query query;
        if (status.equals(""))
            query = new Query(new Criteria());
        else
            query = new Query(Criteria.where("status").is(Integer.valueOf(status)));

        //if(page != 0) query.skip(page-1*count).limit(count);

        if (!cat.equals(""))
                query.addCriteria(Criteria.where("category").is(cat));
            if (!subCat.equals(""))
                query.addCriteria(Criteria.where("subCategory").is(subCat));

            int ticketCount  = ticketsCount();
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
