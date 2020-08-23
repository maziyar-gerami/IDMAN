package parsso.idman.Models;

import lombok.Getter;
import lombok.Setter;
import parsso.idman.Models.EventsSubModel.Time;

import java.util.Date;


@Setter
@Getter

public class Event {
    String action;
    String userId;
    String application;
    String details;
    String clientIP;
    String serverIP;
    Time time;
}

