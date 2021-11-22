package parsso.idman.models.logs;


import lombok.Getter;
import lombok.Setter;
import org.javers.core.diff.changetype.ValueChange;

@Getter
@Setter
public class Change {
    private String attribute;
    private Object from;
    private Object to;

    @SuppressWarnings("unused")
    public Change() {

        this.attribute = null;
        this.from = null;
        this.to = null;
    }

    @SuppressWarnings("unused")
    public Change(Object from, Object to) {
        this.from = from;
        this.to = to;
    }

    public Change(ValueChange c) {
        this.attribute = c.getPropertyName();
        this.from = c.getLeft();
        this.to = c.getRight();

    }
}