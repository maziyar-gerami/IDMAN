package parsso.idman.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.Date;

@Setter
@Getter
public class Audit {
    ObjectId _id;
    private String principal;
    private String resourceOperatedUpon;
    private String actionPerformed;
    private String applicationCode;
    private Date whenActionWasPerformed;
    private String clientIpAddress;
    private String serverIpAddress;

    private Time time;

    public Time getTime() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(whenActionWasPerformed);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);



        time = new Time(year
                , month
                , day
                ,hour
                ,minute
                , seconds);

        return time;
    }

}
