package parsso.idman.helpers;

import java.util.List;

import com.google.common.io.Files;

public class Extentsion {

  public static String getExtension(String filename) {
    return Files.getFileExtension(filename);
  }

  public static boolean check(String filename, List<String> extensions) {
    if (extensions.contains(getExtension(filename.toLowerCase()))) {
      return true;
    }
    return false;
  }

}
