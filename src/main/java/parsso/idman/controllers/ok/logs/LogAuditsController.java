package parsso.idman.controllers.ok.logs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.Models.Logs.Audit;
import parsso.idman.RepoImpls.logs.AuditsRepoImpl;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;


@RestController
@RequestMapping("/api/logs/audits")
public class LogAuditsController {
	AuditsRepoImpl auditRepo;

	@Autowired
	public LogAuditsController(AuditsRepoImpl auditRepo) {
		this.auditRepo = auditRepo;
	}

	@GetMapping("/users")
	public ResponseEntity<Audit.ListAudits> getUsersAudits(@RequestParam(name = "userID", defaultValue = "") String userID,
	                                                       @RequestParam(name = "date", defaultValue = "") String date,
	                                                       @RequestParam(name = "page") String page,
	                                                       @RequestParam(name = "count") String count) throws ParseException {
		return new ResponseEntity<>(auditRepo.retrieve(userID, date, Integer.parseInt(page), Integer.parseInt(count)), HttpStatus.OK);
	}

	@GetMapping("/user")
	public ResponseEntity<Audit.ListAudits> getUserAudits(HttpServletRequest request,
	                                                      @RequestParam(name = "date", defaultValue = "") String date,
	                                                      @RequestParam(name = "page") String page,
	                                                      @RequestParam(name = "count") String count) throws ParseException {
		return new ResponseEntity<>(auditRepo.retrieve(request.getUserPrincipal().getName(), date, Integer.parseInt(page), Integer.parseInt(count)), HttpStatus.OK);
	}
}





