package parsso.idman.Repos;


import org.springframework.http.HttpStatus;
import parsso.idman.Models.PublicMessage;

import java.util.List;

public interface PubMessageRepo {


    List<PublicMessage> showPubicMessages(String id);

    HttpStatus postPubicMessage(String doer, PublicMessage message);

    HttpStatus editPubicMessage(String doer, PublicMessage message);

    HttpStatus deletePubicMessage(String doer, String id);
}