package parsso.idman.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Devices;
import parsso.idman.models.response.Response;
import parsso.idman.repos.AuthenticatorRepo;


@RestController
public class AuthenticatorController {
    final AuthenticatorRepo authenticatorRepo;

    @Autowired
    public AuthenticatorController(AuthenticatorRepo authenticatorRepo) {
        this.authenticatorRepo = authenticatorRepo;
    }

    @GetMapping("/api/googleAuth")
    public ResponseEntity<Response> retrieveDevices(@RequestParam(value = "username", defaultValue = "") String username,
                                                              @RequestParam(value = "deviceName", defaultValue = "") String name,
                                                              @RequestParam(value = "page", defaultValue = "0") String page,
                                                              @RequestParam(value = "count", defaultValue = "0") String count,
                                                              @RequestParam(value = "lang", defaultValue = "fa") String lang) {
        Devices.DeviceList deviceList = authenticatorRepo.retrieve(username, name, Integer.parseInt(page),Integer.parseInt(count));
        return new ResponseEntity<>(new Response(deviceList,lang), HttpStatus.OK);

    }

    @DeleteMapping("/api/googleAuth")
    public ResponseEntity<Response> deleteBuUsername(@RequestParam(value = "username", defaultValue = "") String username,
                                                       @RequestParam(value = "deviceName", defaultValue = "") String deviceName,
                                                       @RequestParam(value = "lang", defaultValue = "fa") String lang) throws NoSuchFieldException, IllegalAccessException {
        if (username.equals("") && deviceName.equals(""))
            return new ResponseEntity<>(new Response(lang, Variables.MODEL_AUTHENTICATOR,HttpStatus.FORBIDDEN.value()),HttpStatus.OK);

        else if (!username.equals("") && !deviceName.equals(""))
            return new ResponseEntity<>(new Response(lang,Variables.MODEL_AUTHENTICATOR,authenticatorRepo
                    .deleteByUsernameAndDeviceName(username, deviceName).value()),HttpStatus.OK);
        else if (!username.equals(""))
            return new ResponseEntity<>(new Response(lang,Variables.MODEL_AUTHENTICATOR,authenticatorRepo
                    .deleteByUsername(username).value()),HttpStatus.OK);
        else
            return new ResponseEntity<>(new Response(lang,Variables.MODEL_AUTHENTICATOR,authenticatorRepo.
                    deleteByDeviceName(deviceName).value()),HttpStatus.OK);

    }

    @GetMapping("/api/public/googleAuth/registered/{uid}")
    public ResponseEntity<Response> registeredDevices(@PathVariable("uid") String uid,
                                                     @RequestParam(value = "lang",defaultValue = "fa") String lang) throws NoSuchFieldException, IllegalAccessException {
        Boolean result = authenticatorRepo.retrieveUsersDevice(uid);
        if(result)
            return new ResponseEntity<>(new Response(true,Variables.MODEL_AUTHENTICATOR,lang), HttpStatus.OK);

        return new ResponseEntity<>(new Response(lang,Variables.MODEL_AUTHENTICATOR,403), HttpStatus.OK);


    }

}
