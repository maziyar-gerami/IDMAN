package parsso.idman.helpers;

import java.time.ZoneId;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import parsso.idman.models.other.Time;

@Setter
@Getter
public class LogTime {

  long millis;
  Time t;
  String date;
  String time;

  public LogTime(long millis) {
    this.t = Time.longToPersianTime(millis);
    this.date = t.getYear() + "/" + t.getMonth() + "/" + t.getDay();
    this.time = t.getHours() + ":" + t.getMinutes() + ":" + t.getSeconds();
  }

  public static long[] rangeCreator(String startDate, String endDate) {
    long[] range = null;

    if (!startDate.equals("") && !endDate.equals("")) {
      range = new Time().dateRangeToEpochRange(new Time().stringInputToTime(startDate),
          new Time().stringInputToTime(endDate), ZoneId.of(Variables.ZONE));

    } else if (!startDate.equals("") && endDate.equals("")) {
      Time now = Time.longToPersianTime(new Date().getTime());

      range = new Time().dateRangeToEpochRange(new Time(Integer.parseInt(startDate.substring(4)),
          Integer.parseInt(startDate.substring(2, 4)), Integer.parseInt(startDate.substring(0, 2))),
          new Time(now.getYear(), now.getMonth(), now.getDay()), ZoneId.of(Variables.ZONE));

    } else if (startDate.equals("") && !endDate.equals("")) {
      range = new Time().dateRangeToEpochRange(Time.longToPersianTime(new Date().getTime()),
          new Time().stringInputToTime(endDate),
          ZoneId.of(Variables.ZONE));
    }

    return range;
  }
}
