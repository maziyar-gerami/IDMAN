package parsso.idman.repos;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import parsso.idman.Models.other.PublicMessage;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("SameReturnValue")
public interface PubMessageRepo {
	List<PublicMessage> showVisiblePubicMessages();

	List<PublicMessage> showAllPubicMessages(String id);

	HttpStatus postPubicMessage(String doer, PublicMessage message);

	HttpStatus editPubicMessage(String doer, PublicMessage message);

	HttpStatus deletePubicMessage(String doer, JSONObject jsonObject);
}