package parsso.idman.Repos.configs;


import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import parsso.idman.Models.Logs.Config;

import java.io.IOException;
import java.util.List;

public interface BackupConfig {

	HttpStatus backupConfig();

	List<Config> listBackedUpConfigs() throws IOException, ParseException;

}
