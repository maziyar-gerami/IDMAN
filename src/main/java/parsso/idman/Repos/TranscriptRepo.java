package parsso.idman.Repos;


import org.json.simple.parser.ParseException;
import parsso.idman.Models.License.License;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.Logs.Transcript;

import java.io.IOException;
import java.util.List;

public interface TranscriptRepo {
    License servicesOfGroup(String ouid) throws IOException, ParseException;

    License servicesOfUser(String userId) throws IOException, ParseException;

    Transcript usersAndGroupsOfService(long serviceId) throws IOException, ParseException;

    List<ReportMessage> accessManaging(Long id, String type, String item);
}
