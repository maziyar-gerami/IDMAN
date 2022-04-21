package parsso.idman.utils.sms.kaveNegar.models;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountOutboxResult extends CountInboxResult {
  private final Long sumPart;
  private final Long cost;

  public CountOutboxResult(JsonObject json) {
    super(json);
    this.sumPart = json.get("sumpart").getAsLong();
    this.cost = json.get("cost").getAsLong();
  }

}