package parsso.idman.utils.SMS.Magfa.Classes;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    public long getMid() {
        return mid;
    }

    @SuppressWarnings("unused")
    public void setMid(long value) {
        this.mid = value;
    }

    @SuppressWarnings("unused")
    public int getStatus() {
        return status;
    }

    @SuppressWarnings("unused")
    public void setStatus(int value) {
        this.status = value;
    }

    @SuppressWarnings("unused")
    public String getDate() {
        return date;
    }

    @SuppressWarnings("unused")
    public void setDate(String value) {
        this.date = value;
    }

    @SuppressWarnings("unused")
    public String getSmsc() {
        return smsc;
    }

    @SuppressWarnings("unused")
    public void setSmsc(String value) {
        this.smsc = value;
    }

}
