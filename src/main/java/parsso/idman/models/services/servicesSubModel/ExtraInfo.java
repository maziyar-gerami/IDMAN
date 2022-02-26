package parsso.idman.models.services.servicesSubModel;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
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

    public parsso.idman.models.services.servicesSubModel.ExtraInfo setExtraInfo(long id, ExtraInfo extraInfo, JSONObject jsonObject,  int i) {
        extraInfo.setId(id);
        extraInfo.setPosition(i);
        try {
            extraInfo.setNotificationApiURL(jsonObject.get("notificationApiURL").toString());
        } catch (Exception ignored) {
        }

        try {
            extraInfo.setNotificationApiKey(jsonObject.get("notificationApiKey").toString());
        } catch (Exception ignored) {
        }

        try {
            extraInfo.setDailyAccess((List<Schedule>) jsonObject.get("dailyAccess"));
        } catch (Exception ignored) {
        }
        return extraInfo;
    }
}
