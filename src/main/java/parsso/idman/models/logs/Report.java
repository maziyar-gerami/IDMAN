package parsso.idman.models.logs;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Time;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class Report {
    @JsonIgnore
    protected char separator = ':';
    @JsonIgnore
    String level;
    Time dateTime;
    String loggerName;
    String message;
    @JsonIgnore
    String details;
    @JsonIgnore
    Source source;
    @JsonIgnore
    String marker;
    @JsonIgnore
    String threadId;
    @JsonIgnore
    String threadName;
    @JsonIgnore
    String priority;
    @JsonIgnore
    long millis;
    @JsonIgnore
    Date date;
    @JsonIgnore
    String thrown;

    public static List<Report> analyze(MongoTemplate mongoTemplate, int skip, int limit) {
        Query query = new Query().skip(skip).limit(limit).with(Sort.by(Sort.Direction.DESC, "millis"));
        List<Report> le = mongoTemplate.find(query, Report.class, Variables.col_Log);
        return new Time().reportSetDate(le);
    }

    public Time getDateTime() {
        return Time.longToPersianTime(date.getTime());
    }

    public String getDetails() {
        return source.toString();
    }

    @Setter
    @Getter

    public static class ListReports {
        long size;
        int pages;
        List<Report> reportsList;

        public ListReports(long size, int pages, List<Report> relativeEvents) {
            this.size = size;
            this.pages = pages;
            this.reportsList = relativeEvents;
        }

        @SuppressWarnings("unused")
        public ListReports() {

        }

        public ListReports(List<Report> reports, long size, int pages) {
            this.size = size;
            this.pages = pages;
            this.reportsList = reports;
        }
    }

    @SuppressWarnings("unused")
    @Setter
    @Getter
    private class Source {
        String className;
        String methodName;
        String fileName;
        String lineNumber;

        @Override
        public String toString() {
            return
                    "className='" + className + separator +
                            ", methodName='" + methodName + separator +
                            ", fileName='" + fileName + separator +
                            ", lineNumber='" + lineNumber + separator;
        }
    }


}
