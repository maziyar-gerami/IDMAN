package parsso.idman.Utils.Other;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GenerateUUID {

    public static String getUUID() throws IOException {

        String command = "wmic csproduct get UUID";
        StringBuffer output = new StringBuffer();

        Process SerNumProcess = Runtime.getRuntime().exec(command);
        BufferedReader sNumReader = new BufferedReader(new InputStreamReader(SerNumProcess.getInputStream()));

        String line = "";
        while ((line = sNumReader.readLine()) != null) {
            output.append(line + "\n");
        }
        return output.toString().substring(output.indexOf("\n"), output.length()).trim();
    }
}
