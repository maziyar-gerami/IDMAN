package parsso.idman.models.other;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.MongoTemplate;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.communicate.MagfaInstantMessage;
import parsso.idman.models.users.User;
import parsso.idman.utils.sms.kaveNegar.KavenegarApi;
import parsso.idman.utils.sms.kaveNegar.excepctions.ApiException;
import parsso.idman.utils.sms.kaveNegar.excepctions.HttpException;
import parsso.idman.utils.sms.magfa.Texts;

import java.util.Date;

@SuppressWarnings("rawtypes")
@Setter
@Getter
public class Notification implements Comparable {
  private String title;
  private String url;
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private long timestamp;
  private Time time;
  private MongoTemplate mongoTemplate;

  @JsonIgnore
  public Notification(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public int compareTo(Object o) {
    if (this.timestamp > ((Notification) o).getTimestamp())
      return 1;
    else
      return -1;
  }

  public boolean sendPasswordChangeNotify(User user, String baseUrl) {
    String SMS_sdk = new Settings(mongoTemplate).retrieve(Variables.SMS_SDK).getValue().toString();

    if (SMS_sdk.equalsIgnoreCase("KaveNegar")) {
      return new Notification(mongoTemplate).sendMessagePasswordNotifyKaveNegar(mongoTemplate, user, baseUrl) > 0;
    } else if (SMS_sdk.equalsIgnoreCase("Magfa")) {
      return sendMessagePasswordNotifyMagfa(mongoTemplate, user) > 0;
    }
    return false;
  }

  private int sendMessagePasswordNotifyMagfa(MongoTemplate mongoTemplate, User user) {
    try {
      Texts texts = new Texts();
      texts.passwordChangeNotification();
      new MagfaInstantMessage(mongoTemplate).SendMessage(user.getMobile(), 1L);
      return Integer
          .parseInt(new Settings(mongoTemplate).retrieve(Variables.TOKEN_VALID_SMS).getValue().toString());
    } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.

      System.out.print("HttpException  : " + ex.getMessage());
      return 0;

    } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.

      System.out.print("ApiException : " + ex.getMessage());
      return 0;

    }

  }

  private int sendMessagePasswordNotifyKaveNegar(MongoTemplate mongoTemplate, User user, String baseurl) {
    try {
      Texts texts = new Texts();
      texts.passwordChangeNotification();
      KavenegarApi api = new KavenegarApi(
          new Settings(mongoTemplate).retrieve(Variables.KAVENEGAR_API_KEY).getValue().toString());
      Time time = Time.longToPersianTime(new Date().getTime());
      String d = time.getYear() + "-" + time.getMonth() + "-" + time.getDay();
      String h = time.getHours() + ":" + time.getMinutes();
      String dh = d + " ساعت " + h;
      api.verifyLookup(user.getMobile(), user.getDisplayName().substring(0, user.getDisplayName().indexOf(' ')),
          dh, baseurl, "changePass");
      return Integer
          .parseInt(new Settings(mongoTemplate).retrieve(Variables.TOKEN_VALID_SMS).getValue().toString());
    } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.

      System.out.print("HttpException  : " + ex.getMessage());
      return 0;

    } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.

      System.out.print("ApiException : " + ex.getMessage());
      return 0;

    }
  }
}