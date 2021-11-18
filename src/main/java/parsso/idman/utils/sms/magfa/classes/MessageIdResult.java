package parsso.idman.utils.sms.magfa.classes;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@SuppressWarnings("unused")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "messageIdResult", propOrder = {
        "status",
        "mid"
})
public class MessageIdResult {
    protected int status;
    protected long mid;

    @SuppressWarnings("unused")
    public int getStatus() {
        return status;
    }

    @SuppressWarnings("unused")
    public void setStatus(int value) {
        this.status = value;
    }

    @SuppressWarnings("unused")
    public long getMid() {
        return mid;
    }

    @SuppressWarnings("unused")
    public void setMid(long value) {
        this.mid = value;
    }

}
