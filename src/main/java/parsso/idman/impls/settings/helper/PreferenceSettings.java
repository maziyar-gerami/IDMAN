package parsso.idman.impls.settings.helper;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Setting;
import parsso.idman.repos.SettingsRepo;

import java.util.List;
import java.util.prefs.Preferences;

public class PreferenceSettings {
  MongoTemplate mongoTemplate;
  public Preferences systemPreferences = Preferences.systemRoot();
  public SettingsRepo settingsRepo;

  public PreferenceSettings(MongoTemplate mongoTemplate, SettingsRepo settingsRepo) {
    this.mongoTemplate = mongoTemplate;
    this.settingsRepo = settingsRepo;
  }

  public void run(String BASE_URL,String BASE_DN) {
    System.out.println(mongoTemplate);
    System.out.println(settingsRepo);
    System.out.println(BASE_DN);
    System.out.println(BASE_URL);
    List<Setting> settings = settingsRepo.retrieve();
    systemPreferences.put(Variables.PREFS_BASE_URL, BASE_URL);
    systemPreferences.put(Variables.PREFS_BASE_DN, BASE_DN);
    
  for (Setting setting : settings) {
    systemPreferences.put(setting.get_id(), setting.getValue());
    }

  }

}
