package parsso.idman.mobile.repos;


import com.google.zxing.WriterException;
import org.springframework.http.HttpStatus;
import parsso.idman.models.users.User;

import java.io.IOException;

@SuppressWarnings("SameReturnValue")
public interface ServicesRepo {
    @SuppressWarnings("unused")
    byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException;

    @SuppressWarnings("unused")
    String ActivationSendMessage(User user);

    @SuppressWarnings("unused")
    String insertMobileToken1(User user);

    @SuppressWarnings("unused")
    HttpStatus verifySMS(String userId, String token);

    @SuppressWarnings("unused")
    String randomString(int len);
}
