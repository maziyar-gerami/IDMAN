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
    String level;
    Time dateTime;
    String loggerName;
    String message;
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

    public String getDetails(){
        return source.toString();
    }

    @Setter
    @Getter
    private class Source{
        String className;
        String methodName;
        String fileName;
        String lineNumber;

        @Override
        public String toString() {
            return
                    "className='" + className + '\'' +
                    ", methodName='" + methodName + '\'' +
                    ", fileName='" + fileName + '\'' +
                    ", lineNumber='" + lineNumber + '\'';
        }
    }
}
