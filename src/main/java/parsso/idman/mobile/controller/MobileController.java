package parsso.idman.mobile.controller;


import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Models.Event;
import parsso.idman.Models.Service;
import parsso.idman.Models.User;
import parsso.idman.Repos.EventRepo;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.Repos.UserRepo;
import parsso.idman.mobile.RepoImpls.ServicesRepoImpl;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;

@RestController
public class  MobileController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ServicesRepoImpl servicesRepo;
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private ServiceRepo serviceRepo;

    @Value(value = "${base.url}")
    private String url;

    @GetMapping(value = "/api/mobile/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] GetQr(HttpServletRequest request) throws IOException, WriterException {
        Principal principal = request.getUserPrincipal();
        User user = userRepo.retrieveUser(principal.getName());
        String temp = url + "/api/mobile/sendsms" + "," + user.getUserId() + "," + user.getQrToken();

        return servicesRepo.getQRCodeImage(temp, 500, 500);
    }

    @PostMapping("/api/mobile/mobnumber")
    public @ResponseBody
    ResponseEntity<String> mobileNumber(@RequestParam("uid") String uid, @RequestParam("qrToken") String QrToken) {
        User user = userRepo.retrieveUser(uid);

        if (QrToken.equals(user.getQrToken())) {

            return new ResponseEntity<>(user.getMobile(),HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/mobile/sendsms")
    public @ResponseBody
    ResponseEntity<HttpStatus> sendSMS(@RequestParam("uid") String uid, @RequestParam("qrToken") String QrToken) {
        User user = userRepo.retrieveUser(uid);

        if (QrToken.equals(user.getQrToken())) {
            servicesRepo.ActivationSendMessage(user.getMobile());
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/api/mobile/active")
    public @ResponseBody
    ResponseEntity<String> active(@RequestParam("uid") String uid, @RequestParam("smsCode") String smsCode, @RequestParam("qrToken") String QrToken) {
        User user = userRepo.retrieveUser(uid);

        if (QrToken.equals(user.getQrToken()))
            if (servicesRepo.verifySMS(uid, smsCode).equals(HttpStatus.OK)) {
                return new ResponseEntity<>(user.getMobileToken(), HttpStatus.OK);
            }
        return new ResponseEntity<>("BAD", HttpStatus.BAD_REQUEST);
    }


    //after activation

    @PostMapping("/api/mobile/services")
    public @ResponseBody
    ResponseEntity<List<Service>> M_listServices(@RequestParam("mobileToken") String MobileToken, @RequestParam("uid") String uid) throws IOException, org.json.simple.parser.ParseException {
        User user = userRepo.retrieveUser(uid);
        if (MobileToken.equals(user.getMobileToken()))
            return new ResponseEntity<>(serviceRepo.listServices(), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/api/mobile/events")
    public @ResponseBody
    ResponseEntity<List<Event>> M_retrieveAllEvents(@RequestParam("mobileToken") String MobileToken, @RequestParam("uid") String uid) throws FileNotFoundException, ParseException {
        User user = userRepo.retrieveUser(uid);
        if (MobileToken.equals(user.getMobileToken()))
            return new ResponseEntity<>(eventRepo.getListUserEvents(), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/api/mobile/user")
    public @ResponseBody
    ResponseEntity<User> M_retrieveUser(@RequestParam("mobileToken") String MobileToken, @RequestParam("uid") String uid) {
        User user = userRepo.retrieveUser(uid);
        if (MobileToken.equals(user.getMobileToken()))
            return new ResponseEntity<>(user, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
}