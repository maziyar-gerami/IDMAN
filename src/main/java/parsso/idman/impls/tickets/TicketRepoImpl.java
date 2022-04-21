package parsso.idman.impls.tickets;

import net.minidev.json.JSONObject;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.impls.tickets.subclass.CreateTickets;
import parsso.idman.impls.tickets.subclass.DeleteTickets;
import parsso.idman.impls.tickets.subclass.RetrieveTickets;
import parsso.idman.impls.tickets.subclass.UpdateTickets;
import parsso.idman.models.tickets.ListTickets;
import parsso.idman.models.tickets.Ticket;
import parsso.idman.repos.TicketRepo;
import parsso.idman.repos.UserRepo;

@Service
public class TicketRepoImpl implements TicketRepo {
  TicketRepo ticketRepo;
  MongoTemplate mongoTemplate;
  UniformLogger uniformLogger;
  Logger logger;
  UserRepo.UsersOp.Retrieve usersOpRetrieve;

  @Autowired
  public TicketRepoImpl(MongoTemplate mongoTemplate, UniformLogger uniformLogger,
      UserRepo.UsersOp.Retrieve userOpRetrieve) {
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
    this.usersOpRetrieve = userOpRetrieve;
  }

  @Override
  public HttpStatus sendTicket(Ticket ticket, String userid) {
    return new CreateTickets(mongoTemplate, uniformLogger, usersOpRetrieve).sendTicket(ticket, userid);
  }

  @Override
  public Ticket retrieveTicket(String ticketID) {
    return new RetrieveTickets(mongoTemplate).retrieve(ticketID);
  }

  @Override
  public HttpStatus reply(String ticketID, String userid, Ticket replyTicket) {

    return new CreateTickets(mongoTemplate, uniformLogger, usersOpRetrieve).reply(ticketID, userid, replyTicket);
  }

  @Override
  public HttpStatus deleteTicket(String doer, @RequestBody JSONObject jsonObject) {

    return new DeleteTickets(mongoTemplate, uniformLogger).delete(doer, jsonObject);
  }

  @Override
  public HttpStatus updateTicketStatus(String doer, int status, @RequestBody JSONObject jsonObject) {

    return new UpdateTickets(mongoTemplate, uniformLogger, usersOpRetrieve).status(doer, status, jsonObject);

  }

  @Override
  public ListTickets retrieveSentTickets(String userId, String page, String count, String date) {
    return new RetrieveTickets(mongoTemplate).sent(userId, page, count, date);
  }

  @Override
  public ListTickets retrieveTicketsReceived(String userId, String page, String count, String from, String ticketId,
      String date) {

    return new RetrieveTickets(mongoTemplate).recieved(userId, page, count, from, ticketId, date);
  }

  @Override
  public HttpStatus updateTicket(String userId, String ticketId, Ticket newTicket) {
    return new UpdateTickets(mongoTemplate, uniformLogger).update(userId, ticketId, newTicket);
  }

  @Override
  public ListTickets retrieve(String doer, String cat, String subCat, String status, String page, String count,
      String from, String ticketId, String date) {

    return new RetrieveTickets(mongoTemplate).retrieve(doer, cat, subCat, status, page, count, from, ticketId,
        date);
  }

  @Override
  public ListTickets retrieveArchivedTickets(String cat, String subCat, String page, String count, String from,
      String ticketId, String date) {

    return new RetrieveTickets(mongoTemplate).archived(cat, subCat, page, count, from, ticketId, date);
  }
}
