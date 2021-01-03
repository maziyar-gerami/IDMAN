package parsso.idman.RepoImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Event;
import parsso.idman.Models.ListEvents;
import parsso.idman.Models.Time;
import parsso.idman.Repos.EventRepo;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.utils.Convertor.DateConverter;
import parsso.idman.utils.Query.QueryDomain;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
public class EventRepoImpl implements EventRepo {

    private static String mainCollection = "MongoDbCasEventRepository";

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    QueryDomain queryDomain;
    @Autowired
    ServiceRepo serviceRepo;

    @Override
    public ListEvents getListSizeEvents(int p, int n) {
        p= inverseP(p,n);
        Query query = new Query();
        int skip = (p-1)*n;
        query.skip(skip);
        query.limit(n);
        query.with(Sort.by(Sort.Direction.ASC,"_id"));
        List<Event> events = mongoTemplate.find(query,Event.class,mainCollection);
        Collections.reverse(events);
        long size =  mongoTemplate.getCollection(mainCollection).countDocuments();
        return new ListEvents(size,(int) Math.ceil(size/n),events);
    }

    private int inverseP(int p,int n){
        long size =  mongoTemplate.getCollection(mainCollection).countDocuments();
        int pages = (int) Math.ceil(size/n);
        return pages-(p-1);
    }

    @Override
    public ListEvents getListUserEvents(String userId, int p, int n)  {
        ListEvents listEvents = new ListEvents();
        Query query = new Query(Criteria.where("principalId").is(userId));
        query.skip((p-1)*(n));
        query.limit(n);
        query.with(Sort.by(Sort.Direction.DESC,"_id"));
        List<Event> eventList =  mongoTemplate.find(query, Event.class,mainCollection);
        listEvents.setSize(eventList.size());
        listEvents.setPages((int) Math.ceil(eventList.size()/n));
        listEvents.setEventList(eventList);
        return listEvents;
    }

    @Override
    public ListEvents getEventsByDate(String date, int p, int n) throws ParseException, IOException, org.json.simple.parser.ParseException {
        p= inverseP(p,n);
        List<Event> allEvents = analyze(mainCollection,(p-1)*n,n);
        long size =  mongoTemplate.getCollection(mainCollection).countDocuments();
        return new ListEvents(size,(int) Math.ceil(size/n),allEvents);
    }

    @Override
    public ListEvents getListUserEventByDate(String date, String userId, int skip, int limit) throws ParseException, IOException, org.json.simple.parser.ParseException {

        Query query = new Query(Criteria.where("principalId").is(userId));
        long size =  mongoTemplate.count(query, ListEvents.class,mainCollection);
        query.skip((skip-1)*(limit));
        query.limit(limit);
        query.with(Sort.by(Sort.Direction.DESC,"_id"));
        List<Event> eventList =  mongoTemplate.find(query, Event.class,mainCollection);
        int pages = (int) Math.ceil(size/limit);
        ListEvents listEvents = new ListEvents(size,pages,eventList);
        return listEvents;
    }

    @Override
    public List<Event> analyze(String collection,int skip, int limit) {

        List<Event> events;
        Query query = new Query();
        query.skip(skip);
        query.limit(limit);
        query.with(Sort.by(Sort.Direction.ASC,"_id"));
        events = mongoTemplate.find(query,Event.class,collection);
        Collections.reverse(events);
        return events;
    }
}


