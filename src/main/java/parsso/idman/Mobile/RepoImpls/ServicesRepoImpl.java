package parsso.idman.Mobile.RepoImpls;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.Communicate.Message;
import parsso.idman.Helpers.Communicate.Token;
import parsso.idman.Mobile.Repos.ServicesRepo;
import parsso.idman.Models.User;
import parsso.idman.Repos.UserRepo;
import parsso.idman.Utils.SMS.sdk.KavenegarApi;
import parsso.idman.Utils.SMS.sdk.excepctions.ApiException;
import parsso.idman.Utils.SMS.sdk.excepctions.HttpException;

import javax.naming.Name;
import javax.naming.directory.SearchControls;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Random;


@Service
public class ServicesRepoImpl implements ServicesRepo {

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    @Value("${token.valid.email}")
    private int EMAIL_VALID_TIME;
    @Value("${token.valid.SMS}")
    private int SMS_VALID_TIME;
    @Value("${sms.api.key}")
    private String SMS_API_KEY;
    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;
    @Autowired
    private LdapTemplate ldapTemplate;
    @Value("${sms.validation.digits}")
    private int SMS_VALIDATION_DIGITS;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private Message message;
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
            String message = user.getTokens().getMobileToken().substring(0, SMS_VALIDATION_DIGITS);
            KavenegarApi api = new KavenegarApi(SMS_API_KEY);
            api.verifyLookup(user.getMobile(), message, "", "", "mfa");
        } catch (HttpException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
            System.out.print("HttpException  : " + ex.getMessage());
        } catch (ApiException ex) { // در صورتی که خروجی وب سرویس 200 نباشد این خطارخ می دهد.
            System.out.print("ApiException : " + ex.getMessage());
        }
        return "SMS Sent!";
    }

    public Name buildDn(String userId) {
        return LdapNameBuilder.newInstance(BASE_DN).add("ou", "People").add("uid", userId).build();
    }

    public String insertMobileToken1(User user) {
        Random rnd = new Random();
        int token = (int) (Math.pow(10, (SMS_VALIDATION_DIGITS - 1)) + rnd.nextInt((int) (Math.pow(10, SMS_VALIDATION_DIGITS - 1) - 1)));
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        long cTimeStamp = currentTimestamp.getTime();
        user.getTokens().setMobileToken(String.valueOf(token) + cTimeStamp);

        Query query = new Query(Criteria.where("userId").is(user.getUserId()));
        mongoTemplate.remove(query, Token.collection);
        mongoTemplate.save(user.getTokens(), Token.collection);

        return "Mobile Token for " + user.getUserId() + " is created";

    }

    public HttpStatus verifySMS(String userId, String token) {
        // return OK or code 200: token is valid and time is ok
        // return requestTimeOut or error 408: token is valid but time is not ok
        // return forbidden or error code 403: token is not valid
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        User user = userRepo.retrieveUser(userId);

        String mainDbToken = user.getTokens().getMobileToken();
        String mainPartToken;

        if (token.length() > 30)
            mainPartToken = mainDbToken.substring(0, 36);
        else
            mainPartToken = mainDbToken.substring(0, SMS_VALIDATION_DIGITS);


        if (token.equals(mainPartToken)) {

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            long cTimeStamp = currentTimestamp.getTime();

            if (mainPartToken.length() > 30) {

                String timeStamp = mainDbToken.substring(mainDbToken.indexOf(user.getUserId()) + user.getUserId().length());

                if ((cTimeStamp - Long.parseLong(timeStamp)) < (60000 * EMAIL_VALID_TIME))
                    return HttpStatus.OK;

                else
                    return HttpStatus.REQUEST_TIMEOUT;
            } else {
                String timeStamp = mainDbToken.substring(SMS_VALIDATION_DIGITS);
                if ((cTimeStamp - Long.parseLong(timeStamp)) < (60000 * SMS_VALID_TIME)) {
                    return HttpStatus.OK;
                } else
                    return HttpStatus.REQUEST_TIMEOUT;

            }

        } else
            return HttpStatus.FORBIDDEN;
    }


    public DirContextOperations buildAttributes(String uid, User p, Name dn) {
        DirContextOperations context = ldapTemplate.lookupContext(dn);

        if (p.getFirstName() != null) context.setAttributeValue("givenName", p.getFirstName());
        if (p.getLastName() != null) context.setAttributeValue("sn", p.getLastName());
        if (p.getDisplayName() != null) context.setAttributeValue("displayName", p.getDisplayName());
        if (p.getUserPassword() != null) context.setAttributeValue("userPassword", p.getUserPassword());
        if (p.getMobile() != null) context.setAttributeValue("mobile", p.getMobile());
        if (p.getMail() != null) context.setAttributeValue("mail", p.getMail());
        if ((p.getFirstName()) != null || (p.getLastName() != null)) {
            if (p.getFirstName() == null)
                context.setAttributeValue("cn", userRepo.retrieveUser(uid).getFirstName() + ' ' + p.getLastName());

            else if (p.getLastName() == null)
                context.setAttributeValue("cn", p.getFirstName() + ' ' + userRepo.retrieveUser(uid).getLastName());

            else context.setAttributeValue("cn", p.getFirstName() + ' ' + p.getLastName());
        }
        if (p.getMail() != null) context.setAttributeValue("photoName", p.getPhoto());

        if (p.getTokens().getResetPassToken() != null)
            context.setAttributeValue("resetPassToken", p.getTokens().getResetPassToken());

        if (p.getMemberOf() != null) {

            for (int i = 0; i < p.getMemberOf().size(); i++) {
                if (i == 0) context.setAttributeValue("ou", p.getMemberOf().get(i));
                else context.addAttributeValue("ou", p.getMemberOf().get(i));
            }
        }

        if (p.getDescription() != null) context.setAttributeValue("description", p.getDescription());
        if (p.getPhoto() != null) context.setAttributeValue("photoName", p.getPhoto());
        if (p.getStatus() != null) context.setAttributeValue("userStatus", p.getStatus());
        if (p.getTokens().getMobileToken() != null)
            context.setAttributeValue("mobileToken", p.getTokens().getMobileToken());


        return context;
    }


    public String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}