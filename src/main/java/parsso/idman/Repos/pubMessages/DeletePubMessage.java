package parsso.idman.Repos.pubMessages;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import parsso.idman.Models.other.PublicMessage;

import java.util.List;

public interface DeletePubMessage {

	HttpStatus deletePubicMessage(String doer, JSONObject jsonObject);
}