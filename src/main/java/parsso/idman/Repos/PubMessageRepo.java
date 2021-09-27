package parsso.idman.Repos;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import parsso.idman.Models.other.PublicMessage;

import java.util.List;

public interface PubMessageRepo {
	List<PublicMessage> showVisiblePubicMessages();

	List<PublicMessage> showAllPubicMessages(String id);

	HttpStatus postPubicMessage(String doer, PublicMessage message);

	HttpStatus editPubicMessage(String doer, PublicMessage message);

	HttpStatus deletePubicMessage(String doer, JSONObject jsonObject);
}