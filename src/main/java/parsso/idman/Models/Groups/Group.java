package parsso.idman.Models.Groups;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import parsso.idman.Models.License.License;

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

    public Group(String name, String description, long usersCount) {

        this.name = name;
        this.description = description;
        this.usersCount = usersCount;
    }

    public Group() {
    }

    @Setter
    @Getter
    public static class GroupUsers {
        List<String> add;
        List<String> remove;
    }


}
