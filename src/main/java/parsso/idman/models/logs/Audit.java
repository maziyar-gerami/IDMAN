package parsso.idman.models.logs;


import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.TimeHelper;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Time;

import java.util.Date;
import java.util.List;

@SuppressWarnings("unused")
@Setter
@Getter
public class Audit {
    ObjectId _id;
    private String principal;
    private String resourceOperatedUpon;
    private String actionPerformed;
    private String applicationCode;
    private String dateString;
    private String timeString;
    private Date whenActionWasPerformed;
    private String clientIpAddress;
    private String serverIpAddress;
    private Time time;

    public static List<Audit> analyze(MongoTemplate mongoTemplate, int skip, int limit) {
        Query query = new Query().skip(skip).limit(limit).with(Sort.by(Sort.Direction.DESC, "_id"));
        return mongoTemplate.find(query, Audit.class, Variables.col_audit);
    }

    public static Audit setStringDateAndTime(Audit audit, String date, String time) {
        audit.setDateString(date);
        audit.setTimeString(time);
        return audit;
    }

    public Time getTime() {
        return TimeHelper.longToPersianTime(whenActionWasPerformed.getTime());
    }

    @SuppressWarnings("unused")
    @Setter
    @Getter
    public static class ListAudits {
        long size;
        int pages;
        List<Audit> auditList;

        public ListAudits(List<Audit> relativeAudits, long size, int pages) {
            this.size = size;
            this.pages = pages;
            this.auditList = relativeAudits;
        }

        public ListAudits() {

        }
    }

}
