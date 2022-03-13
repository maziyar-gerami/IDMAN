package parsso.idman.repoImpls.tickets.subclass;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Time;
import parsso.idman.models.tickets.ListTickets;
import parsso.idman.models.tickets.Ticket;
import parsso.idman.repoImpls.tickets.helper.TicketsCount;

public class RetrieveTickets {
    MongoTemplate mongoTemplate;
    public RetrieveTickets(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public Ticket retrieve(String ticketID) {
        Query query = new Query(Criteria.where("_id").is(ticketID));
        try {
            return mongoTemplate.findOne(query, Ticket.class, Variables.col_tickets);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ListTickets sent(String userId, String page, String count, String date) {
        int skip = (Integer.parseInt(page) - 1) * Integer.parseInt(count);
        int limit = Integer.parseInt(count);

        Query query = new Query(Criteria.where("from").is(userId).and("deleteFor").ne(userId))
                .with(Sort.by(Sort.Direction.DESC, "_id")).skip(skip).limit(limit);
        List<Ticket> ticketList = mongoTemplate.find(query, Ticket.class, Variables.col_tickets);

        if (!date.equals("")) {
            String time = new Time(Integer.parseInt(date.substring(4)), Integer.parseInt(date.substring(2, 4)), Integer.parseInt(date.substring(0, 2))).toStringDate();
            String timeStart = time + "T00:00:00.000000" + ZoneId.of(Variables.ZONE).toString().substring(3);
            String timeEnd = time + "T23:59:59.000000" + ZoneId.of(Variables.ZONE).toString().substring(3);

            long eventStartDate = OffsetDateTime.parse(timeStart).atZoneSameInstant(ZoneId.of(Variables.ZONE)).toEpochSecond() * 1000;
            long eventEndDate = OffsetDateTime.parse(timeEnd).atZoneSameInstant(ZoneId.of(Variables.ZONE)).toEpochSecond() * 1000;

            query.addCriteria(Criteria.where("creationTime").gte(eventStartDate).lte(eventEndDate));
        }

        int size = (int) mongoTemplate.count(query, Variables.col_tickets);
        return new ListTickets(size, ticketList, (int) Math.ceil((double) size / (double) limit));
    }

    public ListTickets recieved(String userId, String page, String count, String from, String ticketId, String date) {

        int skip = (Integer.parseInt(page) - 1) * Integer.parseInt(count);
        int limit = Integer.parseInt(count);

        Query query = new Query(Criteria.where("to").is(userId).and("status").is(1)).with(Sort.by(Sort.Direction.DESC, "creatinTime")).skip(skip).limit(limit);

        query.addCriteria(Criteria.where("deleteFor").ne(userId));

        if (!from.equals(""))
            query.addCriteria(Criteria.where("from").regex(from));

        if (!ticketId.equals(""))
            query.addCriteria(Criteria.where("_id").regex(ticketId));

        if (!date.equals("")) {

            Time time = new Time(Integer.parseInt(date.substring(4)),
                    Integer.parseInt(date.substring(2, 4)),
                    Integer.parseInt(date.substring(0, 2)));
            long[] range = new Time().specificDateToEpochRange(time, ZoneId.of(Variables.ZONE));

            query = new Query(Criteria.where("creationTime").gte(range[0]).lte(range[1]));
        }

        List<Ticket> ticketList = mongoTemplate.find(query, Ticket.class, Variables.col_tickets);

        int size = (int) mongoTemplate.count(query, Variables.col_tickets);
        return new ListTickets((int) mongoTemplate.count(query, Variables.col_tickets), ticketList, (int) Math.ceil((double) size / (double) limit));
    }

    public ListTickets retrieve(String doer, String cat, String subCat, String status, String page, String count, String from, String ticketId, String date) {

        int skip = (Integer.parseInt(page) - 1) * Integer.parseInt(count);
        int limit = Integer.parseInt(count);
        int st;
        try {
            st = Integer.parseInt(status);
        } catch (Exception e) {
            st = -1;
        }
        Query query;
        if (st == -1)
            query = new Query();
        else
            query = new Query(Criteria.where("status").is(st));

        query.addCriteria(Criteria.where("deleteFor").ne(doer)).skip(skip).limit(limit).with(Sort.by(Sort.Direction.DESC, "creationTime"));

        if (!cat.equals(""))
            query.addCriteria(Criteria.where("category").regex(cat));
        if (!subCat.equals(""))
            query.addCriteria(Criteria.where("subCategory").regex(subCat));
        if (!from.equals(""))
            query.addCriteria(Criteria.where("from").regex(from));
        if (!ticketId.equals(""))
            query.addCriteria(Criteria.where("_id").regex(ticketId));
        if (!date.equals("")) {
            String time = new Time(Integer.parseInt(date.substring(4)), Integer.parseInt(date.substring(2, 4)), Integer.parseInt(date.substring(0, 2))).toStringDate();
            String timeStart = time + "T00:00:00.000Z";
            String timeEnd = time + "T23:59:59.000Z";
            long eventStartDate = OffsetDateTime.parse(timeStart).atZoneSameInstant(ZoneId.of(Variables.ZONE)).toEpochSecond() * 1000;
            long eventEndDate = OffsetDateTime.parse(timeEnd).atZoneSameInstant(ZoneId.of(Variables.ZONE)).toEpochSecond() * 1000;

            query.addCriteria(Criteria.where("creationTime").gte(eventStartDate).lte(eventEndDate));
        }

        int ticketCount = new TicketsCount(mongoTemplate).count(cat, subCat, st, ticketId, date, false);
        List<Ticket> ticketList = mongoTemplate.find(query, Ticket.class, Variables.col_tickets);
        return new ListTickets(ticketCount, ticketList, (int) Math.ceil((double) ticketCount / (double) limit));
    }

    public ListTickets archived(String cat, String subCat, String page, String count, String from, String ticketId, String date) {

        int skip = (Integer.parseInt(page) - 1) * Integer.parseInt(count);
        int limit = Integer.parseInt(count);

        Query query = new Query();

        query.addCriteria(Criteria.where("deleteFor").exists(true));
        query.skip(skip).limit(limit);

        query.with(Sort.by(Sort.Direction.DESC, "creationTime"));

        if (!cat.equals(""))
            query.addCriteria(Criteria.where("category").regex(cat));
        if (!subCat.equals(""))
            query.addCriteria(Criteria.where("subCategory").regex(subCat));

        if (!from.equals(""))
            query.addCriteria(Criteria.where("from").regex(from));

        if (!ticketId.equals(""))
            query.addCriteria(Criteria.where("_id").regex(ticketId));

        if (!date.equals("")) {
            String time = new Time(Integer.parseInt(date.substring(4)), Integer.parseInt(date.substring(2, 4)), Integer.parseInt(date.substring(0, 2))).toStringDate();
            String timeStart = time + "T00:00:00.000Z";
            String timeEnd = time + "T23:59:59.000Z";

            long eventStartDate = OffsetDateTime.parse(timeStart).atZoneSameInstant(ZoneId.of(Variables.ZONE)).toEpochSecond() * 1000;
            long eventEndDate = OffsetDateTime.parse(timeEnd).atZoneSameInstant(ZoneId.of(Variables.ZONE)).toEpochSecond() * 1000;

            query.addCriteria(Criteria.where("creationTime").gte(eventStartDate).lte(eventEndDate));

        }

        int ticketCount = new TicketsCount(mongoTemplate).count(cat, subCat, -1, ticketId, date, true);
        List<Ticket> ticketList = mongoTemplate.find(query, Ticket.class, Variables.col_tickets);
        return new ListTickets(ticketCount, ticketList, (int) Math.ceil((double) ticketCount / (double) limit));
    }
}
