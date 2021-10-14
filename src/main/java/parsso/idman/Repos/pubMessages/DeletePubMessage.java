package parsso.idman.Repos.pubMessages;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;

public interface DeletePubMessage {

	HttpStatus deletePubicMessage(String doer, JSONObject jsonObject);
}