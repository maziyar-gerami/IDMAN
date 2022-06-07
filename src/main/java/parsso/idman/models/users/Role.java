package parsso.idman.models.users;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Role {
  public Role(int _id, String role) {
    this._id = _id;
    this.role = role;
  }

  int _id;
  String role;
}
