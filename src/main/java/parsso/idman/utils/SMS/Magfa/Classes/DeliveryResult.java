
package parsso.idman.Utils.SMS.Magfa.Classes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for deliveryResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deliveryResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="dlrs" type="{webservice.magfa.com}deliveryStatus" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deliveryResult", propOrder = {
    "status",
    "dlrs"
})
public class DeliveryResult {

    protected int status;
    @XmlElement(required = true)
    protected List<DeliveryStatus> dlrs;

    /**
     * Gets the value of the status property.
     *
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     */
    public void setStatus(int value) {
        this.status = value;
    }

    /**
     * Gets the value of the dlrs property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dlrs property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDlrs().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DeliveryStatus }
     *
     *
     */
    public List<DeliveryStatus> getDlrs() {
        if (dlrs == null) {
            dlrs = new ArrayList<DeliveryStatus>();
        }
        return this.dlrs;
    }

}
