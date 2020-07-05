package parsso.idman.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.Person;
import parsso.idman.Repos.PersonRepo;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RestController
public class PublicController {

    @Qualifier("personRepoImpl")
    @Autowired
    private PersonRepo personRepo;



    @GetMapping("/api/public/sendMail/{email}")
    public ResponseEntity<String> sendMail(@PathVariable("email") String email) {
        return new ResponseEntity<String> (personRepo.sendEmail(email), HttpStatus.OK);
    }

    @GetMapping("/api/public/checkMail/{email}")
    public ResponseEntity <List<Person>> checkMail (@PathVariable("email") String email) {
        return new ResponseEntity<List<Person>> (personRepo.checkMail(email), HttpStatus.OK);
    }

    @PutMapping("/api/public/resetPass/{uid}/{token}")
    public ResponseEntity<String> rebindLdapPerson
            (
             @RequestParam("newPassword") String newPassword,

             @PathVariable("token") String token, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String result = personRepo.updatePass(principal.getName(),newPassword,token);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/api/public/getName/{uid}")
        public ResponseEntity<Person> getName
        (@PathVariable("uid") String uid) {
            return new ResponseEntity<>(personRepo.getName(uid), HttpStatus.OK);
    }

    @GetMapping("/api/public/sendMail/{email}")
    public ResponseEntity<String> sendMail(@PathVariable("email") String email, @PathVariable("uid") String uid) {
        return new ResponseEntity<>(personRepo.sendEmail(email, uid), HttpStatus.OK);
    }

    @GetMapping ("/api/public/validateToken/{uId}/{token}")
    public String resetPass(@PathVariable("uId") String uId, @PathVariable("token") String token)
    {
        Person person = personRepo.checkToken(uId,token);

        if (person != null)
            return "resetPassword";
        else
            return "resetPassWrongTAI";
    }
}
