package parsso.idman.models.groups;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import parsso.idman.models.license.License;

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

  public Group(String id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  @Setter
  @Getter
  public static class GroupUsers {
    List<String> add;
    List<String> remove;
  }

  @Override
  public boolean equals(Object obj) {
    return this.id.equals(((Group) obj).id);
  }
}
