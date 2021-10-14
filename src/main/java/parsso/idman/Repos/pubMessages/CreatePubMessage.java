package parsso.idman.Repos.pubMessages;


import org.springframework.http.HttpStatus;
import parsso.idman.Models.other.PublicMessage;

public interface CreatePubMessage {

	HttpStatus postPubicMessage(String doer, PublicMessage message);

}