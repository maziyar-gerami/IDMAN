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
        EventThread eventThread = new EventThread();
        eventThread.run();

        AuditThread auditThread = new AuditThread();
        auditThread.run();

    }

    public class EventThread implements Runnable{

        @Override
        public void run() {
            try {
                events();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public class AuditThread implements Runnable{

        @Override
        public void run() {
            try {
                audits();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    public void events(){
        Query query = new Query(Criteria.where("dateString").exists(false));

        char[] animationChars = new char[]{'|', '/', '-', '\\'};

        long count = mongoTemplate.count(query, Variables.col_casEvent);

        for (int page = 0; page <= Math.ceil( (float)count/Variables.PER_BATCH_COUNT); page++) {


            int skip = (page == 0) ? 0 : ((page - 1) * Variables.PER_BATCH_COUNT);

            List<Event> events = mongoTemplate.find(query.skip(skip).limit(Variables.PER_BATCH_COUNT), Event.class, Variables.col_casEvent);

            for (Event event : events) {
                long _id = Long.parseLong(event.get_id());
                LogTime logTime = new LogTime(_id);
                Event event1 = Event.setStringDateAndTime(event, logTime.getDate(), logTime.getTime());
                event1.setTimeString(event1.getTimeString());
                event1.setDateString(event.getDateString());
                Update update = createUpdate(Long.parseLong(event.get_id()));
                mongoTemplate.upsert(new Query(Criteria.where("_id").is(_id)), update, Variables.col_casEvent);

            }
            double i =  (page * 100 / Math.ceil( (float)count/Variables.PER_BATCH_COUNT));
            i = Math.round(i*Math.pow(10,1))/Math.pow(10,1);
            System.out.print("Processing events: " + i + "% " + animationChars[(int)i % 4] + "\r");
            Runtime.getRuntime().freeMemory();
        }

    }

    public void audits(){
        Query query = new Query(Criteria.where("dateString").exists(false));

        long count = mongoTemplate.count(query, Variables.col_audit);

        for (int page = 0; page <= Math.ceil( (float)count/Variables.PER_BATCH_COUNT); page++) {

            int skip = (page == 0) ? 0 : ((page - 1) * Variables.PER_BATCH_COUNT);

            List<Audit> audits = mongoTemplate.find(query.skip(skip).limit(Variables.PER_BATCH_COUNT), Audit.class, Variables.col_audit);


            char[] animationChars = new char[]{'|', '/', '-', '\\'};

            for (Audit audit : audits) {
                long t = Long.parseLong(audit.get_id().toString().substring(0, 8), 16) * 1000;
                LogTime logTime = new LogTime(t);
                Audit audit1 = Audit.setStringDateAndTime(audit, logTime.getDate(), logTime.getTime());
                audit1.setTimeString(audit1.getTimeString());
                audit1.setDateString(audit1.getDateString());
                Update update = createUpdate(t);
                mongoTemplate.upsert(new Query(Criteria.where("_id").is(audit.get_id())), update, Variables.col_audit);
            }
            double i =  page * 100 / (Math.ceil( (float)count/Variables.PER_BATCH_COUNT));
            i = Math.round(i*Math.pow(10,1))/Math.pow(10,1);
            System.out.print("Processing audits: " + i + "% " + animationChars[(int)i % 4] + "\r");
            Runtime.getRuntime().freeMemory();
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
