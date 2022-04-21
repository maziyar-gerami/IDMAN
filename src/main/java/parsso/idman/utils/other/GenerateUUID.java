package parsso.idman.utils.other;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class GenerateUUID {
  public static String getUUID() throws IOException {

    StringBuilder output = new StringBuilder();

    BufferedReader sNumReader = new BufferedReader(
        new InputStreamReader(new ByteArrayInputStream(UUID.randomUUID().toString().getBytes())));

    String line;
    while ((line = sNumReader.readLine()) != null) {
      output.append(line).append("\n");
    }
    return output.substring(output.indexOf("\n"), output.length()).trim();
  }
}
