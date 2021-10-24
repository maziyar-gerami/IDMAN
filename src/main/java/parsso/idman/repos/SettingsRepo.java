package parsso.idman.repos;


import org.springframework.stereotype.Service;
import parsso.idman.Models.Logs.Setting;

@Service
public interface SettingsRepo {

    Setting retrieve(String settingName);

    void emailNotification();

    void instantMessageNotification();

}
