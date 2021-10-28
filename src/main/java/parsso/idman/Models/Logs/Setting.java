package parsso.idman.Models.Logs;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Setting {
    private String name;
    private String value;
    private String description;
    private String groupFA;
    private String groupEN;
    private String system;
}