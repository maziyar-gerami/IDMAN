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

<<<<<<< HEAD
    @Autowired
    private OusRepo ousRepo;

    @PostMapping("/api/groups")
=======

    @Autowired
    private OusRepo ousRepo;

    @PostMapping("/add-ou")
>>>>>>> a0d0f3a5dec3208e6b55845dd55e395a072dce44
    public ResponseEntity<String> bindLdapPerson(@RequestBody OrganizationalUnit ou) {
        String result = ousRepo.create(ou);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
<<<<<<< HEAD

    @PutMapping("/api/groups/{name}")
    public ResponseEntity<String> rebindLdapPerson(@RequestBody OrganizationalUnit ou, @PathVariable("name") String name) {
        String result = ousRepo.update(name, ou);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/api/groups")
=======
    @PutMapping("/update-ou")

    public ResponseEntity<String> rebindLdapPerson(@RequestBody OrganizationalUnit ou) {
        String result = ousRepo.update(ou);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/retrieve-ous")
>>>>>>> a0d0f3a5dec3208e6b55845dd55e395a072dce44
    public ResponseEntity<List<OrganizationalUnit>> retrieve() {
        return new ResponseEntity<List<OrganizationalUnit>>(ousRepo.retrieve(), HttpStatus.OK);
    }

<<<<<<< HEAD
    @GetMapping("/api/groups/{name}")
    public ResponseEntity <OrganizationalUnit> retrieveOU(@PathVariable("id") String name) {
        return new ResponseEntity<OrganizationalUnit> (ousRepo.retrieveOu(name), HttpStatus.OK);
    }

    @DeleteMapping("/api/groups/{name}")
    public ResponseEntity<String> unbindLdapOU(@PathVariable("name") String name) {
        return new ResponseEntity<>(ousRepo.remove(name), HttpStatus.OK);
    }

    @DeleteMapping("/api/groups")
    public ResponseEntity<String> unbindAllLdapOU() {
        return new ResponseEntity<>(ousRepo.remove(), HttpStatus.OK);
    }
=======
    @GetMapping("/retrieve-ou")
    public ResponseEntity <OrganizationalUnit> retrieveOU(@RequestParam(name = "name") String name) {
        return new ResponseEntity<OrganizationalUnit> (ousRepo.retrieveOu(name), HttpStatus.OK);
    }

    @GetMapping("/remove-ou")
    public ResponseEntity<String> unbindLdapOU(@RequestParam(name = "name") String name) {
        return new ResponseEntity<>(ousRepo.remove(name), HttpStatus.OK);
    }

>>>>>>> a0d0f3a5dec3208e6b55845dd55e395a072dce44

}