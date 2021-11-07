package parsso.idman.utils.sms.magfa.classes;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendMessage", propOrder = {
        "alphabet",
        "id",
        "parts",
        "recipient",
        "status",
        "tariff",
        "userId"
})
public class SendMessage {
    protected String alphabet;
    protected long id;
    protected int parts;
    protected String recipient;
    protected int status;
    protected double tariff;
    protected long userId;

    public String getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(String value) {
        this.alphabet = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long value) {
        this.id = value;
    }

    public int getParts() {
        return parts;
    }

    public void setParts(int value) {
        this.parts = value;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String value) {
        this.recipient = value;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int value) {
        this.status = value;
    }

    public double getTariff() {
        return tariff;
    }

    public void setTariff(double value) {
        this.tariff = value;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long value) {
        this.userId = value;
    }

}
