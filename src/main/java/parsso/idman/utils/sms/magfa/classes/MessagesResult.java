package parsso.idman.utils.sms.magfa.classes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "messagesResult", propOrder = {
    "status",
    "messages"
})
public class MessagesResult {
  protected int status;
  @XmlElement(required = true)
  protected List<DatedCustomerReturnIncomingFormat> messages;

  public int getStatus() {
    return status;
  }

  public void setStatus(int value) {
    this.status = value;
  }

  public List<DatedCustomerReturnIncomingFormat> getMessages() {
    if (messages == null) {
      messages = new ArrayList<>();
    }
    return this.messages;
  }

}
