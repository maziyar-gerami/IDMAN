package parsso.idman.repos;


import org.springframework.stereotype.Service;
import parsso.idman.Models.License.License;
import parsso.idman.Models.Logs.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Service

public class LogsRepo{
public interface AuditRepo {
	Audit.ListAudits retrieve(String userId, String date, int p, int n) throws ParseException;
}

	public interface EventRepo {

		Event.ListEvents retrieve(String userId, String date, int p, int n) throws ParseException;

		Event.ListEvents retrieveListSizeEvents(int page, int n) throws IOException, org.json.simple.parser.ParseException;

		List<Event> analyze(int skip, int limit);
	}

	public interface ReportRepo {
		Report.ListReports retrieve(String userId, String date, int p, int n) throws ParseException;

		List<ReportMessage> accessManaging(int page, int nRows, long id, String date, String doerId, String instanceName) throws ParseException;
	}

	public interface TranscriptRepo {
		License servicesOfGroup(String ouid) throws IOException, org.json.simple.parser.ParseException;

		License servicesOfUser(String userId) throws IOException, org.json.simple.parser.ParseException;

	}
}
