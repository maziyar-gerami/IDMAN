package parsso.idman.Repos.logs.audits;


import org.springframework.stereotype.Service;
import parsso.idman.Models.Logs.ListAudits;

import java.io.IOException;
import java.text.ParseException;

@Service
public interface AuditRepo {
	ListAudits retrieve(String userId, String date, int p, int n) throws ParseException;

	ListAudits retrieveListSizeAudits(int page, int n) throws IOException, org.json.simple.parser.ParseException;

	ListAudits retrieveUserAudits(String userId, int page, int n) throws IOException, org.json.simple.parser.ParseException;

	ListAudits retrieveAuditsByDate(String date, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

	ListAudits retrieveUserAuditsByDate(String date, String userId, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

}
