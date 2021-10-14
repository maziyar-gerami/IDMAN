package parsso.idman.Repos.configs;


import org.springframework.http.HttpStatus;

import java.io.IOException;

public interface CreateConfig {

	HttpStatus saveToMongo() throws IOException;
}
