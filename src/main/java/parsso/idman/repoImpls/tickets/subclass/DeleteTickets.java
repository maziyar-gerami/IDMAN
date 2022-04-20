package parsso.idman.repoImpls.tickets.subclass;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;

import net.minidev.json.JSONObject;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.tickets.Ticket;

@SuppressWarnings({"unchecked"})
public class DeleteTickets {

    MongoTemplate mongoTemplate;
    UniformLogger uniformLogger;
    Logger logger;

    public DeleteTickets(MongoTemplate mongoTemplate,UniformLogger uniformLogger){
        this.mongoTemplate = mongoTemplate;
        this.uniformLogger = uniformLogger;
    }


    public HttpStatus delete(String doer, @RequestBody JSONObject jsonObject) {

        logger = LogManager.getLogger(doer);

        List<String> deleteFor = new LinkedList<>();

        ArrayList<String> jsonArray = (ArrayList<String>) jsonObject.get("names");
        Iterator<String> iterator = jsonArray.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Ticket ticket = new RetrieveTickets(mongoTemplate).retrieve(iterator.next());

            if (ticket.getDeleteFor() != null)
                deleteFor = ticket.getDeleteFor();

            //if (ticket.getStatus() == 0) {
            mongoTemplate.remove(ticket, Variables.col_tickets);
            //continue;
            //}

            deleteFor.add(doer);
            ticket.setDeleteFor(deleteFor);

            try {
                mongoTemplate.save(ticket, Variables.col_tickets);
                uniformLogger.info(doer, new ReportMessage(Variables.MODEL_TICKETING, ticket.get_id(), "", Variables.ACTION_DELETE, Variables.RESULT_SUCCESS, ""));
            } catch (Exception e) {
                e.printStackTrace();
                uniformLogger.warn(doer, new ReportMessage(Variables.MODEL_TICKETING, ticket.get_id(), "", Variables.ACTION_DELETE, Variables.RESULT_FAILED, "writing to MongoDB"));
                i++;
            }
        }
        if (i == 0)
            return HttpStatus.OK;

        else
            return HttpStatus.PARTIAL_CONTENT;
    }
}
