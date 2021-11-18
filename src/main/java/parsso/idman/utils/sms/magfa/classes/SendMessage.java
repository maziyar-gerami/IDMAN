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

    @SuppressWarnings("unused")
    public String getAlphabet() {
        return alphabet;
    }

    @SuppressWarnings("unused")
    public void setAlphabet(String value) {
        this.alphabet = value;
    }

    @SuppressWarnings("unused")
    public long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(long value) {
        this.id = value;
    }

    @SuppressWarnings("unused")
    public int getParts() {
        return parts;
    }

    @SuppressWarnings("unused")
    public void setParts(int value) {
        this.parts = value;
    }

    @SuppressWarnings("unused")
    public String getRecipient() {
        return recipient;
    }

    @SuppressWarnings("unused")
    public void setRecipient(String value) {
        this.recipient = value;
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
    public double getTariff() {
        return tariff;
    }

    @SuppressWarnings("unused")
    public void setTariff(double value) {
        this.tariff = value;
    }

    @SuppressWarnings("unused")
    public long getUserId() {
        return userId;
    }

    @SuppressWarnings("unused")
    public void setUserId(long value) {
        this.userId = value;
    }

}
