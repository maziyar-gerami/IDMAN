package parsso.idman.utils.sms.magfa.classes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deliveryResult", propOrder = {
    "status",
    "dlrs"
})
public class DeliveryResult {
  protected int status;
  @XmlElement(required = true)
  protected List<DeliveryStatus> dlrs;

  public int getStatus() {
    return status;
  }

  public void setStatus(int value) {
    this.status = value;
  }

  public List<DeliveryStatus> getDlrs() {
    if (dlrs == null) {
      dlrs = new ArrayList<>();
    }
    return this.dlrs;
  }

}
