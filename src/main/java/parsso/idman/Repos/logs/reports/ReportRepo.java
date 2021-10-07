package parsso.idman.Repos.logs.reports;


import parsso.idman.Models.Logs.Report;
import parsso.idman.Models.Logs.ReportMessage;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ReportRepo {
	Report.ListReports retrieve(String userId, String date, int p, int n) throws ParseException;

	Report.ListReports getListSizeLogs(int page, int n) throws IOException, org.json.simple.parser.ParseException;

	Report.ListReports getListUserLogs(String userId, int page, int n) throws IOException, org.json.simple.parser.ParseException;

	Report.ListReports getLogsByDate(String date, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

	Report.ListReports getListUserLogByDate(String date, String userId, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

	List<ReportMessage> accessManaging(int page, int nRows, long id, String date, String doerId, String instanceName) throws ParseException;
}
