package parsso.idman.utils.sms.magfa.classes;


import javax.xml.bind.annotation.*;

@SuppressWarnings("ALL")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customerReturnIncomingFormat", propOrder = {
        "body",
        "senderNumber",
        "recipientNumber",
        "errorResult"
})
@XmlSeeAlso({
        DatedCustomerReturnIncomingFormat.class
})
public class CustomerReturnIncomingFormat {
    @XmlElement(required = true)
    protected String body;
    @XmlElement(required = true)
    protected String senderNumber;
    @XmlElement(required = true)
    protected String recipientNumber;
    @XmlElement(required = true)
    protected String errorResult;

    public String getBody() {
        return body;
    }

    public void setBody(String value) {
        this.body = value;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String value) {
        this.senderNumber = value;
    }

    public String getRecipientNumber() {
        return recipientNumber;
    }

    public void setRecipientNumber(String value) {
        this.recipientNumber = value;
    }

    public String getErrorResult() {
        return errorResult;
    }

    public void setErrorResult(String value) {
        this.errorResult = value;
    }

}
