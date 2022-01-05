package parsso.idman.models.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Setting {
    private String _id;
    private String value;
    private String descriptionEN;
    private String descriptionFA;
    private String groupFA;
    private String groupEN;

}