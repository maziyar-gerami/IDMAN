package parsso.idman.RepoImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Event;
import parsso.idman.Models.ListEvents;
import parsso.idman.Repos.EventRepo;
import parsso.idman.utils.Convertor.DateConverter;
import java.util.*;

@Service
public class EventRepoImpl implements EventRepo {

    private static String mainCollection = "MongoDbCasEventRepository";

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ListEvents getListSizeEvents(int p, int n) {
        List<Event> allEvents = analyze(mainCollection,(p-1)*n,n);
        long size =  mongoTemplate.getCollection(mainCollection).countDocuments();
        return new ListEvents(size,(int) Math.ceil(size/n),allEvents);
    }

    @Override
    public ListEvents getListUserEvents(String userId, int p, int n)  {
        Query query = new Query(Criteria.where("principalId").is(userId))
                .with(Sort.by(Sort.Direction.DESC,"_id"));
        long size = mongoTemplate.count(query, Event.class,mainCollection);

        query.skip((p-1)*(n)).limit(n);
        List<Event> eventList =  mongoTemplate.find(query, Event.class,mainCollection);
        ListEvents listEvents = new ListEvents(size,(int) Math.ceil(size/n),eventList);
        return listEvents;
    }

    @Override
    public ListEvents getEventsByDate(String date, int p, int n)  {

        DateConverter dateConverter = new DateConverter();
        dateConverter.persianToGregorian(Integer.valueOf(date.substring(4)),Integer.valueOf(date.substring(2,4)),Integer.valueOf(date.substring(0,2)));
        String time = dateConverter.getYear()+"-"+String.format("%02d", dateConverter.getMonth())+"-"+String.format("%02d", dateConverter.getDay());
        Query query = new Query(Criteria.where("creationTime").regex(time));
        List<Event> allEvents = mongoTemplate.find(query, Event.class, mainCollection);
        long size =  mongoTemplate.count(query,Event.class,mainCollection);

        return new ListEvents(size,(int) Math.ceil(size/n),allEvents);
    }

    @Override
    public ListEvents getListUserEventByDate(String date, String userId, int skip, int limit)  {

        DateConverter dateConverter = new DateConverter();
        dateConverter.persianToGregorian(Integer.valueOf(date.substring(4)),Integer.valueOf(date.substring(2,4)),Integer.valueOf(date.substring(0,2)));
        String time = dateConverter.getYear()+"-"+String.format("%02d", dateConverter.getMonth())+"-"+String.format("%02d", dateConverter.getDay());
        Query query = new Query(Criteria.where("principalId").is(userId).and("creationTime").regex(time));
        long size =  mongoTemplate.count(query, ListEvents.class,mainCollection);
        query.skip((skip-1)*(limit)).limit(limit).with(Sort.by(Sort.Direction.DESC,"_id"));
        List<Event> eventList =  mongoTemplate.find(query, Event.class,mainCollection);
        int pages = (int) Math.ceil(size/limit);
        ListEvents listEvents = new ListEvents(size,pages,eventList);
        return listEvents;
    }

    @Override
    public List<Event> analyze(String collection,int skip, int limit) {
        Query query = new Query().skip(skip).limit(limit).with(Sort.by(Sort.Direction.DESC,"_id"));
        return mongoTemplate.find(query,Event.class,collection);
    }
}


