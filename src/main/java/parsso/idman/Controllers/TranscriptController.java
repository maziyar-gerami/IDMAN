package parsso.idman.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import parsso.idman.Models.License.License;
import parsso.idman.Models.Logs.Transcript;
import parsso.idman.Repos.TranscriptRepo;

import java.io.IOException;

@Controller
public class TranscriptController {
    @Autowired
    private TranscriptRepo transcriptRepo;

    //************************************* APIs ****************************************


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

    @GetMapping("/api/transcripts/access/services/{id}")
    public ResponseEntity<Object> accessManaging(@PathVariable("id") String id,
                                                 @RequestParam( name = "type", defaultValue = "") String type,
                                                 @RequestParam( name = "item", defaultValue = "") String item)  {
        return new ResponseEntity<>(transcriptRepo.accessManaging(Long.valueOf(id), type,item), HttpStatus.OK);
    }

}
