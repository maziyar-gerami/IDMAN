package parsso.idman.Repos.pubMessages;


import org.springframework.http.HttpStatus;
import parsso.idman.Models.other.PublicMessage;

public interface UpdatePubMessage {

	HttpStatus editPubicMessage(String doer, PublicMessage message);

}