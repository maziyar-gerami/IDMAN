package parsso.idman.mobile.Repos;

import com.google.zxing.WriterException;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.DirContextOperations;
import parsso.idman.Models.User;

import javax.naming.Name;
import java.io.IOException;

public interface ServicesRepo {
    public byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException;

    public String ActivationSendMessage(String mobile);

    public String insertMobileToken1(User user);

    public HttpStatus verifySMS(String userId, String token);

    public DirContextOperations buildAttributes(String uid, User p, Name dn);
}
