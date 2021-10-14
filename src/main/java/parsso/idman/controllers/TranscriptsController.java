package parsso.idman.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Repos.logs.transcripts.TranscriptRepo;

import java.text.ParseException;
import java.util.List;

@Controller
public class TranscriptsController {
	private final TranscriptRepo transcriptRepo;

	public TranscriptsController(TranscriptRepo transcriptRepo) {
		this.transcriptRepo = transcriptRepo;
	}

	//************************************* Pages ****************************************

	@SuppressWarnings("SameReturnValue")
	@GetMapping("/transcripts")
	public String getPageTranscripts() {
		return "transcripts";
	}


	//************************************* APIs ****************************************


	@GetMapping("/api/transcripts/access/services")
	public ResponseEntity<List<ReportMessage>> accessManaging(@RequestParam(value = "page",defaultValue = "") String p ,
	                                                          @RequestParam(value = "n",defaultValue = "") String n,
	                                                          @RequestParam(value = "sName" ,defaultValue = "") String instanceName,
	                                                          @RequestParam(value = "sId",defaultValue = "") String id,
	                                                          @RequestParam(value = "date",defaultValue = "") String date,
	                                                          @RequestParam(value = "doerId",defaultValue = "") String doerId) throws ParseException {
		long _id = !id.equals("")? Long.parseLong(id):0;
		int page = !p.equals("")?Integer.parseInt(p):0;
		int nRows = !n.equals("")? Integer.parseInt(n): 0 ;

		return new ResponseEntity<>(transcriptRepo.accessManaging(page,nRows,_id,date,doerId, instanceName), HttpStatus.OK);
	}

}
