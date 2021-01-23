package parsso.idman.Mobile.controller;


import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Configs.WebSecurityConfig;
import parsso.idman.Mobile.RepoImpls.JwtUtil;
import parsso.idman.Mobile.RepoImpls.ServicesRepoImpl;
import parsso.idman.Models.User;
import parsso.idman.Repos.UserRepo;

import java.io.IOException;

@RestController
public class LoginwithQR {

    private static String random = null;
    @Autowired
    WebSecurityConfig webSecurityConfig;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ServicesRepoImpl servicesRepo;
    @Autowired
    private SimpMessagingTemplate webSocket;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;


    @GetMapping(value = "/api/mobile/qrlogin", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] GetQr() throws IOException, WriterException {

        random = servicesRepo.randomString(42);
        System.out.println(random);
        return servicesRepo.getQRCodeImage(random, 500, 500);
    }

    @PostMapping(value = "/api/mobile/login")
    public HttpStatus creatAuthenticationToken(@RequestParam("qrToken") String qrToken, @RequestParam("uid") String uid, @RequestParam("mobileToken") String mobileToken) throws Exception {
        try {
            User user = userRepo.retrieveUsers(uid);
            if (user.getUsersExtraInfo().getMobileToken().equals(mobileToken) && qrToken.equals(random)) {
                authenticationManager.authenticate
                        (new UsernamePasswordAuthenticationToken(uid, mobileToken));


                String jwt = jwtUtil.generateToken(user);

                webSocket.convertAndSend("/room.2", jwt);

                return HttpStatus.OK;
            } else
                return HttpStatus.BAD_REQUEST;
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect user and pass", e);
        }

    }


}
