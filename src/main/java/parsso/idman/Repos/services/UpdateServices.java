package parsso.idman.Repos.services;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import parsso.idman.Models.Services.Service;

import java.io.IOException;

public interface UpdateServices {

	HttpStatus updateService(String doerID, long id, JSONObject jsonObject, String system) throws IOException, ParseException;

	HttpStatus increasePosition(String id);

	HttpStatus decreasePosition(String id);

	HttpStatus updateOuIdChange(String doerID, Service service, long sid, String name, String oldOu, String newOu) throws IOException;

}
