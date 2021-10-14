package parsso.idman.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.Models.Logs.Audit;
import parsso.idman.Models.Logs.Event;
import parsso.idman.Models.Logs.Report;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.RepoImpls.logs.AuditsRepoImpl;
import parsso.idman.RepoImpls.logs.EventsRepoImpl;
import parsso.idman.RepoImpls.logs.ReportsRepoImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
public class LogsController {
    final AuditsRepoImpl auditRepo;
    EventsRepoImpl eventRepo;
    ReportsRepoImpl reportsRepo;

    @Autowired
    public LogsController(AuditsRepoImpl auditRepo) {
        this.auditRepo = auditRepo;
    }


    @GetMapping("/api/logs/audits/users")
    public ResponseEntity<Audit.ListAudits> getUsersAudits(@RequestParam(name = "userID", defaultValue = "") String userID,
                                                           @RequestParam(name = "date", defaultValue = "") String date,
                                                           @RequestParam(name = "page") String page,
                                                           @RequestParam(name = "count") String count) {
        return new ResponseEntity<>(auditRepo.retrieve(userID, date, Integer.parseInt(page), Integer.parseInt(count)), HttpStatus.OK);
    }

    @GetMapping("/api/logs/audits/user")
    public ResponseEntity<Audit.ListAudits> getUserAudits(HttpServletRequest request,
                                                          @RequestParam(name = "date", defaultValue = "") String date,
                                                          @RequestParam(name = "page") String page,
                                                          @RequestParam(name = "count") String count) {
        return new ResponseEntity<>(auditRepo.retrieve(request.getUserPrincipal().getName(), date, Integer.parseInt(page), Integer.parseInt(count)), HttpStatus.OK);
    }


    @GetMapping("/api/logs/events/users")
    public ResponseEntity<Event.ListEvents> getUsersEvents(@RequestParam(name = "userID", defaultValue = "") String userID,
                                                           @RequestParam(name = "date", defaultValue = "") String date,
                                                           @RequestParam(name = "page") String page,
                                                           @RequestParam(name = "count") String count) {
        return new ResponseEntity<>(eventRepo.retrieve(userID, date, !page.equals("") ? Integer.parseInt(page) : 0,
                !count.equals("") ? Integer.parseInt(count) : 0), HttpStatus.OK);
    }

    @GetMapping("/api/logs/events/user")
    public ResponseEntity<Event.ListEvents> getUserEvents(HttpServletRequest request,
                                                          @RequestParam(name = "date", defaultValue = "") String date,
                                                          @RequestParam(name = "page") String page,
                                                          @RequestParam(name = "count") String count) {
        return new ResponseEntity<>(eventRepo.retrieve(request.getUserPrincipal().getName(), date, !page.equals("") ? Integer.parseInt(page) : 0,
                !count.equals("") ? Integer.parseInt(count) : 0), HttpStatus.OK);

    }


    @GetMapping("/api/logs/reports/users")
    public ResponseEntity<Report.ListReports> getUsersReports(@RequestParam(name = "userID", defaultValue = "") String userID,
                                                              @RequestParam(name = "date", defaultValue = "") String date,
                                                              @RequestParam(name = "page") String page,
                                                              @RequestParam(name = "count") String count) {
        return new ResponseEntity<>(reportsRepo.retrieve(userID, date, Integer.parseInt(page), Integer.parseInt(count)), HttpStatus.OK);

    }

    @GetMapping("/api/logs/reports/user")
    public ResponseEntity<Report.ListReports> getUserReports(HttpServletRequest request,
                                                             @RequestParam(name = "date", defaultValue = "") String date,
                                                             @RequestParam(name = "page") String page,
                                                             @RequestParam(name = "count") String count) {
        return new ResponseEntity<>(reportsRepo.retrieve(request.getUserPrincipal().getName(), date, Integer.parseInt(page), Integer.parseInt(count)), HttpStatus.OK);
    }

    @GetMapping("/api/logs/reports/serviceAccess")
    public ResponseEntity<List<ReportMessage>> accessManaging(@RequestParam(value = "page") String p,
                                                              @RequestParam(value = "count") String n,
                                                              @RequestParam(value = "name", defaultValue = "") String instanceName,
                                                              @RequestParam(value = "id", defaultValue = "") String id,
                                                              @RequestParam(value = "date", defaultValue = "") String date,
                                                              @RequestParam(value = "doerID", defaultValue = "") String doerId) {
        long _id = !id.equals("") ? Long.parseLong(id) : 0;
        int page = !p.equals("") ? Integer.parseInt(p) : 0;
        int nRows = !n.equals("") ? Integer.parseInt(n) : 0;

        return new ResponseEntity<>(reportsRepo.accessManaging(page, nRows, _id, date, doerId, instanceName), HttpStatus.OK);
    }


}





