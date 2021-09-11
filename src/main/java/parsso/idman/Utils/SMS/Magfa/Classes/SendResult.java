package parsso.idman.Utils.SMS.Magfa.Classes;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendResult", propOrder = {
		"status",
		"messages"
})
public class SendResult {
	protected int status;
	@XmlElement(required = true)
	protected List<SendMessage> messages;

	public int getStatus() {
		return status;
	}

	public void setStatus(int value) {
		this.status = value;
	}

	public List<SendMessage> getMessages() {
		if (messages == null) {
			messages = new ArrayList<SendMessage>();
		}
		return this.messages;
	}

}
