package parsso.idman.utils.sms.kaveNegar.models;

import com.google.gson.JsonObject;

@SuppressWarnings("ALL")
public class AccountInfoResult {
  private final Long remainCredit;
  private final Long expireDate;
  private final String type;

  public AccountInfoResult(JsonObject json) {
    this.remainCredit = json.get("remaincredit").getAsLong();
    this.expireDate = json.get("expiredate").getAsLong();
    this.type = json.get("type").getAsString();
  }

  public Long getRemainCredit() {
    return remainCredit;
  }

  public Long getExpireDate() {
    return expireDate;
  }

  public String getType() {
    return type;
  }
}
