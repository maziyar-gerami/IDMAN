package parsso.idman.Models.Logs;


import lombok.Getter;
import lombok.Setter;
import org.javers.core.diff.changetype.ValueChange;

@Getter
@Setter
public class Changes {
	private String attribute;
	private Object from;
	private Object to;

	public Changes(){

		this.attribute = null;
		this.from = null;
		this.to = null;
	}

	public Changes(Object from, Object to) {
		this.from = from;
		this.to = to;
	}

	public Changes(ValueChange c) {
		this.attribute = c.getPropertyName();
		this.from = c.getLeft();
		this.to = c.getRight();

	}
}