package parsso.idman.Controllers;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.User;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RestController
public class PublicController {

    @Qualifier("userRepoImpl")
    @Autowired
    private UserRepo userRepo;


    @GetMapping("/api/public/sendMail/{email}")
    public ResponseEntity<String> sendMail(@PathVariable("email") String email) {
        return new ResponseEntity<String>(userRepo.sendEmail(email), HttpStatus.OK);
    }

    @GetMapping("/api/public/sendSMS/{mobile}")
    public ResponseEntity<String> sendMessage(@PathVariable("mobile") String mobile) {
        return new ResponseEntity<>(userRepo.sendMessage(mobile), HttpStatus.OK);
    }

    @GetMapping("/api/public/checkMail/{email}")
    public HttpEntity<List<JSONObject>> checkMail(@PathVariable("email") String email) {
        return new ResponseEntity<List<JSONObject>>(userRepo.checkMail(email), HttpStatus.OK);
    }

    @GetMapping("/api/public/checkMobile/{mobile}")
    public HttpEntity<List<JSONObject>> checkMobile(@PathVariable("mobile") String mobile) {
        return new ResponseEntity<List<JSONObject>>(userRepo.checkMobile(mobile), HttpStatus.OK);
    }

    @PutMapping("/api/public/resetPass/{uid}/{token}")
    public ResponseEntity<String> rebindLdapUser
            (
                    @RequestParam("newPassword") String newPassword,

                    @PathVariable("token") String token, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String result = userRepo.updatePass(principal.getName(), newPassword, token);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/api/public/getName/{uid}")
    public ResponseEntity<User> getName
            (@PathVariable("uid") String uid) {
        return new ResponseEntity<>(userRepo.getName(uid), HttpStatus.OK);
    }

    @GetMapping("/api/public/sendMail/{email}/{uid}")
    public ResponseEntity<String> sendMail(@PathVariable("email") String email, @PathVariable("uid") String uid) {
        return new ResponseEntity<>(userRepo.sendEmail(email, uid), HttpStatus.OK);
    }

    @GetMapping("/api/public/sendSMS/{mobile}/{uid}")
    public ResponseEntity<String> sendMessage(@PathVariable("mobile") String mobile, @PathVariable("uid") String uid) {
        return new ResponseEntity<>(userRepo.sendMessage(mobile, uid), HttpStatus.OK);
    }

    @GetMapping("/api/public/validateToken/{uId}/{token}")
    public ResponseEntity<HttpStatus> resetPass(@PathVariable("uId") String uId, @PathVariable("token") String token) {
        return new ResponseEntity<HttpStatus>(userRepo.checkToken(uId, token),userRepo.checkToken(uId,token));
    }
}
