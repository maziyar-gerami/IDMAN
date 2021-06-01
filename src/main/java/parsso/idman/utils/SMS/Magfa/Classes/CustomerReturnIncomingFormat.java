package parsso.idman.Utils.SMS.Magfa.Classes;


import javax.xml.bind.annotation.*;


/**
 * <p>Java class for customerReturnIncomingFormat complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="customerReturnIncomingFormat">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="body" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="senderNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="recipientNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="errorResult" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
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

    /**
     * Gets the value of the body property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBody() {
        return body;
    }

    /**
     * Sets the value of the body property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBody(String value) {
        this.body = value;
    }

    /**
     * Gets the value of the senderNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSenderNumber() {
        return senderNumber;
    }

    /**
     * Sets the value of the senderNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSenderNumber(String value) {
        this.senderNumber = value;
    }

    /**
     * Gets the value of the recipientNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRecipientNumber() {
        return recipientNumber;
    }

    /**
     * Sets the value of the recipientNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRecipientNumber(String value) {
        this.recipientNumber = value;
    }

    /**
     * Gets the value of the errorResult property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getErrorResult() {
        return errorResult;
    }

    /**
     * Sets the value of the errorResult property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setErrorResult(String value) {
        this.errorResult = value;
    }

}
