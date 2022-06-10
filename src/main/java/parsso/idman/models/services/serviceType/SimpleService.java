package parsso.idman.models.services.serviceType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimpleService implements Comparable<SimpleService> {
  long _id;
  String url;
  int position;
  String serviceId;
  String name;
  String description;

  @Override
  public int compareTo(SimpleService second) {
    return Integer.compare(second.getPosition(), this.getPosition());
  }
}
