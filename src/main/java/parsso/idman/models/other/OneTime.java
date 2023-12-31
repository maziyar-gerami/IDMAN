package parsso.idman.models.other;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OneTime {
  String _id;
  boolean run;
  Long time;

  public OneTime(String id, boolean run, Long time) {
    this._id = id;
    this.run = run;
    this.time = time;
  }

  public OneTime() {
  }

  public OneTime(String id) {
    this._id = id;
    this.run = false;
    this.time = 0L;
  }

}
