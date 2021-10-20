package parsso.idman.Models.Services;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
import parsso.idman.Models.other.Notification;
import parsso.idman.Models.other.Return;

import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
public class ServiceGist {
    @JsonIgnore
    @JsonProperty("return")
    private Return aReturn;
    private int count;
    private List<Notification> notifications;
    private Inconsistency inconsistency;

    public ServiceGist(Return aReturn) {
        this.count = 0;
        this.aReturn = aReturn;
        this.notifications = new LinkedList<>();
    }

    public ServiceGist(Return aReturn, Inconsistency inconsistency) {
        this.count = 0;
        this.aReturn = aReturn;
        this.inconsistency = inconsistency;
        this.notifications = new LinkedList<>();
    }

    public ServiceGist(int count, List<Notification> notifications, Return aReturn) {
        this.count = count;
        this.notifications = notifications;
        this.aReturn = aReturn;
    }


    public static Inconsistency parseServiceGist(JSONObject json) {
        List<String> omitted = new LinkedList();

        List<String> malformed = new LinkedList();

        if (!json.containsKey("count"))
            omitted.add("count");
        else if (!(json.get("count") instanceof Integer))
            malformed.add("count");

        if (!json.containsKey("notifications"))
            omitted.add("notifications");
        else if (!(json.get("notifications") instanceof List))
            malformed.add("notifications");

        if (!json.containsKey("return"))
            omitted.add("return");
        else if (!(json.get("return") instanceof Return))
            malformed.add("return");

        if (malformed.isEmpty() && omitted.isEmpty())
            return new Inconsistency(true);


        return new Inconsistency(omitted, malformed, true);
    }

    @Getter
    @Setter
    public static class Inconsistency {
        private boolean notExist;
        private List<String> omitted;
        private List<String> malformed;


        public Inconsistency(List<String> omitted, List<String> malformed, boolean notExist) {
            this.notExist = notExist;
            this.omitted = omitted;
            this.malformed = malformed;
        }

        public Inconsistency(boolean notExist) {
            this.notExist = notExist;
        }
    }
}
