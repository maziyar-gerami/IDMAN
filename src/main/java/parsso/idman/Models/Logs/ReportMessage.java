package parsso.idman.Models.Logs;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import parsso.idman.Helpers.TimeHelper;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Services.Service;
import parsso.idman.Models.Users.UsersGroups;
import parsso.idman.Models.other.Time;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
public class ReportMessage {
    char separator = ':';
    String doerID;
    String model;
    Object instance;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String instanceName;
    String attribute;
    String result;
    String action;
    String description;
    String type;
    String item;
    String accessChange;
    @JsonIgnore
    UsersGroups usersGroups;
    @JsonIgnore
    List<Change> difference;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Object from;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Object to;
    long millis;
    Time time;
    String level;

    public ReportMessage() {
    }


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

    public ReportMessage(String model, Object instance, String attribute, String action, String result, String type, String item, String description) {
        this.model = model;
        this.instance = instance;
        this.attribute = attribute;
        this.result = result;
        this.action = action;
        this.type = type;
        this.item = item;
        this.description = description;
        this.millis = new Date().getTime();
        time = TimeHelper.longToPersianTime(millis);
    }

    public ReportMessage(String model, long instance, String attribute, String action, String result,
                         Service from, Service to, UsersGroups usersGroups, String description) {
        this.model = model;
        this.instance = instance;
        this.attribute = attribute;
        this.action = action;
        this.result = result;
        this.from = from;
        this.to = to;
        this.millis = new Date().getTime();
        this.difference = difference(from, to);
        this.usersGroups = usersGroups;
        this.description = description;
    }

    public ReportMessage(String model, Object instance, String attribute, String action, String result, Object from, Object to, String description) {
        this.model = model;
        this.instance = instance;
        this.attribute = attribute;
        this.result = result;
        this.action = action;
        this.to = to;
        this.setDifference(difference(from, to));
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

    public ReportMessage(Change ch, ReportMessage reportMessage) {
        this.doerID = reportMessage.getDoerID();
        this.model = reportMessage.model;
        this.instance = reportMessage.getInstance();
        this.instanceName = reportMessage.getInstanceName();
        this.attribute = ch.getAttribute();
        this.difference = difference(reportMessage.getFrom(), reportMessage.getTo());
        this.millis = new Date().getTime();
        this.result = reportMessage.getResult();
        this.action = reportMessage.getAction();

        this.description = reportMessage.description;
        time = TimeHelper.longToPersianTime(millis);

        this.from = ch.getFrom();
        this.to = ch.getTo();
    }

    public ReportMessage(String type, String item, String accessChange, ReportMessage reportMessage) {
        this.doerID = reportMessage.getDoerID();
        this.model = reportMessage.model;
        this.instance = reportMessage.getInstance();
        this.instanceName = reportMessage.getInstanceName();
        this.attribute = Variables.ACCESS_STRATEGY;
        this.type = type;
        this.item = item;

        this.result = reportMessage.getResult();
        this.action = reportMessage.getAction();
        this.accessChange = accessChange;

        this.description = reportMessage.description;
        this.millis = reportMessage.getMillis();
        time = TimeHelper.longToPersianTime(millis);

    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public String toString() {
        String first = "[" + level + "]" + separator +
                model + separator;
        if (instance != null && !instance.equals("null"))
            first = first + instance + separator;
        if (attribute != null && !attribute.equals("null"))
            first = first + attribute + separator;
        if (action != null && !action.equals("null"))
            first = first + action + separator;
        if (type != null && !type.equals("null"))
            first = first + type + separator;
        if (item != null && !item.equals("null"))
            first = first + item + separator;
        if (accessChange != null && !accessChange.equals("null"))
            first = first + accessChange + separator;
        if (result != null && !result.equals("null"))
            first = first + result + separator;
        if (description != null && !description.equals("null"))

            first = first + description;

        first = first.replaceAll("null", "");
        first = first.replaceAll(separator + " {2}" + separator, ":");
        first = first.replaceAll(separator + " {2}" + separator, String.valueOf(separator));
        String last = first.replaceAll(String.valueOf(separator) + separator, String.valueOf(separator));
        last = last.replaceAll(String.valueOf(separator) + separator + separator + separator, String.valueOf(separator));
        last = last.replaceAll(String.valueOf(separator), " " + separator + " ");
        if ((last.charAt(last.length() - 2)) == separator)
            return last.substring(0, last.length() - 3);
        return last;
    }

    private List<Change> difference(Object o1, Object o2) {
        Javers javers = JaversBuilder.javers().build();

        List<Change> chs = new LinkedList<>();

        Diff diff = javers.compare(o1, o2);
        List<org.javers.core.diff.changetype.ValueChange> changes = diff.getChangesByType(org.javers.core.diff.changetype.ValueChange.class);

        for (org.javers.core.diff.changetype.ValueChange c : changes)
            if (c.getLeft() != null && c.getRight() != null)
                chs.add(new Change(c));

        return chs;
    }

    @Setter
    @Getter
    public static class ListReportMessage{

        long size;
        int pages;
        List<ReportMessage> reportMessageList;

        ListReportMessage(){

        }

        public ListReportMessage(long size, int pages, List<ReportMessage> reportMessageList) {
            this.size = size;
            this.pages = pages;
            this.reportMessageList = reportMessageList;
        }
    }

}


