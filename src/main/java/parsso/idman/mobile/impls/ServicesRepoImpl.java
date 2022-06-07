package parsso.idman.mobile.impls;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Random;
import javax.naming.Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import parsso.idman.configs.Prefs;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.communicate.Token;
import parsso.idman.mobile.repos.ServicesRepo;
import parsso.idman.models.users.User;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;
import parsso.idman.utils.sms.kaveNegar.KavenegarApi;
import parsso.idman.utils.sms.kaveNegar.excepctions.ApiException;
import parsso.idman.utils.sms.kaveNegar.excepctions.HttpException;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class ServicesRepoImpl implements ServicesRepo {
  static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  static final SecureRandom rnd = new SecureRandom();
  @Autowired
  private UsersRetrieveRepo usersOpRetrieve;
  @Autowired
  private MongoTemplate mongoTemplate;

  public byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

    ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
    return pngOutputStream.toByteArray();
  }

  public String ActivationSendMessage(User user) {
    insertMobileToken1(user);
    try {
      String message = user.getUsersExtraInfo().getMobileToken().substring(0, Integer.parseInt(
          new Settings(mongoTemplate).retrieve(Variables.SMS_VALIDATION_DIGITS).getValue().toString()));
      KavenegarApi api = new KavenegarApi(
          new Settings(mongoTemplate).retrieve(Variables.KAVENEGAR_API_KEY).getValue().toString());
      api.verifyLookup(user.getMobile(), message, "", "", "mfa");
    } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
      System.out.print("HttpException  : " + ex.getMessage());
    } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
      System.out.print("ApiException : " + ex.getMessage());
    }
    return "SMS Sent!";
  }

  public Name buildDn(String userId) {
    return LdapNameBuilder.newInstance(Prefs.get(Variables.PREFS_BASE_DN)).add("ou", "People").add("uid", userId)
        .build();
  }

  public String insertMobileToken1(User user) {
    int SMS_VALIDATION_DIGITS = Integer
        .parseInt(new Settings(mongoTemplate).retrieve(Variables.SMS_VALIDATION_DIGITS).getValue().toString());
    Random rnd = new Random();
    int token = (int) (Math.pow(10, (SMS_VALIDATION_DIGITS - 1))
        + rnd.nextInt((int) (Math.pow(10, SMS_VALIDATION_DIGITS - 1) - 1)));
    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
    long cTimeStamp = currentTimestamp.getTime();
    user.getUsersExtraInfo().setMobileToken(String.valueOf(token) + cTimeStamp);

    mongoTemplate.save(user.getUsersExtraInfo(), Token.collection);

    return "mobile Token for " + user.get_id() + " is created";

  }

  public HttpStatus verifySMS(String userId, String token) {
    int SMS_VALIDATION_DIGITS = Integer
        .parseInt(new Settings(mongoTemplate).retrieve(Variables.SMS_VALIDATION_DIGITS).getValue().toString());

    User user = usersOpRetrieve.retrieveUsers(userId);

    String mainDbToken = user.getUsersExtraInfo().getMobileToken();
    String mainPartToken;

    if (token.length() > 30)
      mainPartToken = mainDbToken.substring(0, 36);
    else
      mainPartToken = mainDbToken.substring(0, SMS_VALIDATION_DIGITS);

    if (token.equals(mainPartToken)) {

      Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
      long cTimeStamp = currentTimestamp.getTime();

      if (mainPartToken.length() > 30) {

        String timeStamp = mainDbToken
            .substring(mainDbToken.indexOf(user.get_id().toString()) + user.get_id().toString().length());

        if ((cTimeStamp - Long.parseLong(timeStamp)) < (60000L * Integer.parseInt(new Settings(mongoTemplate)
            .retrieve("token.valid.email").getValue().toString())))
          return HttpStatus.OK;

        else
          return HttpStatus.REQUEST_TIMEOUT;
      } else {
        String timeStamp = mainDbToken.substring(SMS_VALIDATION_DIGITS);
        if ((cTimeStamp - Long.parseLong(timeStamp)) < (60000L * Integer.parseInt(new Settings(mongoTemplate)
            .retrieve("token.valid.SMS").getValue().toString()))) {
          return HttpStatus.OK;
        } else
          return HttpStatus.REQUEST_TIMEOUT;

      }

    } else
      return HttpStatus.FORBIDDEN;
  }

  public String randomString(int len) {
    StringBuilder sb = new StringBuilder(len);
    for (int i = 0; i < len; i++)
      sb.append(AB.charAt(rnd.nextInt(AB.length())));
    return sb.toString();
  }
}