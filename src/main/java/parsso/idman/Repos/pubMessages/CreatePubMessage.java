package parsso.idman.Repos.pubMessages;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import parsso.idman.Models.other.PublicMessage;

import java.util.List;

public interface CreatePubMessage {

	HttpStatus postPubicMessage(String doer, PublicMessage message);

}