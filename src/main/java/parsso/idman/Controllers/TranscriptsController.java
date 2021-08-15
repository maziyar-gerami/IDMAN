package parsso.idman.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import parsso.idman.Repos.TranscriptRepo;

@Controller
public class TranscriptsController {
    @Autowired
    TranscriptRepo transcriptRepo;


}
