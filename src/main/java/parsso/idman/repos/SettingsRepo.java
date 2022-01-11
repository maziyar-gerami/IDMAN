package parsso.idman.repos;

import org.springframework.http.HttpStatus;
import parsso.idman.models.other.Property;
import parsso.idman.models.other.Setting;

import java.util.List;

public interface SettingsRepo {
    List<Setting> retrieve();

    HttpStatus update(List<Property> properties);
}
