package parsso.idman.Utils.SMS.Magfa.Classes;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deliveryStatus", propOrder = {
        "mid",
        "status",
        "date",
        "smsc"
})
public class DeliveryStatus {
    protected long mid;
    protected int status;
    @XmlElement(required = true)
    protected String date;
    protected String smsc;

    public long getMid() {
        return mid;
    }

    public void setMid(long value) {
        this.mid = value;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int value) {
        this.status = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String value) {
        this.date = value;
    }

    public String getSmsc() {
        return smsc;
    }

    public void setSmsc(String value) {
        this.smsc = value;
    }

}
