package parsso.idman.Models;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SkyRoom {
    boolean enable;
    String presenter;
    String students;

    public SkyRoom(boolean enable, String presenter, String students) {
        this.enable = enable;
        this.presenter = presenter;
        this.students = students;
    }
}
