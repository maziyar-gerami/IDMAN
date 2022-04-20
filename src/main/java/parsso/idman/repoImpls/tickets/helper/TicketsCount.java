package parsso.idman.repoImpls.tickets.helper;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Time;

public class TicketsCount {

    MongoTemplate mongoTemplate;
    public TicketsCount(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public int count(String cat, String subcat, int status, String id, String date, boolean archive) {
        Query query = new Query();
        if (status != -1)
            query.addCriteria(Criteria.where("status").is(status));

        if (!cat.equals("")) {
            query.addCriteria(Criteria.where("category").regex(cat));
            if (!subcat.equals(""))
                query.addCriteria(Criteria.where("subCategory").regex(subcat));
        }
        if (!id.equals(""))
            query.addCriteria(Criteria.where("_id").regex(id));

        if (archive)
            query.addCriteria(Criteria.where("deleteFor").exists(true));

        if (!date.equals("")) {
            String time = new Time(Integer.parseInt(date.substring(4)), Integer.parseInt(date.substring(2, 4)), Integer.parseInt(date.substring(0, 2))).toStringDate();
            String timeStart = time + "T00:00:00.000Z";
            String timeEnd = time + "T23:59:59.000Z";

            long eventStartDate = OffsetDateTime.parse(timeStart).atZoneSameInstant(ZoneId.of(Variables.ZONE)).toEpochSecond() * 1000;
            long eventEndDate = OffsetDateTime.parse(timeEnd).atZoneSameInstant(ZoneId.of(Variables.ZONE)).toEpochSecond() * 1000;

            query.addCriteria(Criteria.where("creationTime").gte(eventStartDate).lte(eventEndDate));

        }
        return (int) mongoTemplate.count(query, Variables.col_tickets);
    }
    
}
