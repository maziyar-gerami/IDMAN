package parsso.idman.Models.other;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("rawtypes")
@Setter
@Getter
public class Notification implements Comparable {
    private String title;
    private String url;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private long timestamp;
    private Time time;

    @Override
    public int compareTo(Object o) {
        if (this.timestamp > ((Notification) o).getTimestamp())
            return 1;
        else
            return -1;
    }
}