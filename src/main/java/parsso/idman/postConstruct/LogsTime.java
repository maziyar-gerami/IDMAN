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
        //events.start();

        Thread audits = new Thread(()->{
            try {
                audits();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        //audits.start();



    }

    public void events(){
        Query query = new Query(Criteria.where("stringDate").exists(false));
        List<Event> events = mongoTemplate.find(query,Event.class, Variables.col_casEvent);

        int c=0;
        char[] animationChars = new char[]{'|', '/', '-', '\\'};

        for (Event event:events) {
            long _id = Long.parseLong(event.get_id());
            LogTime logTime = new LogTime(_id);
            Event event1 = Event.setStringDateAndTime(event,logTime.getDate(),logTime.getTime());
            event1.setTimeString(event1.getTimeString());
            event1.setDateString(event.getDateString());
            Update update =createUpdate(Long.parseLong(event.get_id()));
            mongoTemplate.upsert(new Query(Criteria.where("_id").is(_id)),update, Variables.col_casEvent);

            int i =(++c*100/events.size());

            System.out.print("Processing events: " + i + "% " + animationChars[i % 4] + "\r");
        }

    }

    public void audits(){
        Query query = new Query(Criteria.where("stringDate").exists(false));
        List<Audit> audits = mongoTemplate.find(query,Audit.class, Variables.col_audit);

        int c=0;
        char[] animationChars = new char[]{'|', '/', '-', '\\'};

        for (Audit audit:audits) {
            long t = Long.parseLong(audit.get_id().toString().substring(0, 8), 16) * 1000;
            LogTime logTime = new LogTime(t);
            Audit audit1 = Audit.setStringDateAndTime(audit,logTime.getDate(),logTime.getTime());
            audit1.setTimeString(audit1.getTimeString());
            audit1.setDateString(audit1.getDateString());
            Update update =createUpdate(t);
            mongoTemplate.upsert(new Query(Criteria.where("_id").is(audit.get_id())), update, Variables.col_audit);

            int i =(++c*100/audits.size());

            System.out.print("\nProcessing audits: " + i + "% " + animationChars[i % 4] + "\r");
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
