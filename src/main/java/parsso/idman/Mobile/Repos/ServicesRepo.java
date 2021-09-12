package parsso.idman.Mobile.Repos;


import com.google.zxing.WriterException;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import parsso.idman.Models.Users.User;

import java.io.IOException;

public interface ServicesRepo {
	byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException;

	String ActivationSendMessage(User user);

	String insertMobileToken1(User user);

	HttpStatus verifySMS(String userId, String token) throws IOException, ParseException;

	String randomString(int len);
}
