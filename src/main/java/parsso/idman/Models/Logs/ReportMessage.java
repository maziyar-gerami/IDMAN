package parsso.idman.Models.Logs;


import lombok.Getter;
import lombok.Setter;
import parsso.idman.Helpers.TimeHelper;
import parsso.idman.Models.Time;

import java.util.Date;

@Setter
@Getter
public class ReportMessage {
    final char separator = ':';
    String model;
    String instance;
    String attribute;
    String result;
    String action;
    String description;
    String from;
    String to;
    long millis;
    Time time;

    public ReportMessage(String model, String instance, String attribute, String action, String result, String description) {
        this.model = model;
        this.instance = instance;
        this.attribute = attribute;
        this.result = result;
        this.action = action;
        this.description = description;
        this.millis = new Date().getTime();
        time = TimeHelper.longToPersianTime(millis);
    }


    @Override
    public String toString() {
        String first = model + separator +
                instance + separator +
                attribute + separator +
                action + separator +
                result + separator +
                description;
        String last = first.replaceAll(String.valueOf(separator) + separator, String.valueOf(separator));
        last = last.replaceAll(String.valueOf(separator), " " + separator + " ");
        if ((last.charAt(last.length() - 2)) == separator)
            return last.substring(0, last.length() - 3);
        return last;
    }
}
