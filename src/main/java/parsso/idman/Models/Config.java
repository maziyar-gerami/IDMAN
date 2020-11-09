package parsso.idman.Models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class Config {

    private String name;

    private Time dateTime;

    private List<Setting> settingList;
}
