package parsso.idman.Models.Logs;


import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import parsso.idman.Helpers.TimeHelper;
import parsso.idman.Models.Time;

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
        return TimeHelper.longToPersianTime(whenActionWasPerformed.getTime());
    }


}
