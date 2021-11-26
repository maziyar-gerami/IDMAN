package parsso.idman.postConstruct;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import parsso.idman.helpers.LogTime;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.Audit;
import parsso.idman.models.logs.Event;

import java.util.List;

public class LogsTime {
    MongoTemplate mongoTemplate;

    public LogsTime(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public void run(){

        Thread events = new Thread(()->{
            try {
                events();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        events.start();

        Thread audits = new Thread(()->{
            try {
                audits();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        audits.start();



    }

    public void events(){
        Query query = new Query(Criteria.where("stringDate").exists(false));
        List<Event> events = mongoTemplate.find(query,Event.class, Variables.col_casEvent);

        for (Event event:events) {
            long _id = Long.parseLong(event.get_id());
            LogTime logTime = new LogTime(_id);
            Event event1 = Event.setStringDateAndTime(event,logTime.getDate(),logTime.getTime());
            event1.setStringTime(event1.getStringTime());
            event1.setStringDate(event.getStringDate());
            Update update =createUpdate(Long.parseLong(event.get_id()));
            mongoTemplate.upsert(new Query(Criteria.where("_id").is(_id)),update, Variables.col_casEvent);
        }

    }

    public void audits(){
        Query query = new Query(Criteria.where("stringDate").exists(false));
        List<Audit> audits = mongoTemplate.find(query,Audit.class, Variables.col_audit);

        for (Audit audit:audits) {
            long t = Long.parseLong(audit.get_id().toString().substring(0, 8), 16) * 1000;
            LogTime logTime = new LogTime(t);
            Audit audit1 = Audit.setStringDateAndTime(audit,logTime.getDate(),logTime.getTime());
            audit1.setStringTime(audit1.getStringTime());
            audit1.setStringDate(audit1.getStringDate());
            Update update =createUpdate(t);
            mongoTemplate.upsert(new Query(Criteria.where("_id").is(audit.get_id())), update, Variables.col_audit);
        }

    }

    private Update createUpdate(Long millis){
        LogTime logTime = new LogTime(millis);
        Update update = new Update();
        update.set("dateString", logTime.getDate());
        update.set("timeString", logTime.getTime());
        return update;
    }
}
