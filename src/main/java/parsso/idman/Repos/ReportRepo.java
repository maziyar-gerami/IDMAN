package parsso.idman.Repos;


import parsso.idman.Models.Logs.ListReports;
import parsso.idman.Models.Logs.Report;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ReportRepo {
    ListReports getListSizeLogs(int page, int n) throws IOException, org.json.simple.parser.ParseException;

    ListReports getListUserLogs(String userId, int page, int n) throws IOException, org.json.simple.parser.ParseException;

    ListReports getLogsByDate(String date, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

    ListReports getListUserLogByDate(String date, String userId, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

    List<Report> analyze(String collection, int skip, int limit);
}
