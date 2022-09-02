package parsso.idman.impls.settings.helper;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import parsso.idman.configs.Prefs;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Setting;
import parsso.idman.repos.SettingsRepo;

import java.util.List;
import java.util.prefs.Preferences;

public class PreferenceSettings {
  MongoTemplate mongoTemplate;
  public SettingsRepo settingsRepo;

  public PreferenceSettings(MongoTemplate mongoTemplate, SettingsRepo settingsRepo) {
    this.mongoTemplate = mongoTemplate;
    this.settingsRepo = settingsRepo;
  }

  public void run(String BASE_URL,String BASE_DN) {

    List<Setting> settings = settingsRepo.retrieve();
    new Prefs(Variables.PREFS_BASE_URL, BASE_URL);
    
  for (Setting setting : settings) {
    new Prefs(setting.get_id(), setting.getValue());
    }

  }

}
