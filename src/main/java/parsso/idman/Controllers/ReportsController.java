package parsso.idman.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import parsso.idman.Helpers.LogsExcelView;
import parsso.idman.Models.Logs.ListReports;
import parsso.idman.Repos.ReportRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Controller
public class ReportsController {
	@Autowired
	private ReportRepo reportRepo;
	@Autowired
	private LogsExcelView logsExcelView;

	//************************************* Pages ****************************************

	@SuppressWarnings("SameReturnValue")
	@GetMapping("/reports")
	public String getPageReports() {
		return "reports";
	}

	//************************************* APIs ****************************************

	@GetMapping("/api/reports/users/{page}/{n}")
	public ResponseEntity<ListReports> retrieveAllLogs(@PathVariable("page") int page, @PathVariable("n") int n) throws IOException, org.json.simple.parser.ParseException {
		return new ResponseEntity<>(reportRepo.getListSizeLogs(page, n), HttpStatus.OK);
	}

	@GetMapping("/api/reports/users/{userId}/{page}/{n}")
	public ResponseEntity<ListReports> retrieveByUser(@PathVariable String userId, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, org.json.simple.parser.ParseException {
		return new ResponseEntity<>(reportRepo.getListUserLogs(userId, page, n), HttpStatus.OK);
	}

	@GetMapping("/api/reports/users/date/{date}/{page}/{n}")
	public ResponseEntity<ListReports> retrieveByDate(@PathVariable String date, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, ParseException, org.json.simple.parser.ParseException {
		return new ResponseEntity<>(reportRepo.getLogsByDate(date, page, n), HttpStatus.OK);
	}

	@GetMapping("/api/reports/users/{id}/date/{date}/{page}/{n}")
	public ResponseEntity<ListReports> retrieveByUserDate(@PathVariable String id, @PathVariable String date, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, ParseException, org.json.simple.parser.ParseException {
		return new ResponseEntity<>(reportRepo.getListUserLogByDate(date, id, page, n), HttpStatus.OK);
	}

	@GetMapping("/api/reports/user/{page}/{n}")
	public ResponseEntity<ListReports> retrieveCurrentUserLogs(HttpServletRequest request, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, org.json.simple.parser.ParseException {
		Principal principal = request.getUserPrincipal();
		ListReports listReports = reportRepo.getListUserLogs(principal.getName(), page, n);
		return new ResponseEntity<>(listReports, HttpStatus.OK);
	}

	@GetMapping("/api/reports/user/date/{date}/{page}/{n}")
	public ResponseEntity<ListReports> retrieveCurrentUserLogsByDate(HttpServletRequest request, @PathVariable String date, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, ParseException, org.json.simple.parser.ParseException {
		Principal principal = request.getUserPrincipal();
		return new ResponseEntity<>(reportRepo.getListUserLogByDate(date, principal.getName(), page, n), HttpStatus.OK);
	}

	@GetMapping("/api/reports/users/export")
	public ModelAndView downloadExcel() {

		// return a view which will be resolved by an excel view resolver
		//noinspection ConstantConditions
		return new ModelAndView(logsExcelView, "listLogs", null);
	}
}
