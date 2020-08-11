package parsso.idman.Repos;

import parsso.idman.Models.Setting;

import java.io.IOException;
import java.util.List;

public interface ConfigRepo {

    public String retrieveSetting() throws IOException;
    public String updateSettings(List<Setting> settings) throws IOException;


}
