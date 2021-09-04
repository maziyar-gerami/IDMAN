package parsso.idman.Models.Logs;


import lombok.Getter;
import lombok.Setter;
import parsso.idman.Models.Time;

import java.util.List;

@Getter
@Setter

public class Config {
    private String name;
    private Time dateTime;
    private List<Setting> settingList;
}
