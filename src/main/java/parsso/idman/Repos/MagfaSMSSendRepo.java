package parsso.idman.Repos;


import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.ArrayList;

@Service
public interface MagfaSMSSendRepo {
	ArrayList<String> SendMessage(String message, String PhoneNumber, Long id) throws MalformedURLException;

}
