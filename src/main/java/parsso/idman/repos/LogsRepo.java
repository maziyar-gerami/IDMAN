package parsso.idman.repos;


import org.springframework.stereotype.Service;
import parsso.idman.Models.License.License;
import parsso.idman.Models.Logs.Audit;
import parsso.idman.Models.Logs.Event;
import parsso.idman.Models.Logs.Report;
import parsso.idman.Models.Logs.ReportMessage;

import java.io.IOException;
import java.util.List;

@Service

public class LogsRepo{
public interface AuditRepo {
	Audit.ListAudits retrieve(String userId, String date, int p, int n);
}

	public interface EventRepo {

		Event.ListEvents retrieve(String userId, String date, int p, int n);

		Event.ListEvents retrieveListSizeEvents(int page, int n);

		List<Event> analyze(int skip, int limit);
	}

	public interface ReportRepo {
		Report.ListReports retrieve(String userId, String date, int p, int n);

		List<ReportMessage> accessManaging(int page, int nRows, long id, String date, String doerId, String instanceName);
	}

	public interface TranscriptRepo {
		License servicesOfGroup(String ouid);

		License servicesOfUser(String userId);

	}
}
