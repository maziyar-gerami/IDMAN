package parsso.idman.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import parsso.idman.Helpers.ExcelView.AuditsExcelView;
import parsso.idman.Models.Logs.ListAudits;
import parsso.idman.Repos.logs.audits.AuditRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;

@Controller
public class AuditsController {
	private final AuditRepo auditRepo;
	private AuditsExcelView auditsExcelView;

	public AuditsController(AuditRepo auditRepo) {
		this.auditRepo = auditRepo;
	}

	@Autowired
	public AuditsController(AuditRepo auditRepo, AuditsExcelView auditsExcelView) {
		this.auditRepo = auditRepo;
		this.auditsExcelView = auditsExcelView;
	}

	//************************************* Pages ****************************************

	@SuppressWarnings("SameReturnValue")
	@GetMapping("/audits")
	public String getPageAudits() {
		return "audits";
	}

	//************************************* APIs ****************************************

	@GetMapping("/api/audits/users/{page}/{n}")
	public ResponseEntity<ListAudits> retrieveAllAudits(@PathVariable("page") int page, @PathVariable("n") int n) throws IOException, org.json.simple.parser.ParseException {
		return new ResponseEntity<>(auditRepo.retrieveListSizeAudits(page, n), HttpStatus.OK);
	}

	@GetMapping("/api/audits/users/{userId}/{page}/{n}")
	public ResponseEntity<ListAudits> retrieveByUser(@PathVariable String userId, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, org.json.simple.parser.ParseException {
		return new ResponseEntity<>(auditRepo.getListUserAudits(userId, page, n), HttpStatus.OK);
	}

	@GetMapping("/api/audits/users/date/{date}/{page}/{n}")
	public ResponseEntity<ListAudits> retrieveByDate(@PathVariable String date, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, ParseException, org.json.simple.parser.ParseException {
		return new ResponseEntity<>(auditRepo.getAuditsByDate(date, page, n), HttpStatus.OK);
	}

	@GetMapping("/api/audits/users/{id}/date/{date}/{page}/{n}")
	public ResponseEntity<ListAudits> retrieveByUserDate(@PathVariable String id, @PathVariable String date, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, ParseException, org.json.simple.parser.ParseException {
		return new ResponseEntity<>(auditRepo.getListUserAuditByDate(date, id, page, n), HttpStatus.OK);
	}

	@GetMapping("/api/audits/user/{page}/{n}")
	public ResponseEntity<ListAudits> retrieveCurrentUserAudits(HttpServletRequest request, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, org.json.simple.parser.ParseException {
		return new ResponseEntity<>(auditRepo.getListUserAudits(request.getUserPrincipal().getName(), page, n), HttpStatus.OK);
	}

	@GetMapping("/api/audits/user/date/{date}/{page}/{n}")
	public ResponseEntity<ListAudits> retrieveCurrentUserAuditsByDate(HttpServletRequest request, @PathVariable String date, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, ParseException, org.json.simple.parser.ParseException {
		return new ResponseEntity<>(auditRepo.getListUserAuditByDate(date, request.getUserPrincipal().getName(), page, n), HttpStatus.OK);
	}

	@GetMapping("/api/audits/users/export")
	public ModelAndView downloadExcel() {
		// return a view which will be resolved by an excel view resolver
		//noinspection ConstantConditions
		return new ModelAndView(auditsExcelView, "listAudits", null);
	}

}
