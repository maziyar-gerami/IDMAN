package parsso.idman.Repos;

import org.springframework.stereotype.Service;
import parsso.idman.Models.Audit;
import parsso.idman.Models.Audit;
import parsso.idman.Models.ListAudits;
import parsso.idman.Models.ListAudits;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
@Service
public interface AuditRepo {

    List<Audit> getMainListAudits() throws ParseException, IOException, org.json.simple.parser.ParseException;
    
    ListAudits getListSizeAudits(int page, int n) throws IOException, org.json.simple.parser.ParseException;

    List<Audit> getListAudits(int page, int n) throws IOException, org.json.simple.parser.ParseException;

    ListAudits getListUserAudits(String userId, int page, int n) throws IOException, org.json.simple.parser.ParseException;

    ListAudits getAuditsByDate(String date, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

    ListAudits getListUserAuditByDate(String date, String userId, int page, int n) throws ParseException, IOException, org.json.simple.parser.ParseException;

}
