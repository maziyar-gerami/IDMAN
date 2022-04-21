package parsso.idman.models.other;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Return {
  int status;
  String message;

  public Return(int status, String message) {
    this.status = status;
    this.message = message;
  }
}
