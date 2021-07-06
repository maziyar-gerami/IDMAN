package parsso.idman.Repos;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Logs.Setting;

import java.io.IOException;
import java.util.List;

@Service
public interface SettingsRepo {
    HttpStatus emailNotification();

    HttpStatus messageNotification();

    List<Setting> retrieveTFSetting() throws IOException;

}
