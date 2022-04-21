package parsso.idman.controllers;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import parsso.idman.helpers.Variables;
import parsso.idman.models.response.Response;
import parsso.idman.repos.AuthenticatorRepo;


@RestController
public class AuthenticatorController {
  final AuthenticatorRepo.Retrieve authenticatorRepoRetrieve;
  final AuthenticatorRepo.Delete authenticatorRepoDelete;

  public AuthenticatorController(AuthenticatorRepo.Retrieve authenticatorRepoRetrieve,
      AuthenticatorRepo.Delete authenticatorRepoDelete) {
    this.authenticatorRepoRetrieve = authenticatorRepoRetrieve;
    this.authenticatorRepoDelete = authenticatorRepoDelete;
  }

  @GetMapping("/api/googleAuth")
  public ResponseEntity<Response> retrieveDevices(
      @RequestParam(value = "username", defaultValue = "") String username,
      @RequestParam(value = "deviceName", defaultValue = "") String name,
      @RequestParam(value = "page", defaultValue = "0") String page,
      @RequestParam(value = "count", defaultValue = "0") String count,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(new Response(
        authenticatorRepoRetrieve.retrieve(username, name, Integer.parseInt(page),
            Integer.parseInt(count)),
        Variables.MODEL_USER, HttpStatus.OK.value(), lang), HttpStatus.OK);
  }

  @DeleteMapping("/api/googleAuth")
  public ResponseEntity<Response> deleteBuUsername(HttpServletRequest request,
      @RequestParam(value = "username", defaultValue = "") String username,
      @RequestParam(value = "deviceName", defaultValue = "") String deviceName,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    if (username.equals("") && deviceName.equals("")) {
      return new ResponseEntity<>(
          new Response(null, Variables.MODEL_AUTHENTICATOR, HttpStatus.FORBIDDEN.value(),
              lang),
          HttpStatus.OK);
    } else if (!username.equals("") && !deviceName.equals("")) {
      return new ResponseEntity<>(new Response(
          null, Variables.MODEL_AUTHENTICATOR, authenticatorRepoDelete
              .deleteByUsernameAndDeviceName(username, deviceName,
                  request.getUserPrincipal().getName())
              .value(),
          lang), HttpStatus.OK);
    } else if (!username.equals("")) {
      return new ResponseEntity<>(new Response(
          null, Variables.MODEL_AUTHENTICATOR, authenticatorRepoDelete
              .deleteByUsername(username,
                  request.getUserPrincipal().getName())
              .value(),
          lang), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          new Response(
              null, Variables.MODEL_AUTHENTICATOR, authenticatorRepoDelete
                  .deleteByDeviceName(deviceName,
                      request.getUserPrincipal()
                          .getName())
                  .value(),
              lang),
          HttpStatus.OK);
    }
  }

  @GetMapping("/api/public/googleAuth/registered/{uid}")
  public ResponseEntity<Response> registeredDevices(@PathVariable("uid") String uid,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    Boolean result = authenticatorRepoRetrieve.retrieveUsersDevice(uid);
    if (result) {
      return new ResponseEntity<>(
          new Response(true, Variables.MODEL_AUTHENTICATOR, HttpStatus.OK.value(), lang),
          HttpStatus.OK);
    }

    return new ResponseEntity<>(
        new Response(null, Variables.MODEL_AUTHENTICATOR, HttpStatus.FORBIDDEN.value(), lang),
        HttpStatus.OK);

  }
}
