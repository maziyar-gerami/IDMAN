package parsso.idman.models.groups;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import parsso.idman.models.license.License;

import java.util.List;

@Getter
@Setter
@ToString
public class Group {
    @Id
    private String id;
    private String name;
    private String description;
    private long usersCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private License service;


    public Group() {
    }

    @Setter
    @Getter
    public static class GroupUsers {
        List<String> add;
        List<String> remove;
    }
}
