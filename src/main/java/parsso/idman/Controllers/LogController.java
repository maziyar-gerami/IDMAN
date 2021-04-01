package parsso.idman.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import parsso.idman.Helpers.LogsExcelView;
import parsso.idman.Models.ListLogs;
import parsso.idman.Repos.LogRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;

@RestController
public class LogController {

    @Autowired
    private LogRepo logRepo;
    @Autowired
    private LogsExcelView logsExcelView;

    //*************************************** Pages ***************************************

    @GetMapping("/reports")
    public String Reports() {
        return "reports";
    }

    //*************************************** APIs ***************************************


    @GetMapping("/api/logs/{page}/{n}")
    public ResponseEntity<ListLogs> retrieveAllLogs(@PathVariable("page") int page, @PathVariable("n") int n) throws IOException, org.json.simple.parser.ParseException {
        return new ResponseEntity<>(logRepo.getListSizeLogs(page, n), HttpStatus.OK);
    }

    @GetMapping("/api/logs/users/{userId}/{page}/{n}")
    public ResponseEntity<ListLogs> retrieveByUser(@PathVariable String userId, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, org.json.simple.parser.ParseException {
        return new ResponseEntity<>(logRepo.getListUserLogs(userId, page, n), HttpStatus.OK);
    }

    @GetMapping("/api/logs/date/{date}/{page}/{n}")
    public ResponseEntity<ListLogs> retrieveByDate(@PathVariable String date, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, ParseException, org.json.simple.parser.ParseException {
        return new ResponseEntity<>(logRepo.getLogsByDate(date, page, n), HttpStatus.OK);
    }

    @GetMapping("/api/logs/users/{id}/date/{date}/{page}/{n}")
    public ResponseEntity<ListLogs> retrieveByUserDate(@PathVariable String id, @PathVariable String date, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, ParseException, org.json.simple.parser.ParseException {
        return new ResponseEntity<>(logRepo.getListUserLogByDate(date, id, page, n), HttpStatus.OK);
    }

    @GetMapping("/api/logs/user/{page}/{n}")
    public ResponseEntity<ListLogs> retrieveCurrentUserLogs(HttpServletRequest request, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, ParseException, org.json.simple.parser.ParseException {
        Principal principal = request.getUserPrincipal();
        ListLogs listLogs = logRepo.getListUserLogs(principal.getName(), page, n);
        return new ResponseEntity<>(listLogs, HttpStatus.OK);
    }

    @GetMapping("/api/logs/user/date/{date}/{page}/{n}")
    public ResponseEntity<ListLogs> retrieveCurrentUserLogsByDate(HttpServletRequest request, @PathVariable String date, @PathVariable("page") int page, @PathVariable("n") int n) throws IOException, ParseException, org.json.simple.parser.ParseException {
        Principal principal = request.getUserPrincipal();
        return new ResponseEntity<>(logRepo.getListUserLogByDate(date, principal.getName(), page, n), HttpStatus.OK);
    }


    @GetMapping("/api/logs/export")
    public ModelAndView downloadExcel() {

        // return a view which will be resolved by an excel view resolver
        return new ModelAndView(logsExcelView, "listLogs", null);
    }
}
