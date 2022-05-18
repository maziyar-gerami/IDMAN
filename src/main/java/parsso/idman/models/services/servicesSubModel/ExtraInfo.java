package parsso.idman.models.services.servicesSubModel;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.models.services.Schedule;

import java.util.List;

@Setter
@Getter
public class ExtraInfo {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    long id;
    String url;
    int position;
    String UUID;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String notificationApiURL;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String notificationApiKey;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Schedule> dailyAccess;

    public ExtraInfo() {
        position = 0;
    }

}
