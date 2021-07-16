package parsso.idman.Models.Logs;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.Models.Time;

import java.util.Date;

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

    public Time getDateTime() {
        return Time.longToPersianTime(date.getTime());
    }

    public String getDetails() {
        return source.toString();
    }

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
