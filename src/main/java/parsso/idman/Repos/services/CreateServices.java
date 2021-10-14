package parsso.idman.Repos.services;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CreateServices {

	String uploadMetadata(MultipartFile file);

	HttpStatus createService(String doerID, JSONObject jsonObject, String system) throws IOException, ParseException;

}
