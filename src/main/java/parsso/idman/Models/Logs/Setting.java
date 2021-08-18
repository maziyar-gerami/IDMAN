package parsso.idman.Models.Logs;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Setting {
    private String name;
    private String value;
    private String description;
    private String group;
    private String sub_group;
    private String system;
    private boolean changable;
}