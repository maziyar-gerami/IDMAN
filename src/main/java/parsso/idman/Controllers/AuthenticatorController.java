package parsso.idman.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.models.other.Devices;
import parsso.idman.repos.AuthenticatorRepo;


@RestController
@RequestMapping("/api/googleAuth")
public class AuthenticatorController {
    AuthenticatorRepo authenticatorRepo;

    @Autowired
    public AuthenticatorController(AuthenticatorRepo authenticatorRepo){
        this.authenticatorRepo = authenticatorRepo;
    }
    @GetMapping
    public ResponseEntity<Devices.DeviceList> retrieveDevices(@RequestParam(value = "username",defaultValue ="") String username,
                                                         @RequestParam(value = "deviceName",defaultValue ="") String name,
                                                         @RequestParam(value = "page",defaultValue = "0") String page,
                                                         @RequestParam(value = "count",defaultValue = "0") String count) {
        return new ResponseEntity<>(authenticatorRepo.retrieve(username,name,Integer.parseInt(page),Integer.parseInt(count)), HttpStatus.OK);

    }
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteBuUsername(@RequestParam(value = "username",defaultValue = "") String username,
                                                       @RequestParam(value = "deviceName",defaultValue = "") String deviceName) {
        if (username.equals("")&& deviceName.equals(""))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        else if (!username.equals("") && !deviceName.equals(""))
            return new ResponseEntity<>(authenticatorRepo.deleteByUsernameAndDeviceName(username, deviceName));
        else if (!username.equals(""))
            return new ResponseEntity<>(authenticatorRepo.deleteByUsername(username));
        else
            return new ResponseEntity<>(authenticatorRepo.deleteByDeviceName(username));

    }

}
