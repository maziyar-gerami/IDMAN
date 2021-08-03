package parsso.idman.Models.Groups;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;


@Getter
@Setter
@ToString
public class Group {


    @Id
    private String id;
    private String name;
    private String description;
    private long usersCount;

    public Group(String name, String description, long usersCount) {

        this.name = name;
        this.description = description;
        this.usersCount = usersCount;
    }

    public Group() {
    }

}
