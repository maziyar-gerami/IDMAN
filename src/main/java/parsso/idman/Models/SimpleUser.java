package parsso.idman.Models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class SimpleUser implements Serializable {
    private String userId;
    private String displayName;
    private List<String> memberOf;
}
