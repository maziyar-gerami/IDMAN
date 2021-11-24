package parsso.idman.postConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
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
        Query query = new Query(Criteria.where("date").exists(false));
        List<Event> events = mongoTemplate.find(query,Event.class, Variables.col_casEvent);

        for (Event event:events)
            mongoTemplate.upsert(new Query(Criteria.where("_id").is(Long.parseLong(event.get_id()))),
                    createUpdate((Long.parseLong(event.get_id()))), Variables.col_casEvent);

    }



    public void audits(){
        Query query = new Query(Criteria.where("date").exists(false));
        List<Audit> audits = mongoTemplate.find(query,Audit.class, Variables.col_audit);

        for (Audit audit:audits) {
            long t = Long.parseLong(audit.get_id().toString().substring(0, 8), 16) * 1000;
            mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(audit.get_id())),createUpdate(t),Variables.col_audit);
        }

    }

    private Update createUpdate(Long millis){
        LogTime logTime = new LogTime(millis);
        Update update = new Update();
        update.set("date", logTime.getDate());
        update.set("time", logTime.getTime());
        return update;
    }
}
