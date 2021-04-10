package parsso.idman.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.PublicMessage;
import parsso.idman.Repos.PubMessageRepo;

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
    public ResponseEntity<HttpStatus> postPublicMessage(@RequestBody String message) {
        return new ResponseEntity<>(pubMessageRepo.postPubicMessage("maziyar", message));
    }

    @PutMapping("/api/users/publicMessage")
    public ResponseEntity<HttpStatus> editPublicMessage(@RequestBody String message) {
        return new ResponseEntity<>(pubMessageRepo.editPubicMessage("maziyar", message));
    }

    @DeleteMapping("/api/users/publicMessage")
    public ResponseEntity<HttpStatus> deletePublicMessage(@RequestParam("id") String id) {
        return new ResponseEntity<>(pubMessageRepo.deletePubicMessage("maziyar",id));
    }

}
