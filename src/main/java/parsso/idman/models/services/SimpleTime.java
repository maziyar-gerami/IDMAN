package parsso.idman.models.services;

import lombok.Getter;
import lombok.Setter;
import parsso.idman.models.other.Time;

@SuppressWarnings("rawtypes")
@Getter
@Setter
public class SimpleTime implements Comparable<SimpleTime> {
  int hour;
  int minute;

  public SimpleTime() {

  }

  public SimpleTime(Time time) {
    this.hour = time.getHours();
    this.minute = time.getMinutes();
  }

  @Override
  public int compareTo(SimpleTime o) {
    if (this.hour > o.getHour())
      return 1;

    else if (this.hour == ((SimpleTime) o).getHour())
      if (this.getMinute() > ((SimpleTime) o).getMinute())
        return 1;

    return -1;
  }
}