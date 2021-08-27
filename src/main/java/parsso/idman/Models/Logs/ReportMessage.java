package parsso.idman.Models.Logs;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import parsso.idman.Helpers.TimeHelper;
import parsso.idman.Models.Time;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
public class ReportMessage {
    final char separator = ':';
    String doerID;
    String model;
    Object instance;
    String attribute;
    String result;
    String action;
    String description;
    @JsonIgnore
    List<Changes> difference;
    Object from;
    Object to;
    long millis;
    Time time;
    String level;

    public ReportMessage(String model, Object instance, String attribute, String action, String result, String description) {
        this.model = model;
        this.instance = instance;
        this.attribute = attribute;
        this.result = result;
        this.action = action;
        this.description = description;
        this.millis = new Date().getTime();
        time = TimeHelper.longToPersianTime(millis);
    }

    public ReportMessage(String model, Object instance, String attribute, String action, String result, Object to, String description) {
        this.model = model;
        this.instance = instance;
        this.attribute = attribute;
        this.result = result;
        this.action = action;
        this.to = to;
        this.description = description;
        this.millis = new Date().getTime();
        time = TimeHelper.longToPersianTime(millis);
    }

    public ReportMessage(String model, Object instance, String attribute, String action, String result, Object from, Object to, String description) {
        this.model = model;
        this.instance = instance;
        this.attribute = attribute;
        this.result = result;
        this.action = action;
        this.setDifference(difference(from,to));
        this.description = description;
        this.millis = new Date().getTime();
        time = TimeHelper.longToPersianTime(millis);
    }

    public ReportMessage(String action, String result, String description) {

        this.action = action;
        this.result = result;
        this.description = description;
        this.millis = new Date().getTime();
        time = TimeHelper.longToPersianTime(millis);
    }

    public ReportMessage() {

    }

    public ReportMessage(Changes ch, ReportMessage reportMessage) {
        this.doerID = reportMessage.getDoerID();
        this.model = reportMessage.model;
        this.instance = reportMessage.getInstance();
        this.attribute = reportMessage.getAttribute();

        this.result = reportMessage.getResult();
        this.action = reportMessage.getAction();

        this.description = reportMessage.description;
        this.millis = reportMessage.getMillis();
        time = reportMessage.getTime();

        this.attribute=ch.getAttribute();
        this.from = ch.getFrom();
        this.to = ch.getTo();
    }

    @Override
    public String toString() {
        String first = "[" + level + "]" + separator +
                model + separator +
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

    private List<Changes> difference(Object o1 , Object o2) {
        Javers javers = JaversBuilder.javers().build();

        List<Changes> chs = new LinkedList<>();

        Diff diff = javers.compare(o1, o2);
        List<org.javers.core.diff.changetype.ValueChange> changes = diff.getChangesByType(org.javers.core.diff.changetype.ValueChange.class);

        for (org.javers.core.diff.changetype.ValueChange c: changes )
            if (c.getLeft()!=null &&  c.getRight()!=null)
                chs.add(new Changes(c));


        return chs;
    }

    @Getter
    @Setter
    public class Changes{
        private String attribute;
        private Object from;
        private Object to;

        public Changes(Object from, Object to) {
            this.from = from;
            this.to = to;
        }

        public Changes(ValueChange c) {
            this.attribute = c.getPropertyName();
            this.from = c.getLeft();
            this.to = c.getRight();

        }
    }
}
