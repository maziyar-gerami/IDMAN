package parsso.idman.Repos;


import org.json.simple.parser.ParseException;
import parsso.idman.Models.License.License;
import parsso.idman.Models.Logs.Transcript;

import java.io.IOException;

public interface TranscriptRepo {


    License servicesOfGroup(String ouid) throws IOException, ParseException;

    License servicesOfUser(String userId) throws IOException, ParseException;

    Transcript usersAndGroupsOfService(long serviceId) throws IOException, ParseException;

}
