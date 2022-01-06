package parsso.idman.repos;

import parsso.idman.models.other.Setting;

import java.util.List;

public interface SettingsRepo {
    List<Setting> retrieve();
}
