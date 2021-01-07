package parsso.idman.Models;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Setting {

    private String name;
    private String value;
    private String description;
    private String group;
    private String system;
}