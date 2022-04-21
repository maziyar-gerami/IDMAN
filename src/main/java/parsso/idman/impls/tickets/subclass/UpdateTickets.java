package parsso.idman.impls.tickets.subclass;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;

import net.minidev.json.JSONObject;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.tickets.Message;
import parsso.idman.models.tickets.Ticket;
import parsso.idman.repos.UserRepo;

@SuppressWarnings("unchecked")
public class UpdateTickets {
  MongoTemplate mongoTemplate;
  UniformLogger uniformLogger;
  UserRepo.UsersOp.Retrieve usersOpRetrieve;
  Logger logger;

  public UpdateTickets(MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
  }

  public UpdateTickets(MongoTemplate mongoTemplate, UniformLogger uniformLogger,
      UserRepo.UsersOp.Retrieve usersOpRetrieve) {
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
    this.usersOpRetrieve = usersOpRetrieve;
  }

  public HttpStatus update(String userId, String ticketId, Ticket newTicket) {
    logger = LogManager.getLogger(userId);
    Query query = new Query(Criteria.where("_id").is(ticketId));
    Ticket oldTicket = mongoTemplate.findOne(query, Ticket.class, Variables.col_tickets);
    Ticket ticketToSave = Ticket.ticketUpdate(oldTicket, newTicket);
    try {
      mongoTemplate.save(ticketToSave, Variables.col_tickets);
      uniformLogger.info(userId, new ReportMessage(Variables.MODEL_TICKETING, ticketId, "",
          Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, ticketToSave, ""));
      return HttpStatus.OK;
    } catch (Exception e) {
      e.printStackTrace();
      uniformLogger.warn(userId, new ReportMessage(Variables.MODEL_TICKETING, ticketId, "",
          Variables.ACTION_UPDATE, Variables.RESULT_FAILED, ticketToSave, "writing to MongoDB"));
      return HttpStatus.FORBIDDEN;
    }
  }

  public HttpStatus status(String doer, int status, @RequestBody JSONObject jsonObject) {

    logger = LogManager.getLogger(doer);

    ArrayList<String> jsonArray = (ArrayList<String>) jsonObject.get("names");
    Iterator<String> iterator = jsonArray.iterator();
    int i = 0;
    String ticketID;
    while (iterator.hasNext()) {
      ticketID = iterator.next();
      Query query = new Query(Criteria.where("ID").is(ticketID));
      Ticket ticket = mongoTemplate.findOne(query, Ticket.class, Variables.col_tickets);

      List<Message> messages = Objects.requireNonNull(ticket).getMessages();

      // check if closed or reopen, add new message
      if (ticket.getStatus() < status && status == 2)
        messages.add(new Message(usersOpRetrieve.retrieveUsers(doer), Variables.ACTION_CLOSE, true));
      else if (ticket.getStatus() > status)
        messages.add(new Message(usersOpRetrieve.retrieveUsers(doer), Variables.ACTION_REOPEN, true));

      ticket.setStatus(status);
      ticket.setMessages(messages);

      try {
        mongoTemplate.save(ticket, Variables.col_tickets);
        uniformLogger.info(doer, new ReportMessage(Variables.MODEL_TICKETING, ticket.get_id(),
            Variables.ATTR_STATUS, Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, ""));

      } catch (Exception e) {
        uniformLogger.info(doer, new ReportMessage(Variables.MODEL_TICKETING, ticket.get_id(),
            Variables.ATTR_STATUS, Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Saving to MongoDB"));

        i++;
      }
    }

    if (i == 0)
      return HttpStatus.OK;
    else
      return HttpStatus.PARTIAL_CONTENT;

  }
}
