package parsso.idman.Repos;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Users.PublicMessage;

import java.util.List;

@Service
public interface PubMessageRepo {

    List<PublicMessage> showPubicMessages(String id);

    HttpStatus postPubicMessage(String doer, String message);

    HttpStatus editPubicMessage(String doer, String message);

    HttpStatus deletePubicMessage(String doer, String id);
}