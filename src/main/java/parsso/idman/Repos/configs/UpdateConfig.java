package parsso.idman.Repos.configs;


import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import parsso.idman.Models.Logs.Config;
import parsso.idman.Models.Logs.Setting;

import java.io.IOException;
import java.util.List;

public interface UpdateConfig {

	String updateSettings(String doerID, List<Setting> settings) throws IOException, ParseException;

}
