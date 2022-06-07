package parsso.idman.controllers;

import java.time.Duration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.excelView.AuditsExcelView;
import parsso.idman.helpers.excelView.EventsExcelView;
import parsso.idman.helpers.excelView.LogsExcelView;

import parsso.idman.models.response.Response;
import parsso.idman.postconstruct.LogsTime;
import parsso.idman.repos.LogsRepo;

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
  final Bucket bucket;

  @Autowired
  public LogsController(
      LogsRepo.AuditRepo auditRepo, MongoTemplate mongoTemplate, LogsRepo.EventRepo eventRepo,
      LogsRepo.ReportRepo reportsRepo, EventsExcelView eventsExcelView,
      AuditsExcelView auditsExcelView,
      LogsExcelView reportExcelView, LogsExcelView logsExcelView) {
    this.auditRepo = auditRepo;
    this.eventRepo = eventRepo;
    this.reportsRepo = reportsRepo;
    this.eventsExcelView = eventsExcelView;
    this.auditsExcelView = auditsExcelView;
    this.logsExcelView = logsExcelView;
    this.mongoTemplate = mongoTemplate;
    Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
    this.bucket = Bucket.builder()
        .addLimit(limit)
        .build();
  }

  @GetMapping("/audits/users")
  public ResponseEntity<Response> getUsersAudits(
      @RequestParam(value = "userID", defaultValue = "") String userID,
      @RequestParam(value = "startDate", defaultValue = "") String startDate,
      @RequestParam(value = "endDate", defaultValue = "") String endDate,
      @RequestParam(value = "service", defaultValue = "") String service,
      @RequestParam(name = "page") String page,
      @RequestParam(name = "count") String count,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NumberFormatException, NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(new Response(
        auditRepo.retrieve(userID, startDate, endDate, Integer.parseInt(page),
            Integer.parseInt(count), (service.equals("") ? 0l : Long.parseLong(service))),
        Variables.MODEL_LOGS, HttpStatus.OK.value(), lang), HttpStatus.OK);
  }

  @GetMapping("/audits/user")
  public ResponseEntity<Response> getUserAudits(HttpServletRequest request,
      @RequestParam(value = "startDate", defaultValue = "") String startDate,
      @RequestParam(value = "endDate", defaultValue = "") String endDate,
      @RequestParam(value = "service", defaultValue = "") String service,
      @RequestParam(name = "page") String page,
      @RequestParam(name = "count") String count,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NumberFormatException, NoSuchFieldException, IllegalAccessException {
    Thread lt = new Thread(() -> new LogsTime(mongoTemplate).run());
    lt.start();

    return new ResponseEntity<>(
        new Response(
            auditRepo.retrieve(request.getUserPrincipal().getName(), startDate,
                endDate,
                Integer.parseInt(page), Integer.parseInt(count), (service.equals("") ? 0l : Long.parseLong(service))),
            Variables.MODEL_LOGS, HttpStatus.OK.value(), lang),
        HttpStatus.OK);

  }

  @GetMapping("/events/users")
  public ResponseEntity<Response> getUsersEvents(
      @RequestParam(value = "userID", defaultValue = "") String userID,
      @RequestParam(value = "startDate", defaultValue = "") String startDate,
      @RequestParam(value = "endDate", defaultValue = "") String endDate,
      @RequestParam(name = "action", defaultValue = "") String action,
      @RequestParam(name = "page") String page,
      @RequestParam(name = "count") String count,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NumberFormatException, NoSuchFieldException, IllegalAccessException {

    Thread lt = new Thread(() -> new LogsTime(mongoTemplate).run());
    lt.start();

    return new ResponseEntity<>(new Response(
        eventRepo.retrieve(userID, startDate, endDate,
            !page.equals("") ? Integer.parseInt(page) : 0,
            !count.equals("") ? Integer.parseInt(count) : 0, action),
        Variables.MODEL_LOGS, HttpStatus.OK.value(), lang), HttpStatus.OK);

  }

  @GetMapping("/events/user")
  public ResponseEntity<Response> getUserEvents(HttpServletRequest request,
      @RequestParam(value = "startDate", defaultValue = "") String startDate,
      @RequestParam(value = "endDate", defaultValue = "") String endDate,
      @RequestParam(value = "action", defaultValue = "") String action,
      @RequestParam(name = "page") String page,
      @RequestParam(name = "count") String count,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NumberFormatException, NoSuchFieldException, IllegalAccessException {
    Thread lt = new Thread(() -> new LogsTime(mongoTemplate).run());
    lt.start();
    return new ResponseEntity<>(new Response(
        eventRepo.retrieve(request.getUserPrincipal().getName(), startDate, endDate,
            !page.equals("") ? Integer.parseInt(page) : 0,
            !count.equals("") ? Integer.parseInt(count) : 0, action),
        Variables.MODEL_LOGS, HttpStatus.OK.value(), lang), HttpStatus.OK);

  }

  @GetMapping("/reports/users")
  public ResponseEntity<Response> getUsersReports(
      @RequestParam(value = "userID", defaultValue = "") String userID,
      @RequestParam(value = "startDate", defaultValue = "") String startDate,
      @RequestParam(value = "endDate", defaultValue = "") String endDate,
      @RequestParam(value = "services", defaultValue = "") List<String> services,
      @RequestParam(name = "page") String page,
      @RequestParam(name = "count") String count,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NumberFormatException, NoSuchFieldException, IllegalAccessException {

    return new ResponseEntity<>(new Response(
        reportsRepo.retrieve(userID, startDate, endDate, Integer.parseInt(page),
            Integer.parseInt(count), services),
        Variables.MODEL_LOGS, HttpStatus.OK.value(), lang), HttpStatus.OK);

  }

  @GetMapping("/reports/user")
  public ResponseEntity<Response> getUserReports(HttpServletRequest request,
      @RequestParam(value = "startDate", defaultValue = "") String startDate,
      @RequestParam(value = "endDate", defaultValue = "") String endDate,
      @RequestParam(value = "services", defaultValue = "") List<String> services,
      @RequestParam(name = "page") String page,
      @RequestParam(name = "count") String count,
      @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NumberFormatException, NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(new Response(
        reportsRepo.retrieve(request.getUserPrincipal().getName(), startDate, endDate, Integer.parseInt(page),
            Integer.parseInt(count), services),
        Variables.MODEL_LOGS, HttpStatus.OK.value(), lang), HttpStatus.OK);

  }

  @GetMapping("/reports/serviceAccess")
  public ResponseEntity<Response> accessManaging(@RequestParam(value = "page") String p,
      @RequestParam(value = "count") String n,
      @RequestParam(value = "name", defaultValue = "") String instanceName,
      @RequestParam(value = "id", defaultValue = "") String id,
      @RequestParam(name = "startDate", defaultValue = "") String startDate,
      @RequestParam(name = "endDate", defaultValue = "") String endDate,
      @RequestParam(value = "doerID", defaultValue = "") String doerId,
      @RequestParam(name = "lang", defaultValue = Variables.DEFAULT_LANG) String lang)
      throws NumberFormatException, NoSuchFieldException, IllegalAccessException {

    long l = !id.equals("") ? Long.parseLong(id) : 0;
    int page = !p.equals("") ? Integer.parseInt(p) : 0;
    int count = !n.equals("") ? Integer.parseInt(n) : 0;

    return new ResponseEntity<>(
        new Response(reportsRepo.accessManaging(page, count, l, startDate, endDate, doerId,
            instanceName),
            Variables.MODEL_LOGS, HttpStatus.OK.value(), lang),
        HttpStatus.OK);

  }

  @SuppressWarnings("ConstantConditions")
  @GetMapping("/export")
  public ModelAndView downloadExcelAudit(@RequestParam("type") String type) {
    // return a view which will be resolved by an excel view resolver
    if (bucket.tryConsume(1)) {
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
    return null;
  }
}
