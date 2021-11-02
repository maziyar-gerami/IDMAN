package parsso.idman.controllers;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.models.other.PublicMessage;
import parsso.idman.repos.PubMessageRepo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
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
    public ResponseEntity<HttpStatus> postPublicMessage(HttpServletRequest request, @RequestBody PublicMessage message) {
        return new ResponseEntity<>(pubMessageRepo.postPubicMessage(request.getUserPrincipal().getName(), message));
    }

    @PutMapping("/api/users/publicMessage")
    public ResponseEntity<HttpStatus> editPublicMessage(HttpServletRequest request, @RequestBody PublicMessage message) {
        return new ResponseEntity<>(pubMessageRepo.editPubicMessage(request.getUserPrincipal().getName(), message));
    }

    @DeleteMapping("/api/users/publicMessages")
    public ResponseEntity<HttpStatus> deletePublicMessage(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        return new ResponseEntity<>(pubMessageRepo.deletePubicMessage(request.getUserPrincipal().getName(), jsonObject));
    }
}
