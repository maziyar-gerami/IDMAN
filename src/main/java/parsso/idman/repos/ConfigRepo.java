package parsso.idman.repos;


import org.springframework.http.HttpStatus;
import parsso.idman.models.logs.Config;
import parsso.idman.models.other.Setting;

import java.io.IOException;
import java.util.List;


public interface ConfigRepo {

    String retrieveSetting() throws IOException;

    void updateSettings(String doerID, List<Setting> settings);

    HttpStatus backupConfig();

    HttpStatus factoryReset(String doerID) throws IOException;

    HttpStatus restore(String doerID, String name);

    List<Config> listBackedUpConfigs();

    HttpStatus saveToMongo() throws IOException;
}
