package parsso.idman.models.users;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class UsersGroups {
  private Inconsistency users;
  private Inconsistency groups;

  public UsersGroups(Inconsistency users, Inconsistency groups) {
    this.users = users;
    this.groups = groups;
  }

  @Setter
  @Getter
  public static class Inconsistency {
    List<String> add;
    List<String> remove;

    public Inconsistency(List<String> add, List<String> remove) {
      this.add = add;
      this.remove = remove;
    }
  }
}
