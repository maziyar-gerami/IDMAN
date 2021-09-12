package parsso.idman.Utils.SMS.Magfa.Classes;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(name = "MagfaSoapServer", targetNamespace = "webservice.magfa.com")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({
		ObjectFactory.class
})
public interface MagfaSoapServer {
	@WebMethod
	@WebResult(partName = "return")
	MessagesResult messages(
			@WebParam(name = "count", partName = "count")
					int count,
			@WebParam(name = "shortNumber", partName = "shortNumber")
					String shortNumber);

	@WebMethod
	@WebResult(partName = "return")
	MessageIdResult mid(
			@WebParam(name = "uid", partName = "uid")
					long uid);

	@WebMethod
	@WebResult(partName = "return")
	DeliveryResult statuses(
			@WebParam(name = "mids", partName = "mids")
					LongArray mids);

	@WebMethod
	@WebResult(partName = "return")
	SendResult send(
			@WebParam(name = "messages", partName = "messages")
					StringArray messages,
			@WebParam(name = "senders", partName = "senders")
					StringArray senders,
			@WebParam(name = "recipients", partName = "recipients")
					StringArray recipients,
			@WebParam(name = "uids", partName = "uids")
					LongArray uids,
			@WebParam(name = "encodings", partName = "encodings")
					IntArray encodings,
			@WebParam(name = "udhs", partName = "udhs")
					StringArray udhs,
			@WebParam(name = "priorities", partName = "priorities")
					IntArray priorities);

	@WebMethod
	@WebResult(partName = "return")
	CreditResult balance();

}
