package parsso.idman.Models.Services;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.Models.other.Notification;
import parsso.idman.Models.other.Return;

import java.util.List;

@Setter
@Getter
public class ServiceGist {
    @JsonIgnore
    @JsonProperty("return")
    private Return aReturn;
    private int count;
    private List<Notification> notifications;


}
