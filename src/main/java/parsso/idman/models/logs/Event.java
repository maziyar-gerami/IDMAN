package parsso.idman.models.logs;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.helpers.TimeHelper;
import parsso.idman.helpers.events.ActionInfo;
import parsso.idman.helpers.events.AgentInfo;
import parsso.idman.models.other.Time;
import ua_parser.Client;
import ua_parser.Parser;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Event implements Serializable {
    @JsonIgnore
    public Properties properties = new Properties();
    @JsonProperty("clientIP")
    public String clientIP;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("serverIP")
    public String serverIP;
    public String eventId;
    @JsonIgnoreProperties
    @JsonIgnore
    public Client agent;
    public String service;
    public AgentInfo agentInfo;
    @JsonIgnore
    String collection = "Agents";
    String _id;
    @JsonProperty("action")
    String type;
    @JsonProperty("userId")
    String principalId;
    @JsonIgnore
    String creationTime;
    String application;
    @JsonIgnore
    String _class;
    @JsonIgnore
    Time time;
    String timeString;
    String dateString;



    public String getType() {
        ActionInfo actionHelper = new ActionInfo(type);
        return actionHelper.getAction();
    }

    public String getApplication() {
        if (_class.contains("org.apereo.cas"))
            return "CAS";
        else
            return null;
    }

    public String getClientIP() {
        return this.properties.getClientip();
    }

    public Client getAgent() {

        Parser uaParser;
        Client c = null;
        try {
            uaParser = new Parser();
            c = uaParser.parse(properties.agent);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return c;

    }

    public Time getTime() {
        return TimeHelper.longToPersianTime(Long.valueOf(_id));
    }

    public static Event setStringDateAndTime(Event event, String date, String time){
        event.setDateString(date);
        event.setTimeString(time);
        return event;
    }

    public AgentInfo getAgentInfo() {
        AgentInfo agentInfo = null;
        try {
            agentInfo = new AgentInfo(getAgent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return agentInfo;
    }

    @Setter
    @Getter
    @JsonDeserialize
    public static class Properties {
        private String eventId;
        private String agent;
        @JsonProperty("clientIP")
        private String clientip;
        @JsonIgnore
        private String timestamp;
    }

    @Setter
    @Getter

    public static class ListEvents {
        long size;
        int pages;
        List<Event> eventList;

        public ListEvents(long size, int pages, List<Event> relativeEvents) {
            this.size = size;
            this.pages = pages;
            this.eventList = relativeEvents;
        }

        @SuppressWarnings("unused")
        public ListEvents() {

        }

        public ListEvents(List<Event> events, long size, int pages) {
            this.size = size;
            this.pages = pages;
            this.eventList = events;

        }


    }

}



