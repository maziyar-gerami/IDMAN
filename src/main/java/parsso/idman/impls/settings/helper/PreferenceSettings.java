package parsso.idman.impls.settings.helper;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Setting;

import java.util.List;
import java.util.prefs.Preferences;

public class PreferenceSettings {
  MongoTemplate mongoTemplate;
  public Preferences systemPreferences = Preferences.systemRoot();

  public PreferenceSettings(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public void run() {
    List<Setting> settings = mongoTemplate.find(new Query(), Setting.class, Variables.col_properties);
    for (Setting setting : settings) {
      System.out.println(setting.get_id());
      systemPreferences.put(setting.get_id(), setting.getValue());
    }
    System.out.println("Settings set!");
  }

}
