package parsso.idman.configs;

import java.util.List;
import java.util.prefs.Preferences;

import parsso.idman.models.other.Setting;

public class Prefs {
  Preferences myPrefs;

  public static String get(String name) {
    Preferences myPrefs = Preferences.userRoot().node("System");
    return myPrefs.get(name, null);
  }

  public Prefs(String key, String value) {
    myPrefs = Preferences.userRoot().node("System");
    myPrefs.put(key, value);
  }

  public Prefs(List<Setting> retrieve) {
    for (Setting setting : retrieve) {
      myPrefs = Preferences.userRoot().node("System");
      myPrefs.put(setting.get_id(), setting.getValue());
    }
  }
}
