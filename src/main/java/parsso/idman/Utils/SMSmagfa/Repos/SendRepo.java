package parsso.idman.Utils.SMSmagfa.Repos;

import java.net.MalformedURLException;
import java.util.ArrayList;

public interface SendRepo {
    public ArrayList<String> SendMessage(String message, String PhoneNumber, Long id) throws MalformedURLException;
}
