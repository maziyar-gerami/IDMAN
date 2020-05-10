package parsso.idman.backend.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.backend.Models.OrganizationalUnit;
import parsso.idman.backend.Repos.OusRepo;


import java.util.List;

@RestController
public class FrontController {


    @Autowired
    private OusRepo ousRepo;

    @PostMapping("/add-ou")
    public ResponseEntity<String> bindLdapPerson(@RequestBody OrganizationalUnit ou) {
        String result = ousRepo.create(ou);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PutMapping("/update-ou")

    public ResponseEntity<String> rebindLdapPerson(@RequestBody OrganizationalUnit ou) {
        String result = ousRepo.update(ou);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/retrieve-ous")
    public ResponseEntity<List<OrganizationalUnit>> retrieve() {
        return new ResponseEntity<List<OrganizationalUnit>>(ousRepo.retrieve(), HttpStatus.OK);
    }

    @GetMapping("/retrieve-ou")
    public ResponseEntity <OrganizationalUnit> retrieveOU(@RequestParam(name = "name") String name) {
        return new ResponseEntity<OrganizationalUnit> (ousRepo.retrieveOu(name), HttpStatus.OK);
    }

    @GetMapping("/remove-ou")
    public ResponseEntity<String> unbindLdapOU(@RequestParam(name = "name") String name) {
        return new ResponseEntity<>(ousRepo.remove(name), HttpStatus.OK);
    }


}