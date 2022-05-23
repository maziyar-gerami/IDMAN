package parsso.idman.models.services.serviceType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimpleService implements Comparable  {
  long _id;
  String url;
  int position;

  @Override
  public int compareTo(Object o) {
    SimpleService second = (SimpleService) o;
    return Integer.compare(second.getPosition(), this.getPosition());
  }
}
