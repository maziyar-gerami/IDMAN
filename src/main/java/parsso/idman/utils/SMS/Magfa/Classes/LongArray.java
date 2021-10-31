package parsso.idman.utils.SMS.Magfa.Classes;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("HttpUrlsUsage")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "longArray", namespace = "http://jaxb.dev.java.net/array", propOrder = {
        "item"
})
public class LongArray {
    @XmlElement(nillable = true)
    protected List<Long> item;

    public List<Long> getItem() {
        if (item == null) {
            item = new ArrayList<>();
        }
        return this.item;
    }

}
