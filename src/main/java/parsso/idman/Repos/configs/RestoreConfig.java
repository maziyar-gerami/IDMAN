package parsso.idman.Repos.configs;


import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public interface RestoreConfig {

	HttpStatus factoryReset(String doerID) throws IOException, ParseException;

	HttpStatus restore(String doerID, String name) throws IOException, ParseException, java.text.ParseException;


}
