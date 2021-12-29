package parsso.idman.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import parsso.idman.helpers.excelView.AuditsExcelView;
import parsso.idman.helpers.excelView.EventsExcelView;
import parsso.idman.helpers.excelView.LogsExcelView;
import parsso.idman.models.logs.Audit;
import parsso.idman.models.logs.Event;
import parsso.idman.models.logs.Report;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.postConstruct.LogsTime;
import parsso.idman.repos.LogsRepo;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/logs")
public class LogsController {
    final LogsRepo.AuditRepo auditRepo;
    final LogsRepo.EventRepo eventRepo;
    final LogsRepo.ReportRepo reportsRepo;
    final EventsExcelView eventsExcelView;
    final AuditsExcelView auditsExcelView;
    final LogsExcelView logsExcelView;
    final MongoTemplate mongoTemplate;

    @Autowired
    public LogsController(LogsRepo.AuditRepo auditRepo, MongoTemplate mongoTemplate, LogsRepo.EventRepo eventRepo, LogsRepo.ReportRepo reportsRepo, EventsExcelView eventsExcelView, AuditsExcelView auditsExcelView, LogsExcelView reportExcelView, LogsExcelView logsExcelView) {
        this.auditRepo = auditRepo;
        this.eventRepo = eventRepo;
        this.reportsRepo = reportsRepo;
        this.eventsExcelView = eventsExcelView;
        this.auditsExcelView = auditsExcelView;
        this.logsExcelView = logsExcelView;
        this.mongoTemplate = mongoTemplate;
    }


    @GetMapping("/audits/users")
    public ResponseEntity<Audit.ListAudits> getUsersAudits(@RequestParam(name = "userID", defaultValue = "") String userID,
                                                           @RequestParam(name = "date", defaultValue = "") String date,
                                                           @RequestParam(name = "page") String page,
                                                           @RequestParam(name = "count") String count) {
        return new ResponseEntity<>(auditRepo.retrieve(userID, date, Integer.parseInt(page), Integer.parseInt(count)), HttpStatus.OK);
    }

    @GetMapping("/audits/user")
    public ResponseEntity<Audit.ListAudits> getUserAudits(HttpServletRequest request,
                                                          @RequestParam(name = "date", defaultValue = "") String date,
                                                          @RequestParam(name = "page") String page,
                                                          @RequestParam(name = "count") String count) {
        Thread lt = new Thread(() -> new LogsTime(mongoTemplate).run());
        lt.start();

        return new ResponseEntity<>(auditRepo.retrieve(request.getUserPrincipal().getName(), date, Integer.parseInt(page), Integer.parseInt(count)), HttpStatus.OK);
    }

    @GetMapping("/events/users")
    public ResponseEntity<Event.ListEvents> getUsersEvents(@RequestParam(name = "userID", defaultValue = "") String userID,
                                                           @RequestParam(name = "date", defaultValue = "") String date,
                                                           @RequestParam(name = "page") String page,
                                                           @RequestParam(name = "count") String count) {

        Thread lt = new Thread(() -> new LogsTime(mongoTemplate).run());
        lt.start();

        return new ResponseEntity<>(eventRepo.retrieve(userID, date, !page.equals("") ? Integer.parseInt(page) : 0,
                !count.equals("") ? Integer.parseInt(count) : 0), HttpStatus.OK);
    }

    @GetMapping("/events/user")
    public ResponseEntity<Event.ListEvents> getUserEvents(HttpServletRequest request,
                                                          @RequestParam(name = "date", defaultValue = "") String date,
                                                          @RequestParam(name = "page") String page,
                                                          @RequestParam(name = "count") String count) {
        Thread lt = new Thread(() -> new LogsTime(mongoTemplate).run());
        lt.start();
        return new ResponseEntity<>(eventRepo.retrieve(request.getUserPrincipal().getName(), date, !page.equals("") ? Integer.parseInt(page) : 0,
                !count.equals("") ? Integer.parseInt(count) : 0), HttpStatus.OK);

    }

    @GetMapping("/reports/users")
    public ResponseEntity<Report.ListReports> getUsersReports(@RequestParam(name = "userID", defaultValue = "") String userID,
                                                              @RequestParam(name = "date", defaultValue = "") String date,
                                                              @RequestParam(name = "page") String page,
                                                              @RequestParam(name = "count") String count) {
        Report.ListReports s1 = reportsRepo.retrieve(userID, date, Integer.parseInt(page), Integer.parseInt(count));

        return new ResponseEntity<>(s1, HttpStatus.OK);

    }

    @GetMapping("/reports/user")
    public ResponseEntity<Report.ListReports> getUserReports(HttpServletRequest request,
                                                             @RequestParam(name = "date", defaultValue = "") String date,
                                                             @RequestParam(name = "page") String page,
                                                             @RequestParam(name = "count") String count) {
        return new ResponseEntity<>(reportsRepo.retrieve(request.getUserPrincipal().getName(), date, Integer.parseInt(page), Integer.parseInt(count)), HttpStatus.OK);
    }

    @GetMapping("/reports/serviceAccess")
    public ResponseEntity<ReportMessage.ListReportMessage> accessManaging(@RequestParam(value = "page") String p,
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

    @SuppressWarnings("ConstantConditions")
    @GetMapping("/export")
    public ModelAndView downloadExcelAudit(@RequestParam("type") String type) {
        // return a view which will be resolved by an excel view resolver
        Thread lt = new Thread(() -> new LogsTime(mongoTemplate).run());
        lt.start();

        switch (type) {
            case "audits":
                return new ModelAndView(auditsExcelView, "listAudits", null);
            case "events":
                return new ModelAndView(eventsExcelView, "listEvents", null);
            case "reports":
                return new ModelAndView(logsExcelView, "listLogs", null);
            default:
                return null;
        }
    }
}





