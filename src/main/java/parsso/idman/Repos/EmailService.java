package parsso.idman.Repos;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Users.User;

import javax.mail.MessagingException;
import java.util.List;

@Service
public interface EmailService {

    HttpStatus sendMail(JSONObject jsonObject);

    int sendMail(String email, String cid, String answer);

    void sendMail(User user, String day) throws MessagingException;

    int sendMail(String email, String uid, String cid, String answer);

    List<JSONObject> checkMail(String email);
}
