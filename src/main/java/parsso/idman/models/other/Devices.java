package parsso.idman.models.other;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.helpers.TimeHelper;

@Setter
@Getter
public class Devices {
    public Devices(){}

    long _id;
    String username;
    String name;
    Time time;

    public Time getTime() {
        return TimeHelper.longToPersianTime(_id);
    }
}
