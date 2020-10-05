package parsso.idman.mobile.controller;

import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Configs.WebSecurityConfig;
import parsso.idman.Models.User;
import parsso.idman.Repos.UserRepo;
import parsso.idman.mobile.RepoImpls.JwtUtil;
import parsso.idman.mobile.RepoImpls.ServicesRepoImpl;
import parsso.idman.mobile.RepoImpls.Socket;

import java.io.IOException;

@RestController
public class LoginwithQR {

    private static String random = null;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ServicesRepoImpl servicesRepo;
    @Autowired
    WebSecurityConfig webSecurityConfig;
    @Autowired
    private Socket socket;
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
    public void creatAuthenticationToken( @RequestParam("uid") String uid, @RequestParam("mobileToken") String mobileToken) throws Exception {
        try {
            User user = userRepo.retrieveUser(uid);
            if (user.getMobileToken().equals(mobileToken) ) {
                authenticationManager.authenticate
                        (new UsernamePasswordAuthenticationToken(uid, mobileToken));
            }
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect user and pass", e);
        }
        final User user = userRepo.retrieveUser(uid);

        String jwt = jwtUtil.generateToken(user);
        socket.broadcast(jwt);
    }


}
