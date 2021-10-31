package parsso.idman.models.other;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SkyRoom {
    boolean enable;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String presenter;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String students;

    public SkyRoom(String skyroomEnable, String role, String presenter, String students) {
        if (!(role.equalsIgnoreCase("USER")) && Boolean.parseBoolean(skyroomEnable)) {
            this.enable = true;
            this.presenter = presenter;
            this.students = students;
        } else {
            this.enable = false;
            this.presenter = null;
        }
    }
}
