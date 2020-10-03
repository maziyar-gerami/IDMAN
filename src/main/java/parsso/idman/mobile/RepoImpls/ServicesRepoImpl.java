package parsso.idman.mobile.RepoImpls;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import parsso.idman.Models.User;
import parsso.idman.RepoImpls.UserRepoImpl;
import parsso.idman.Repos.UserRepo;
import parsso.idman.mobile.Repos.ServicesRepo;
import parsso.idman.utils.SMS.sdk.KavenegarApi;
import parsso.idman.utils.SMS.sdk.excepctions.ApiException;
import parsso.idman.utils.SMS.sdk.excepctions.HttpException;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.directory.SearchControls;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Random;


@Service
public class ServicesRepoImpl implements ServicesRepo {

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
    private UserRepoImpl userRepoImpl;


    public byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    public String ActivationSendMessage(String mobile) {
        String s = userRepo.checkMobile(mobile).get(0).getAsString("userId");
        User user = userRepo.retrieveUser(userRepo.checkMobile(mobile).get(0).getAsString("userId"));
        insertMobileToken1(user);
        try {
            String receptor = mobile;
            String message = user.getMobileToken().substring(0, SMS_VALIDATION_DIGITS);
            KavenegarApi api = new KavenegarApi(SMS_API_KEY);
            api.verifyLookup(receptor, message, "", "", "mfa");
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
        user.setMobileToken(String.valueOf(token) + cTimeStamp);
        Name dn = buildDn(user.getUserId());
        Context context = buildAttributes(user.getUserId(), user, dn);
        ldapTemplate.modifyAttributes((DirContextOperations) context);
        return "Mobile Token for " + user.getUserId() + " is created";

    }

    public HttpStatus verifySMS(String userId, String token) {
        // return OK or code 200: token is valid and time is ok
        // return requestTimeOut or error 408: token is valid but time is not ok
        // return forbidden or error code 403: token is not valid
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        User user = userRepo.retrieveUser(userId);

        String mainDbToken = user.getMobileToken();
        String mainPartToken;

        mainPartToken = mainDbToken.substring(0, SMS_VALIDATION_DIGITS);


        if (token.equals(mainPartToken)) {
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            long cTimeStamp = currentTimestamp.getTime();


            String timeStamp = mainDbToken.substring(SMS_VALIDATION_DIGITS);

            if ((cTimeStamp - Long.valueOf(timeStamp)) < (60000 * SMS_VALID_TIME)) {
                return HttpStatus.OK;
            } else
                return HttpStatus.REQUEST_TIMEOUT;


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
        if (p.getMail() != null) context.setAttributeValue("photoName", p.getPhotoName());

        if (p.getResetPassToken() != null) context.setAttributeValue("resetPassToken", p.getResetPassToken());

        if (p.getMemberOf() != null) {

            for (int i = 0; i < p.getMemberOf().size(); i++) {
                if (i == 0) context.setAttributeValue("ou", p.getMemberOf().get(i));
                else context.addAttributeValue("ou", p.getMemberOf().get(i));
            }
        }

        if (p.getDescription() != null) context.setAttributeValue("description", p.getDescription());
        if (p.getPhotoName() != null) context.setAttributeValue("photoName", p.getPhotoName());
        if (p.getStatus() != null) context.setAttributeValue("userStatus", p.getStatus());
        if (p.getMobileToken() != null) context.setAttributeValue("mobileToken", p.getMobileToken());


        return context;
    }

}