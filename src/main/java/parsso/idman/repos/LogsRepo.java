package parsso.idman.repos;


import org.springframework.stereotype.Service;
import parsso.idman.models.license.License;
import parsso.idman.models.logs.Audit;
import parsso.idman.models.logs.Event;
import parsso.idman.models.logs.Report;
import parsso.idman.models.logs.ReportMessage;

import java.util.List;

@Service

public class LogsRepo {
    public interface AuditRepo {
        Audit.ListAudits retrieve(String userId, String startDate, String endDate, int p, int n);
    }

    public interface EventRepo {

        Event.ListEvents retrieve(String userId, String startDate, String endDate, int p, int n);

        Event.ListEvents retrieveListSizeEvents(int page, int n);

        List<Event> analyze(int skip, int limit);
    }

    public interface ReportRepo {
        Report.ListReports retrieve(String userId, String startDate, String endDate, int p, int n);

        ReportMessage.ListReportMessage accessManaging(int page, int nRows, long id, String startDate, String endDate, String doerId, String instanceName);
    }

    public interface TranscriptRepo {


        License servicesOfUser(String userId);

    }
}
