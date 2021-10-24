package parsso.idman.Models.Users;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@SuppressWarnings("unchecked")
@Setter
@Getter
public class Inconsistency {
    List<Object> add;
    List<Object> remove;

    public Inconsistency(List add, List remove) {
        this.add = add;
        this.remove = remove;
    }
}
