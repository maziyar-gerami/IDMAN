package parsso.idman.repos;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import parsso.idman.models.other.PublicMessage;

import java.util.List;

@SuppressWarnings("SameReturnValue")
public interface PubMessageRepo {
    List<PublicMessage> showVisiblePubicMessages();

    List<PublicMessage> showAllPubicMessages(String id);

    HttpStatus postPubicMessage(String doer, PublicMessage message);

    HttpStatus editPubicMessage(String doer, PublicMessage message);

    HttpStatus deletePubicMessage(String doer, JSONObject jsonObject);
}