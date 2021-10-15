package parsso.idman.Models.Users;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Inconsistency {
    List add;
    List remove;

    public Inconsistency(List add, List remove) {
        this.add = add;
        this.remove = remove;
    }
}
