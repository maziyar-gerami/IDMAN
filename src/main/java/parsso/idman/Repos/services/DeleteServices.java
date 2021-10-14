package parsso.idman.Repos.services;


import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.LinkedList;

public interface DeleteServices {
	LinkedList<String> deleteServices(String doerID, JSONObject files) throws IOException;

}