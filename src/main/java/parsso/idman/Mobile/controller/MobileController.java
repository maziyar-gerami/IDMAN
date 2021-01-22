package parsso.idman.Mobile.controller;


import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Mobile.RepoImpls.ServicesRepoImpl;
import parsso.idman.Models.ListEvents;
import parsso.idman.Models.Service;
import parsso.idman.Models.User;
import parsso.idman.Repos.EventRepo;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.Repos.UserRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;

@RestController
public class MobileController {

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
        User user = userRepo.retrieveUsers(principal.getName());
        String temp = url + "/api/mobile/sendsms" + "," + user.getUserId() + "," + user.getTokens().getQrToken();

        return servicesRepo.getQRCodeImage(temp, 500, 500);
    }

    @PostMapping("/api/mobile/mobnumber")
    public @ResponseBody
    ResponseEntity<String> mobileNumber(@RequestParam("uid") String uid, @RequestParam("qrToken") String QrToken) {
        User user = userRepo.retrieveUsers(uid);

        if (QrToken.equals(user.getTokens().getQrToken())) {

            return new ResponseEntity<>(user.getMobile(), HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/mobile/sendsms")
    public @ResponseBody
    ResponseEntity<HttpStatus> sendSMS(@RequestParam("uid") String uid, @RequestParam("qrToken") String QrToken) {
        User user = userRepo.retrieveUsers(uid);

        if (QrToken.equals(user.getTokens().getQrToken())) {
            servicesRepo.ActivationSendMessage(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/api/mobile/active")
    public @ResponseBody
    ResponseEntity<String> active(@RequestParam("uid") String uid, @RequestParam("smsCode") String smsCode, @RequestParam("qrToken") String QrToken) {
        User user = userRepo.retrieveUsers(uid);

        if (QrToken.equals(user.getTokens().getQrToken()))
            if (servicesRepo.verifySMS(uid, smsCode).equals(HttpStatus.OK)) {
                return new ResponseEntity<>(user.getTokens().getMobileToken(), HttpStatus.OK);
            }
        return new ResponseEntity<>("BAD", HttpStatus.BAD_REQUEST);
    }


    //after activation

    @PostMapping("/api/mobile/services")
    public @ResponseBody
    ResponseEntity<List<Service>> M_listServices(@RequestParam("mobileToken") String MobileToken, @RequestParam("uid") String uid) throws IOException, org.json.simple.parser.ParseException {
        User user = userRepo.retrieveUsers(uid);
        if (MobileToken.equals(user.getTokens().getMobileToken()))
            return new ResponseEntity<>(serviceRepo.listServicesFull(), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
/*
    @PostMapping("/api/mobile/events")
    public @ResponseBody
    ResponseEntity<List<Event>> M_retrieveAllEvents(@RequestParam("mobileToken") String MobileToken, @RequestParam("uid") String uid) throws IOException, ParseException, org.json.simple.parser.ParseException {
        User user = userRepo.retrieveUser(uid);
        if (MobileToken.equals(user.getTokens().getMobileToken()))
            return new ResponseEntity<>(eventRepo.getMainListEvents(), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

 */

    @PostMapping("/api/mobile/events/{page}/{n}")
    public @ResponseBody
    ResponseEntity<ListEvents> M_retrieveAllEvents(@RequestParam("mobileToken") String MobileToken,
                                                   @PathVariable("page") String page, @PathVariable("n") String n,
                                                   @RequestParam("uid") String uid) throws IOException, ParseException, org.json.simple.parser.ParseException {

        User user = userRepo.retrieveUsers(uid);
        if (MobileToken.equals(user.getTokens().getMobileToken()))
            return new ResponseEntity<>(eventRepo.getListSizeEvents(Integer.valueOf(page), Integer.valueOf(n)), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/api/mobile/user")
    public @ResponseBody
    ResponseEntity<User> M_retrieveUser(@RequestParam("mobileToken") String MobileToken, @RequestParam("uid") String uid) {
        User user = userRepo.retrieveUsers(uid);
        if (MobileToken.equals(user.getTokens().getMobileToken()))
            return new ResponseEntity<>(user, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/api/mobile/photo")
    public @ResponseBody
    ResponseEntity<byte[]> M_retrieveUserPhoto(@RequestParam("mobileToken") String MobileToken, @RequestParam("uid") String uid) {
        User user = userRepo.retrieveUsers(uid);
        if (MobileToken.equals(user.getTokens().getMobileToken()))
            return new ResponseEntity<>(userRepo.showProfilePic(user), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
}