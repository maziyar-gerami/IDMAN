package parsso.idman.Repos.email;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Users.User;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@Service
public interface EmailService {
	HttpStatus sendMail(JSONObject jsonObject) throws IOException, ParseException;

	int sendMail(String email, String cid, String answer) throws IOException, ParseException;

	void sendMail(User user, String day) throws MessagingException;

	int sendMail(String email, String uid, String cid, String answer) throws IOException, ParseException;

	List<JSONObject> checkMail(String email);
}
