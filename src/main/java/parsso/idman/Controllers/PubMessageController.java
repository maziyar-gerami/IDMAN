package parsso.idman.Controllers;


import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.PublicMessage;
import parsso.idman.Repos.PubMessageRepo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class PubMessageController {


    /**
     * The Storage service.
     */


    @Autowired
    PubMessageRepo pubMessageRepo;



    //*************************************** APIs ***************************************

    @GetMapping("/api/public/publicMessages")
    public ResponseEntity<List<PublicMessage>> getPublicMessage(@RequestParam (name="id", defaultValue = "") String id) {
        return new ResponseEntity<>(pubMessageRepo.showPubicMessages(id), HttpStatus.OK);
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
        return new ResponseEntity<>(pubMessageRepo.deletePubicMessage(request.getUserPrincipal().getName(),jsonObject));
    }
}
