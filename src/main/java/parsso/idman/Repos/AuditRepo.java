package parsso.idman.Repos;

import org.springframework.stereotype.Service;
import parsso.idman.Models.*;
import parsso.idman.Models.Audit;
import parsso.idman.Models.ListAudits;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
@Service
public interface AuditRepo {

    ListAudits getListSizeAudits(int page, int n) throws IOException, org.json.simple.parser.ParseException;

    ListAudits getListUserAudits(String userId, int page, int n) throws IOException, org.json.simple.parser.ParseException;

    ListAudits getAuditsByDate(String date, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

    ListAudits getListUserAuditByDate(String date, String userId, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

    List<Audit> analyze(String collection, int skip, int limit);


}
