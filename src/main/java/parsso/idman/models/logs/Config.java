package parsso.idman.models.logs;


import lombok.Getter;
import lombok.Setter;
import parsso.idman.models.other.Time;

import java.util.List;

@Getter
@Setter

public class Config {
    private String name;
    private Time dateTime;
    private List<Setting> settingList;
}
