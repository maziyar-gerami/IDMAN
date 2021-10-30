package parsso.idman.Utils.SMS.KaveNegar.models;


import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountPostalCodeResult {
    private final String section;
    private final Long value;

    public CountPostalCodeResult(JsonObject json) {
        this.section = json.get("section").getAsString();
        this.value = json.get("value").getAsLong();
    }


}