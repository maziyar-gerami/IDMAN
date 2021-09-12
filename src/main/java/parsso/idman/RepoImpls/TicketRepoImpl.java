package parsso.idman.RepoImpls;


import net.minidev.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import parsso.idman.Helpers.UniformLogger;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.Tickets.ListTickets;
import parsso.idman.Models.Tickets.Message;
import parsso.idman.Models.Tickets.Ticket;
import parsso.idman.Models.Time;
import parsso.idman.Repos.TicketRepo;
import parsso.idman.Repos.UserRepo;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class TicketRepoImpl implements TicketRepo {
	final String collection = Variables.col_tickets;
	@Autowired
	TicketRepo ticketRepo;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	UniformLogger uniformLogger;
	Logger logger;
	String model = "Tickets";
	@Autowired
	UserRepo userRepo;
	ZoneId zoneId = ZoneId.of(Variables.ZONE);

	@Override
	public HttpStatus sendTicket(Ticket ticket, String userid) {
		try {
			logger = LogManager.getLogger(userid);

			List<Message> messages = new LinkedList<>();
			messages.add(new Message(userRepo.retrieveUsers(userid), ticket.getMessage()));

			Ticket ticketToSave = new Ticket(userid, ticket.getSubject(), messages);

			mongoTemplate.save(ticketToSave, collection);
			uniformLogger.info(userid, new ReportMessage(model, ticketToSave.getID(), "", Variables.ACTION_CREATE, Variables.RESULT_SUCCESS, ""));

			return HttpStatus.OK;
		} catch (Exception e) {
			uniformLogger.error(userid, new ReportMessage(model, ticket.getID(), "", Variables.ACTION_CREATE, Variables.RESULT_FAILED, ""));

			return HttpStatus.FORBIDDEN;
		}
	}

	@Override
	public Ticket retrieveTicket(String ticketID) {

		Query query = new Query(Criteria.where("ID").is(ticketID));
		try {
			return mongoTemplate.findOne(query, Ticket.class, collection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public HttpStatus reply(String ticketID, String userid, Ticket replyTicket) throws IOException, ParseException {

		logger = LogManager.getLogger(userid);
		int st;

		try {
			st = Integer.valueOf(replyTicket.getStatus());
		} catch (Exception e) {
			st = 1;
		}
		if (st == 0) st = 1;

		Ticket ticket = retrieveTicket(ticketID);

		List<Message> messages = ticket.getMessages();
		ticket.setModifiedTime(new Date().getTime());
		String to = "";
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
			messages.add(new Message(userRepo.retrieveUsers(userid), userRepo.retrieveUsers(to), replyTicket.getMessage()));
			messages.add(new Message(userRepo.retrieveUsers(userid), "CLOSE", true));
		} else if (ticket.getStatus() > st) {
			messages.add(new Message(userRepo.retrieveUsers(userid), "REOPEN", true));
			messages.add(new Message(userRepo.retrieveUsers(userid), userRepo.retrieveUsers(to), replyTicket.getMessage()));
		} else
			messages.add(new Message(userRepo.retrieveUsers(userid), userRepo.retrieveUsers(to), replyTicket.getMessage()));

		Ticket ticketToSave = new Ticket(ticket, messages);
		ticketToSave.setStatus(st);

		if (ticket.getTo().equals("SUPPORTER"))
			ticketToSave.setTo(userid);
		else
			ticketToSave.setTo(ticket.getTo());

		try {
			mongoTemplate.remove(new Query(Criteria.where("ID").is(ticketID)), collection);
			mongoTemplate.save(ticketToSave, collection);
			uniformLogger.info(userid, new ReportMessage(model, ticketToSave.getID(), "", "reply", Variables.RESULT_SUCCESS, ""));
		} catch (Exception e) {

			e.printStackTrace();
			uniformLogger.warn(userid, new ReportMessage(model, ticketToSave.getID(), "", "reply", Variables.RESULT_FAILED, "writing to MongoDB"));

		}

		return HttpStatus.OK;
	}

	private int ticketsCount(String cat, String subcat, int status, String id, String date, boolean archive) {
		Query query = new Query();
		if (status != -1)
			query.addCriteria(Criteria.where("status").is(status));

		if (!cat.equals("")) {
			query.addCriteria(Criteria.where("category").regex(cat));
			if (!subcat.equals(""))
				query.addCriteria(Criteria.where("subCategory").regex(subcat));
		}
		if (!query.equals(""))
			query.addCriteria(Criteria.where("ID").regex(id));

		if (archive)
			query.addCriteria(Criteria.where("deleteFor").exists(true));

		if (!date.equals("")) {
			String time = new Time(Integer.valueOf(date.substring(4)), Integer.valueOf(date.substring(2, 4)), Integer.valueOf(date.substring(0, 2))).toStringDate();
			String timeStart = time + "T00:00:00.000Z";
			String timeEnd = time + "T23:59:59.000Z";

			long eventStartDate = OffsetDateTime.parse(timeStart).atZoneSameInstant(zoneId).toEpochSecond() * 1000;
			long eventEndDate = OffsetDateTime.parse(timeEnd).atZoneSameInstant(zoneId).toEpochSecond() * 1000;

			query.addCriteria(Criteria.where("creationTime").gte(eventStartDate).lte(eventEndDate));

		}
		return (int) mongoTemplate.count(query, collection);
	}

	@Override
	public HttpStatus deleteTicket(String doer, @RequestBody JSONObject jsonObject) {

		logger = LogManager.getLogger(doer);

		List<String> deleteFor = new LinkedList<>();

		ArrayList jsonArray = (ArrayList) jsonObject.get("names");
		Iterator<String> iterator = jsonArray.iterator();
		int i = 0;
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
				uniformLogger.info(doer, new ReportMessage(Variables.MODEL_TICKETING, ticket.getID(), "", Variables.ACTION_DELETE, Variables.RESULT_SUCCESS, ""));
			} catch (Exception e) {
				e.printStackTrace();
				uniformLogger.warn(doer, new ReportMessage(Variables.MODEL_TICKETING, ticket.getID(), "", Variables.ACTION_DELETE, Variables.RESULT_FAILED, "writing to MongoDB"));
				i++;
			}
		}
		if (i == 0)
			return HttpStatus.OK;

		else
			return HttpStatus.PARTIAL_CONTENT;
	}

	@Override
	public HttpStatus updateTicketStatus(String doer, int status, @RequestBody JSONObject jsonObject) throws IOException, ParseException {

		logger = LogManager.getLogger(doer);

		ArrayList jsonArray = (ArrayList) jsonObject.get("names");
		Iterator<String> iterator = jsonArray.iterator();
		int i = 0;
		String ticketID;
		while (iterator.hasNext()) {
			ticketID = iterator.next();
			Query query = new Query(Criteria.where("ID").is(ticketID));
			Ticket ticket = mongoTemplate.findOne(query, Ticket.class, collection);

			List<Message> messages = ticket.getMessages();

			//check if closed or reopen, add new message
			if (ticket.getStatus() < status && status == 2)
				messages.add(new Message(userRepo.retrieveUsers(doer), Variables.ACTION_CLOSE, true));
			else if (ticket.getStatus() > status)
				messages.add(new Message(userRepo.retrieveUsers(doer), Variables.ACTION_REOPEN, true));

			ticket.setStatus(status);
			ticket.setMessages(messages);

			try {
				mongoTemplate.remove(query, collection);
				mongoTemplate.save(ticket, collection);
				uniformLogger.info(doer, new ReportMessage(model, ticket.getID(), Variables.ATTR_STATUS, Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, ""));

			} catch (Exception e) {
				uniformLogger.info(doer, new ReportMessage(model, ticket.getID(), Variables.ATTR_STATUS, Variables.ACTION_UPDATE, Variables.RESULT_FAILED, "Saving to MongoDB"));

				i++;
			}
		}

		if (i == 0)
			return HttpStatus.OK;
		else
			return HttpStatus.PARTIAL_CONTENT;

	}

	@Override
	public ListTickets retrieveSentTickets(String userId, String page, String count, String date) {
		int skip = (Integer.valueOf(page) - 1) * Integer.valueOf(count);

		int limit = Integer.valueOf(count);

		Query query = new Query(Criteria.where("from").is(userId).and("deleteFor").ne(userId))
				.with(Sort.by(Sort.Direction.DESC, "_id")).skip(skip).limit(limit);
		List<Ticket> ticketList = mongoTemplate.find(query, Ticket.class, collection);

		if (!date.equals("")) {
			String time = new Time(Integer.valueOf(date.substring(4)), Integer.valueOf(date.substring(2, 4)), Integer.valueOf(date.substring(0, 2))).toStringDate();
			String timeStart = time + "T00:00:00.000000" + zoneId.toString().substring(3);
			String timeEnd = time + "T23:59:59.000000" + zoneId.toString().substring(3);

			long eventStartDate = OffsetDateTime.parse(timeStart).atZoneSameInstant(zoneId).toEpochSecond() * 1000;
			long eventEndDate = OffsetDateTime.parse(timeEnd).atZoneSameInstant(zoneId).toEpochSecond() * 1000;

			query.addCriteria(Criteria.where("creationTime").gte(eventStartDate).lte(eventEndDate));

		}

		int size = (int) mongoTemplate.count(query, collection);
		return new ListTickets(size, ticketList, (int) Math.ceil((double) size / (double) limit));

	}

	@Override
	public ListTickets retrieveTicketsReceived(String userId, String page, String count, String from, String ticketId, String date) {

		int skip = (Integer.valueOf(page) - 1) * Integer.valueOf(count);

		int limit = Integer.valueOf(count);

		Query query = new Query(Criteria.where("to").is(userId).and("status").is(1)).with(Sort.by(Sort.Direction.DESC, "_id")).skip(skip).limit(limit);

		query.addCriteria(Criteria.where("deleteFor").ne(userId));

		if (!from.equals(""))
			query.addCriteria(Criteria.where("from").regex(from));

		if (!ticketId.equals(""))
			query.addCriteria(Criteria.where("ID").regex(ticketId));

		if (!date.equals("")) {
			String time = new Time(Integer.valueOf(date.substring(4)), Integer.valueOf(date.substring(2, 4)), Integer.valueOf(date.substring(0, 2))).toStringDate();
			String timeStart = time + "T00:00:00.000000" + zoneId.toString().substring(3);
			String timeEnd = time + "T23:59:59.000000" + zoneId.toString().substring(3);

			long eventStartDate = OffsetDateTime.parse(timeStart).atZoneSameInstant(zoneId).toEpochSecond() * 1000;
			long eventEndDate = OffsetDateTime.parse(timeEnd).atZoneSameInstant(zoneId).toEpochSecond() * 1000;

			query.addCriteria(Criteria.where("creationTime").gte(eventStartDate).lte(eventEndDate));

		}

		List<Ticket> ticketList = mongoTemplate.find(query, Ticket.class, collection);

		int size = (int) mongoTemplate.count(query, collection);

		return new ListTickets(size, ticketList, (int) Math.ceil((double) size / (double) limit));
	}

	@Override
	public HttpStatus updateTicket(String userId, String ticketId, Ticket newTicket) {
		logger = LogManager.getLogger(userId);
		Query query = new Query(Criteria.where("ID").is(ticketId));
		Ticket oldTicket = mongoTemplate.findOne(query, Ticket.class, collection);
		Ticket ticketToSave = Ticket.ticketUpdate(oldTicket, newTicket);
		try {
			mongoTemplate.remove(query, collection);
			mongoTemplate.save(ticketToSave, collection);
			uniformLogger.info(userId, new ReportMessage(model, ticketId, "", Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, ticketToSave, ""));
			return HttpStatus.OK;
		} catch (Exception e) {
			e.printStackTrace();
			uniformLogger.warn(userId, new ReportMessage(model, ticketId, "", Variables.ACTION_UPDATE, Variables.RESULT_FAILED, ticketToSave, "writing to MongoDB"));
			return HttpStatus.FORBIDDEN;
		}
	}

	@Override
	public ListTickets retrieve(String doer, String cat, String subCat, String status, String page, String count, String from, String ticketId, String date) {

		int skip = (Integer.valueOf(page) - 1) * Integer.valueOf(count);
		int limit = Integer.valueOf(count);
		int st;
		try {
			st = Integer.valueOf(status);
		} catch (Exception e) {
			st = -1;
		}
		Query query;
		if (st == -1)
			query = new Query();
		else
			query = new Query(Criteria.where("status").is(st));

		query.addCriteria(Criteria.where("deleteFor").ne(doer));
		query.skip(skip).limit(limit);

		query.with(Sort.by(Sort.Direction.DESC, "_id"));

		if (!cat.equals(""))
			query.addCriteria(Criteria.where("category").regex(cat));
		if (!subCat.equals(""))
			query.addCriteria(Criteria.where("subCategory").regex(subCat));

		if (!from.equals(""))
			query.addCriteria(Criteria.where("from").regex(from));

		if (!ticketId.equals(""))
			query.addCriteria(Criteria.where("ID").regex(ticketId));

		if (!date.equals("")) {
			String time = new Time(Integer.valueOf(date.substring(4)), Integer.valueOf(date.substring(2, 4)), Integer.valueOf(date.substring(0, 2))).toStringDate();
			String timeStart = time + "T00:00:00.000Z";
			String timeEnd = time + "T23:59:59.000Z";

			long eventStartDate = OffsetDateTime.parse(timeStart).atZoneSameInstant(zoneId).toEpochSecond() * 1000;
			long eventEndDate = OffsetDateTime.parse(timeEnd).atZoneSameInstant(zoneId).toEpochSecond() * 1000;

			query.addCriteria(Criteria.where("creationTime").gte(eventStartDate).lte(eventEndDate));


		}

		int ticketCount = ticketsCount(cat, subCat, st, ticketId, date, false);
		List<Ticket> ticketList = mongoTemplate.find(query, Ticket.class, collection);
		return new ListTickets(ticketCount, ticketList, (int) Math.ceil((double) ticketCount / (double) limit));
	}

	@Override
	public ListTickets retrieveArchivedTickets(String doer, String cat, String subCat, String status, String page, String count, String from, String ticketId, String date) {

		int skip = (Integer.valueOf(page) - 1) * Integer.valueOf(count);
		int limit = Integer.valueOf(count);

		Query query = new Query();

		query.addCriteria(Criteria.where("deleteFor").exists(true));
		query.skip(skip).limit(limit);

		query.with(Sort.by(Sort.Direction.DESC, "_id"));

		if (!cat.equals(""))
			query.addCriteria(Criteria.where("category").regex(cat));
		if (!subCat.equals(""))
			query.addCriteria(Criteria.where("subCategory").regex(subCat));

		if (!from.equals(""))
			query.addCriteria(Criteria.where("from").regex(from));

		if (!ticketId.equals(""))
			query.addCriteria(Criteria.where("ID").regex(ticketId));

		if (!date.equals("")) {
			String time = new Time(Integer.valueOf(date.substring(4)), Integer.valueOf(date.substring(2, 4)), Integer.valueOf(date.substring(0, 2))).toStringDate();
			String timeStart = time + "T00:00:00.000Z";
			String timeEnd = time + "T23:59:59.000Z";

			long eventStartDate = OffsetDateTime.parse(timeStart).atZoneSameInstant(zoneId).toEpochSecond() * 1000;
			long eventEndDate = OffsetDateTime.parse(timeEnd).atZoneSameInstant(zoneId).toEpochSecond() * 1000;

			query.addCriteria(Criteria.where("creationTime").gte(eventStartDate).lte(eventEndDate));

		}

		int ticketCount = ticketsCount(cat, subCat, -1, ticketId, date, true);
		List<Ticket> ticketList = mongoTemplate.find(query, Ticket.class, collection);
		return new ListTickets(ticketCount, ticketList, (int) Math.ceil((double) ticketCount / (double) limit));
	}
}
