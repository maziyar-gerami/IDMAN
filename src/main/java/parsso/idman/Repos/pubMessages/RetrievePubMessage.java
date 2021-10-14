package parsso.idman.Repos.pubMessages;


import parsso.idman.Models.other.PublicMessage;

import java.util.List;

public interface RetrievePubMessage {
	List<PublicMessage> showVisiblePubicMessages();

	List<PublicMessage> showAllPubicMessages(String id);

}