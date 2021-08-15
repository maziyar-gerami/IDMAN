package parsso.idman.Repos;


import org.json.simple.parser.ParseException;
import parsso.idman.Models.Logs.Transcript;

import java.io.IOException;

public interface TranscriptRepo {


    Transcript servicesOfGroup(String ouid) throws IOException, ParseException;

    Transcript servicesOfUser(String userId) throws IOException, ParseException;

}
