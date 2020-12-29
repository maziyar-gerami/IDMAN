package parsso.idman.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

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
    @JsonIgnore
    private String serverIpAddress;
    private Time dateAndTime;
    @JsonIgnore
    String dateTime;

    public void setActionPerformed(String actionPerformed) {
        this.actionPerformed = actionPerformed;
        dateTime = Time.convertDateTimeGeorgian(whenActionWasPerformed.toString());
    }

    public Time getTime() {
        dateAndTime = new Time(Integer.valueOf(dateTime.substring(0, 4))
                , Integer.valueOf(dateTime.substring(5, 7))
                , Integer.valueOf(dateTime.substring(8, 10))
                ,Integer.valueOf(dateTime.substring(11,13))
                ,Integer.valueOf(dateTime.substring(14,16))
                , Integer.valueOf(dateTime.substring(17,19)));

        return dateAndTime;
    }

}
