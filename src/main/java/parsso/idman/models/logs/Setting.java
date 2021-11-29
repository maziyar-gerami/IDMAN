package parsso.idman.models.logs;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Setting {
    private String name;
    private String value;
    private String description;
    private String groupFA;
    @JsonProperty("group")
    private String groupEN;
    private String system;
}