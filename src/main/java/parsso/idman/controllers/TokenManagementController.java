package parsso.idman.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.helpers.Variables;
import parsso.idman.models.response.Response;
import parsso.idman.repos.TokenManagementRepo;

@RestController
@RequestMapping("/api/public/tokenManagement")
public class TokenManagementController {

    final TokenManagementRepo tokenManagement;

    @Autowired
    TokenManagementController(TokenManagementRepo tokenManagementRepo) {
        this.tokenManagement = tokenManagementRepo;
    }

    @GetMapping
    ResponseEntity<String> retrieve(@RequestParam("username") String userId) {
        return new ResponseEntity<>(tokenManagement.retrieve(userId), HttpStatus.OK);
    }


    @DeleteMapping()
    ResponseEntity<HttpStatus> deleteAll(@RequestParam(value = "username") String userId, @RequestParam(name = "token", defaultValue = "") String token) {
        if (token.equals(""))
            return new ResponseEntity<>(tokenManagement.delete(userId));
        return new ResponseEntity<>(tokenManagement.delete(userId, token));
    }


    @PostMapping
    ResponseEntity<HttpStatus> create(@RequestParam("username") String userId, @RequestParam("token") String token) {
        return new ResponseEntity<>(tokenManagement.create(userId, token));
    }

    @GetMapping("/valid")
    ResponseEntity<Response> retrieve(@RequestParam("username") String userId, @RequestParam("token") String token, @RequestParam(value = "lang", defaultValue = Variables.DEFAULT_LANG) String lang) throws NoSuchFieldException, IllegalAccessException {
        return new ResponseEntity<>(new Response(tokenManagement.valid(userId, token),Variables.MODEL_TOKEN,HttpStatus.OK.value(), lang), HttpStatus.OK);
    }

}
