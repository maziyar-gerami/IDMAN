package parsso.idman.impls.logs;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import parsso.idman.helpers.LogTime;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.Event;
import parsso.idman.models.other.Time;
import parsso.idman.repos.LogsRepo;

@Service
public class EventsRepoImpl implements LogsRepo.EventRepo {
  final MongoTemplate mongoTemplate;

  @Autowired
  public EventsRepoImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Event.ListEvents retrieve(String userId, String startDate, String endDate, int p, int n, String action) {
    Query query = new Query();
    long[] range = null;

    if (!userId.equals("")) {
      query.addCriteria(Criteria.where("principalId").is(userId));
    }

    if (!action.equals("")){
      if (action.equals("success")){
        query.addCriteria(Criteria.where("type").regex("CasTicketGrantingTicketCreatedEvent"));
      } else if (action.equals("failure")){
        query.addCriteria(Criteria.where("type").regex("CasAuthenticationTransactionFailureEvent"));
      } else {
        return null;
      }

    }

    range = LogTime.rangeCreator(startDate, endDate);

    if (range != null) {
      query.addCriteria(Criteria.where("_id")
          .gte(range[0]).lte(range[1]));
    }

    long size = mongoTemplate.count(query, Event.class, Variables.col_casEvent);

    query.skip((long) (p - 1) * n).limit(n).with(Sort.by(Sort.Direction.DESC, "_id"));

    List<Event> events = mongoTemplate.find(query, Event.class, Variables.col_casEvent);

    return new Event.ListEvents(events, size, (int) Math.ceil(size / (float) n));
  }

  @Override
  public Event.ListEvents retrieveListSizeEvents(int p, int n, String action) {
    List<Event> allEvents = analyze((p - 1) * n, n, action);
    long size = mongoTemplate.getCollection(Variables.col_casEvent).countDocuments();

    return new Event.ListEvents(size, (int) Math.ceil((double) size / (double) n), eventsSetTime(allEvents));
  }

  @Override
  public List<Event> analyze(int skip, int limit, String action) {
    Query query = new Query();

    if (!action.equals("")){
      if (action.equals("success")){
        query.addCriteria(Criteria.where("type").is("CasTicketGrantingTicketCreatedEvent"));
      } else if (action.equals("failure")){
        query.addCriteria(Criteria.where("type").is("CasAuthenticationTransactionFailureEvent"));
      } else {
        return null;
      }

    }

    query.skip(skip).limit(limit).with(Sort.by(Sort.Direction.DESC, "_id"));
    List<Event> le = mongoTemplate.find(query, Event.class, Variables.col_casEvent);
    return eventsSetTime(le);
  }

  private List<Event> eventsSetTime(List<Event> le) {
    for (Event event : le) {
      ZonedDateTime eventDate = OffsetDateTime.parse(event.getCreationTime())
          .atZoneSameInstant(ZoneId.of(Variables.ZONE));
      Time time1 = new Time(eventDate.getYear(), eventDate.getMonthValue(), eventDate.getDayOfMonth(),
          eventDate.getHour(), eventDate.getMinute(), eventDate.getSecond());
      event.setTime(time1);
    }
    return le;
  }
}
