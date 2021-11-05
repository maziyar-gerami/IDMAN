package parsso.idman.utils.sms.kaveNegar.models;


import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CountInboxResult {
    private final Long startDate;
    private final Long endDate;
    private final Long sumCount;

    public CountInboxResult(JsonObject json) {
        this.startDate = json.get("startdate").getAsLong();
        this.endDate = json.get("enddate").getAsLong();
        this.sumCount = json.get("sumcount").getAsLong();
    }

}