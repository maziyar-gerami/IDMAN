package parsso.idman.helpers;

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
}
