package parsso.idman.Utils.SMS.Magfa.Classes;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "datedCustomerReturnIncomingFormat", propOrder = {
        "date"
})
public class DatedCustomerReturnIncomingFormat
        extends CustomerReturnIncomingFormat {
    @XmlElement(required = true)
    protected String date;

    public String getDate() {
        return date;
    }

    public void setDate(String value) {
        this.date = value;
    }

}
