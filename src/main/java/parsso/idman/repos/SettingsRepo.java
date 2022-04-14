package parsso.idman.repos;

import org.springframework.http.HttpStatus;
import parsso.idman.models.other.Property;
import parsso.idman.models.other.Setting;
import parsso.idman.repoImpls.settings.settings.subclasses.BackupSettings.Backup;

import java.io.IOException;
import java.util.List;

public interface SettingsRepo {
    List<Setting> retrieve();

    List<Setting> retrieveALL();

    HttpStatus update(String doer, List<Property> properties);

    HttpStatus backup(String doerID);

    Object retrieveProperties(long id) ;

    HttpStatus reset(String doerID) throws IOException;

    HttpStatus restore(String name, String id);

    List<Backup> listBackups(String lang);
}
