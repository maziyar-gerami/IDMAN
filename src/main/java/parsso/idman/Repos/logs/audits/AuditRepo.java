package parsso.idman.Repos.logs.audits;


import org.springframework.stereotype.Service;
import parsso.idman.Models.Logs.ListAudits;

import java.io.IOException;
import java.text.ParseException;

@Service
public interface AuditRepo {
	ListAudits retrieveListSizeAudits(int page, int n) throws IOException, org.json.simple.parser.ParseException;

	ListAudits getListUserAudits(String userId, int page, int n) throws IOException, org.json.simple.parser.ParseException;

	ListAudits getAuditsByDate(String date, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

	ListAudits getListUserAuditByDate(String date, String userId, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

}
