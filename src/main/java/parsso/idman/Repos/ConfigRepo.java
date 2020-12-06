package parsso.idman.Repos;

import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.Config;
import parsso.idman.Models.Setting;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface ConfigRepo {

    String retrieveSetting() throws IOException;

    String updateSettings(List<Setting> settings) throws IOException;

    HttpStatus backupConfig();

    HttpStatus factoryReset() throws IOException;

    HttpStatus restore(String name) throws IOException, ParseException, java.text.ParseException;

    List<Config> listBackedUpConfigs() throws IOException, ParseException;
}
