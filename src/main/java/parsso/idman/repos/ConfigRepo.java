package parsso.idman.repos;


import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import parsso.idman.Models.Logs.Config;
import parsso.idman.Models.Logs.Setting;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("SameReturnValue")
public interface ConfigRepo {

	String retrieveSetting() throws IOException;

	void updateSettings(String doerID, List<Setting> settings);

	HttpStatus backupConfig();

	HttpStatus factoryReset(String doerID) throws IOException;

	HttpStatus restore(String doerID, String name);

	List<Config> listBackedUpConfigs();

	HttpStatus saveToMongo() throws IOException;
}
