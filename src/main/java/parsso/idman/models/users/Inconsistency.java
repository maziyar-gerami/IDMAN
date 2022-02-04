package parsso.idman.models.users;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Inconsistency {
    List<String> add;
    List<String> remove;

    public Inconsistency(List<String> add, List<String> remove) {
        this.add = add;
        this.remove = remove;
    }
}
