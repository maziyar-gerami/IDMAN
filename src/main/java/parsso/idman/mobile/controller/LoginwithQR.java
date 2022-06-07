package parsso.idman.mobile.controller;

import com.google.zxing.WriterException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import parsso.idman.configs.WebSecurityConfig;
import parsso.idman.mobile.impls.JwtUtil;
import parsso.idman.mobile.impls.ServicesRepoImpl;
import parsso.idman.models.users.User;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
public class LoginwithQR {
  private static String random = null;
  @Autowired
  WebSecurityConfig webSecurityConfig;
  @Autowired
  private UsersRetrieveRepo usersOpRetrieve;
  @Autowired
  private ServicesRepoImpl servicesRepo;
  @Autowired
  private SimpMessagingTemplate webSocket;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private JwtUtil jwtUtil;

  @GetMapping(value = "/api/mobile/qrlogin", produces = MediaType.IMAGE_PNG_VALUE)
  public @ResponseBody byte[] GetQr() throws IOException, WriterException {

    random = servicesRepo.randomString(42);
    return servicesRepo.getQRCodeImage(random, 500, 500);
  }

  @PostMapping(value = "/api/mobile/login")
  public HttpStatus creatAuthenticationToken(@RequestParam("qrToken") String qrToken, @RequestParam("uid") String uid,
      @RequestParam("mobileToken") String mobileToken) throws Exception {
    try {
      User user = usersOpRetrieve.retrieveUsers(uid);
      if (user.getUsersExtraInfo().getMobileToken().equals(mobileToken) && qrToken.equals(random)) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(uid, mobileToken));

        String jwt = jwtUtil.generateToken(user);

        webSocket.convertAndSend("/room.2", jwt);

        return HttpStatus.OK;
      } else {
        return HttpStatus.BAD_REQUEST;
      }
    } catch (BadCredentialsException e) {
      throw new Exception("Incorrect user and pass", e);
    }

  }

}
