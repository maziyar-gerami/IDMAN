package parsso.idman.Mobile.Repos;

import com.google.zxing.WriterException;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.DirContextOperations;
import parsso.idman.Models.User;

import javax.naming.Name;
import java.io.IOException;

public interface ServicesRepo {
    byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException;

    String ActivationSendMessage(User user);

    String insertMobileToken1(User user);

    HttpStatus verifySMS(String userId, String token);

    DirContextOperations buildAttributes(String uid, User p, Name dn);

    String randomString(int len);
}
