package parsso.idman.utils.SMS.Magfa.Classes;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "messageIdResult", propOrder = {
        "status",
        "mid"
})
public class MessageIdResult {
    protected int status;
    protected long mid;

    public int getStatus() {
        return status;
    }

    public void setStatus(int value) {
        this.status = value;
    }

    public long getMid() {
        return mid;
    }

    public void setMid(long value) {
        this.mid = value;
    }

}
