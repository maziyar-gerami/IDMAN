package parsso.idman.Repos;


import parsso.idman.Models.Log;
import parsso.idman.Models.ListLogs;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface LogRepo {


    ListLogs getListSizeLogs(int page, int n) throws IOException, org.json.simple.parser.ParseException;

    ListLogs getListUserLogs(String userId, int page, int n) throws IOException, org.json.simple.parser.ParseException;

    ListLogs getLogsByDate(String date, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

    ListLogs getListUserLogByDate(String date, String userId, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

    List<Log> analyze(String collection, int skip, int limit);
}
