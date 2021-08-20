package parsso.idman.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import parsso.idman.Models.License.License;
import parsso.idman.Models.Logs.Transcript;
import parsso.idman.Repos.TranscriptRepo;

import java.io.IOException;

@Controller
public class TranscriptController {


    @Autowired
    private TranscriptRepo transcriptRepo;

    //*************************************** Pages ***************************************

    //*************************************** APIs ***************************************


    @GetMapping("/api/transcripts/users/service/{id}")
    public ResponseEntity<Transcript> retrieveUsersOfServices(@PathVariable("id") String id) throws IOException, org.json.simple.parser.ParseException {
        return new ResponseEntity<>(transcriptRepo.usersAndGroupsOfService(Long.valueOf(id)), HttpStatus.OK);
    }

    @GetMapping("/api/transcripts/services/group/{id}")
    public ResponseEntity<License> retrieveGroupsService(@PathVariable("id") String id) throws IOException, org.json.simple.parser.ParseException {
        return new ResponseEntity<>(transcriptRepo.servicesOfGroup(id), HttpStatus.OK);
    }

    @GetMapping("/api/transcripts/services/user/{id}")
    public ResponseEntity<License> retrieveUsersServices(@PathVariable("id") String id) throws IOException, org.json.simple.parser.ParseException {
        return new ResponseEntity<>(transcriptRepo.servicesOfUser(id), HttpStatus.OK);
    }

    //@GetMapping("/api/audits/users/export")
    public ModelAndView downloadExcel() {
        // return a view which will be resolved by an excel view resolver
        //return new ModelAndView(auditsExcelView, "listAudits", null);
        return null;
    }

}
