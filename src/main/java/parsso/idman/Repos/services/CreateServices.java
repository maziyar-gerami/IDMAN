package parsso.idman.Repos.services;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import parsso.idman.Models.Services.Service;
import parsso.idman.Models.Services.ServiceGist;
import parsso.idman.Models.Services.ServiceType.MicroService;
import parsso.idman.Models.Users.User;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public interface CreateServices {

	String uploadMetadata(MultipartFile file);

	HttpStatus createService(String doerID, JSONObject jsonObject, String system) throws IOException, ParseException;

}
