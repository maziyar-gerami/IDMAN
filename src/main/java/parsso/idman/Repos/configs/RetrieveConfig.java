package parsso.idman.Repos.configs;


import parsso.idman.Models.Logs.Setting;

import java.io.IOException;
import java.util.List;

public interface RetrieveConfig {

	List<Setting> retrieveTFSetting() throws IOException;

	String retrieveSetting() throws IOException;

}
