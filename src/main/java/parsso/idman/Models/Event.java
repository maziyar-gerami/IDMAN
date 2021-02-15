package parsso.idman.Models;


import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import parsso.idman.Helpers.Events.ActionInfo;
import parsso.idman.Helpers.Events.AgentInfo;
import parsso.idman.RepoImpls.ServiceRepoImpl;
import ua_parser.Client;
import ua_parser.Parser;

import java.io.IOException;
import java.io.Serializable;


@Setter
@Getter
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Event implements Serializable {
    @JsonIgnore
    public Properties properties = new Properties();
    @JsonProperty("clientIP")
    public String clientip;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("serverIP")
    public String serverip;
    public String eventId;
    @JsonIgnoreProperties
    @JsonIgnore
    public Client agent;
    public String service;
    public AgentInfo agentInfo;
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
    @JsonProperty("userId")
    String principalId;
    @JsonIgnore
    String creationTime;
    String application;
    @JsonIgnore
    String _class;
    Time time;



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

    public String getClientip() {
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

    public String getServerip() {
        return this.properties.serverip;
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

        private String serverip;
        @JsonIgnore
        private String timestamp;
    }

}



