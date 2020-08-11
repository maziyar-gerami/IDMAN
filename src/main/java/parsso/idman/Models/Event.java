package parsso.idman.Models;

import lombok.Getter;
import lombok.Setter;

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
    Date timeStamp;
}
