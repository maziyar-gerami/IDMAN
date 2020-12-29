package parsso.idman.RepoImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Event;
import parsso.idman.Models.ListEvents;
import parsso.idman.Repos.EventRepo;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.utils.Query.QueryDomain;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventRepoImpl implements EventRepo {

    public static String path;
    public static String mainCollection = "MongoDbCasEventRepository";

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    QueryDomain queryDomain;
    @Autowired
    ServiceRepo serviceRepo;

    @Override
    public ListEvents getListSizeEvents(int p, int n) {
        p= inverseP(p,n);
        List<Event> allEvents = analyze(mainCollection,(p-1)*n,n);
        long size =  mongoTemplate.getCollection(mainCollection).countDocuments();
        int pages = (int) Math.ceil(size/n);
        return new ListEvents(size,pages,allEvents);
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
        List<Event> eventList =  mongoTemplate.find(query, Event.class,mainCollection);
        Collections.reverse(eventList);
        listEvents.setSize(eventList.size());
        listEvents.setPages((int) Math.ceil(eventList.size()/n));
        listEvents.setEventList(eventList);
        return listEvents;
    }

    @Override
    public ListEvents getEventsByDate(String date, int p, int n) throws ParseException, IOException, org.json.simple.parser.ParseException {
        p= inverseP(p,n);
        List<Event> events = analyze(mainCollection,n*(p-1),n);

        ListEvents listEvents = new ListEvents();
        Query query = new Query(Criteria.where("principalId").is(date));
        listEvents.setSize((int) mongoTemplate.count(query,mainCollection));
        listEvents.setPages((int) Math.ceil(listEvents.getSize()/n));
        listEvents.setEventList(events);
        return listEvents;
    }

    @Override
    public ListEvents getListUserEventByDate(String date, String userId, int skip, int limit) throws ParseException, IOException, org.json.simple.parser.ParseException {

        Query query = new Query(Criteria.where("principalId").is(userId));
        long size =  mongoTemplate.count(query, ListEvents.class,mainCollection);
        query.skip((skip-1)*(limit));
        query.limit(limit);
        List<Event> eventList =  mongoTemplate.find(query, Event.class,mainCollection);
        int pages = (int) Math.ceil(size/limit);
        ListEvents listEvents = new ListEvents(size,pages,eventList);
        return listEvents;
    }

    @Override
    public List<Event> analyze(String collection,int skip, int limit) {

        List<Event> events;
        Query query = new Query();
        if (skip!=0) query.skip(skip);
        if (limit!=0) query.limit(limit);
        events = mongoTemplate.find(query,Event.class,collection);
        Collections.reverse(events);
        return events;
    }
}


