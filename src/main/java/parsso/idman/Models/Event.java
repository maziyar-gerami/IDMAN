package parsso.idman.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mongodb.client.MongoClients;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;


import parsso.idman.Helpers.Events.ActionInfo;
import parsso.idman.Helpers.Events.AgentInfo;
import parsso.idman.RepoImpls.ServiceRepoImpl;
import ua_parser.Client;
import ua_parser.OS;
import ua_parser.Parser;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;


@Setter
@Getter
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Event implements Serializable {
    @Autowired
    ServiceRepoImpl serviceRepo;

    @JsonIgnore
    @Autowired
    MongoTemplate mongoTemplate;
    @JsonIgnore
    String collection = "Agents";

    String _id;
    @JsonProperty("action")
    String type;
    public String getType(){
        ActionInfo actionHelper = new ActionInfo(type);
        return actionHelper.getAction();
    }

    @JsonProperty("userId")
    String principalId;
    @JsonIgnore
    String creationTime;
    String application;
    public String getApplication(){
        if (_class.contains("org.apereo.cas"))
            return  "CAS";
        else
            return null;
    }
    @JsonIgnore
    String _class;
    @JsonIgnore
    Properties properties = new Properties();
    Time time;
    @JsonProperty("clientIP")
    public String clientip;
    public String getClientip() {
        return properties.clientip;
    }
    @JsonProperty("serverIP")
    public String serverip;
    public String getServerip() {
        return properties.serverip;
    }
    public String eventId;
    public String getEventId() {
        return properties.eventId;
    }
    @JsonIgnoreProperties
    @JsonIgnore
    public Client agent;


    public Time getTime() {
        time = new Time(Integer.valueOf(creationTime.substring(0, 4))
        , Integer.valueOf(creationTime.substring(5, 7))
                , Integer.valueOf(creationTime.substring(8, 10))
        ,Integer.valueOf(creationTime.substring(11,13))
        ,Integer.valueOf(creationTime.substring(14,16))
        , Integer.valueOf(creationTime.substring(17,19)));


        return time;
    }

    public String service;

    @Setter
    @Getter
    @JsonDeserialize
    public static class Properties {
        private String eventId;
        private String agent;
        @JsonProperty("clientIP")
        private String clientip;
        private String serverip;
        @JsonIgnore
        private String timestamp;
    }

    public Client getAgent() {

        Parser uaParser;
        Client c=null;
        try {
            uaParser = new Parser();
            c = uaParser.parse(properties.agent);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return  c;

    }
    public AgentInfo agentInfo;
    public AgentInfo getAgentInfo(){
        AgentInfo agentInfo = null;
        try {
            agentInfo = new AgentInfo(getAgent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  agentInfo;
    }

    }



