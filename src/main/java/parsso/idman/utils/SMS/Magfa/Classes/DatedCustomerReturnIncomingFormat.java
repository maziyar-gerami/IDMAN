package parsso.idman.Utils.SMS.Magfa.Classes;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for datedCustomerReturnIncomingFormat complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="datedCustomerReturnIncomingFormat">
 *   &lt;complexContent>
 *     &lt;extension base="{webservice.magfa.com}customerReturnIncomingFormat">
 *       &lt;sequence>
 *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "datedCustomerReturnIncomingFormat", propOrder = {
        "date"
})
public class DatedCustomerReturnIncomingFormat
        extends CustomerReturnIncomingFormat {

    @XmlElement(required = true)
    protected String date;

    /**
     * Gets the value of the date property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDate(String value) {
        this.date = value;
    }

}
