package parsso.idman.Repos;


import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import parsso.idman.Models.Logs.Config;
import parsso.idman.Models.Logs.Setting;

import java.io.IOException;
import java.util.List;

public interface ConfigRepo {
	List<Setting> retrieveTFSetting() throws IOException;

	String retrieveSetting() throws IOException;

	String updateSettings(String doerID, List<Setting> settings) throws IOException;

	HttpStatus backupConfig();

	HttpStatus factoryReset(String doerID) throws IOException;

	HttpStatus restore(String doerID, String name) throws IOException, ParseException, java.text.ParseException;

	List<Config> listBackedUpConfigs() throws IOException, ParseException;

	HttpStatus saveToMongo() throws IOException;
}
