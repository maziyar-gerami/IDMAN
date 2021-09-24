package parsso.idman.Utils.SMS.Magfa.Classes;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stringArray", namespace = "http://jaxb.dev.java.net/array", propOrder = {
		"item"
})
public class StringArray {
	@XmlElement(nillable = true)
	protected List<String> item;

	public List<String> getItem() {
		if (item == null) {
			item = new ArrayList<>();
		}
		return this.item;
	}

}
