package parsso.idman.Repos.groups;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public interface DeleteGroup {
	HttpStatus remove(String doerId, JSONObject jsonObject) throws IOException, ParseException;

}
