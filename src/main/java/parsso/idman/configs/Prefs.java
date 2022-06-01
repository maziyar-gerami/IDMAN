package parsso.idman.configs;

import java.util.prefs.Preferences;

public class Prefs {
  Preferences myPrefs;


  public static String get(String name) {
    Preferences myPrefs = Preferences.userRoot().node("System");
    return myPrefs.get(name, "def");
  }

  public Prefs(String key, String value) {
    myPrefs = Preferences.userRoot().node("System");
    myPrefs.put(key, value);
  }
}
