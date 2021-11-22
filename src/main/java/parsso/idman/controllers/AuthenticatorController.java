package parsso.idman.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.models.other.Devices;
import parsso.idman.repos.AuthenticatorRepo;

import java.util.LinkedList;

@RestController
@RequestMapping("/api/authenticator")
public class AuthenticatorController {
    AuthenticatorRepo authenticatorRepo;

    @Autowired
    public AuthenticatorController(AuthenticatorRepo authenticatorRepo){
        this.authenticatorRepo = authenticatorRepo;
    }
    @GetMapping
    public ResponseEntity<LinkedList<Devices>> retrieveDevices(@RequestParam(value = "username",defaultValue ="") String userId) {
        return new ResponseEntity<>(authenticatorRepo.retrieve(userId), HttpStatus.OK);

    }
    @DeleteMapping("/username")
    public ResponseEntity<HttpStatus> deleteBuUsername(@RequestParam(value = "username") String username){
        return new ResponseEntity<>(authenticatorRepo.deleteByUsername(username));
    }

    @DeleteMapping("/deviceName")
    public ResponseEntity<HttpStatus> deleteByDeviceName(@RequestParam(value = "deviceName") String deviceName){
        return new ResponseEntity<>(authenticatorRepo.deleteByDeviceName(deviceName));
    }

}
