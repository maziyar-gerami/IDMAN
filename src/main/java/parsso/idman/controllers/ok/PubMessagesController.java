package parsso.idman.controllers.ok;


import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.other.PublicMessage;
import parsso.idman.Repos.PubMessageRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
public class PubMessagesController {
	final
	PubMessageRepo pubMessageRepo;

	public PubMessagesController(PubMessageRepo pubMessageRepo) {
		this.pubMessageRepo = pubMessageRepo;
	}

	@GetMapping("/api/public/publicMessages")
	public ResponseEntity<List<PublicMessage>> getVisiblePublicMessage() {
		return new ResponseEntity<>(pubMessageRepo.showVisiblePubicMessages(), HttpStatus.OK);
	}

	@GetMapping("/api/users/publicMessages")
	public ResponseEntity<List<PublicMessage>> getAllPublicMessage(@RequestParam(name = "id", defaultValue = "") String id) {
		return new ResponseEntity<>(pubMessageRepo.showAllPubicMessages(id), HttpStatus.OK);
	}

	@PostMapping("/api/users/publicMessage")
	public ResponseEntity<HttpStatus> postPublicMessage(HttpServletRequest request, @RequestBody PublicMessage message) throws IOException, ParseException {
		return new ResponseEntity<>(pubMessageRepo.postPubicMessage(request.getUserPrincipal().getName(), message));
	}

	@PutMapping("/api/users/publicMessage")
	public ResponseEntity<HttpStatus> editPublicMessage(HttpServletRequest request, @RequestBody PublicMessage message) throws IOException, ParseException {
		return new ResponseEntity<>(pubMessageRepo.editPubicMessage(request.getUserPrincipal().getName(), message));
	}

	@DeleteMapping("/api/users/publicMessages")
	public ResponseEntity<HttpStatus> deletePublicMessage(HttpServletRequest request, @RequestBody JSONObject jsonObject) throws IOException, ParseException {
		return new ResponseEntity<>(pubMessageRepo.deletePubicMessage(request.getUserPrincipal().getName(), jsonObject));
	}
}
