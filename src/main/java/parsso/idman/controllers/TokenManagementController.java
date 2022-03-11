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
    ResponseEntity<Response> retrieve(@RequestParam("username") String userId, @RequestParam(value = "lang", defaultValue = "fa") String lang) throws NoSuchFieldException, IllegalAccessException {
        return new ResponseEntity<>(new Response(tokenManagement.retrieve(userId),Variables.MODEL_TOKEN,HttpStatus.OK.value(),lang),HttpStatus.OK);
    }


    @DeleteMapping()
    ResponseEntity<Response> deleteAll(@RequestParam(value = "username") String userId, @RequestParam(name = "token", defaultValue = "") String token, @RequestParam(value = "lang", defaultValue = "fa") String lang) throws NoSuchFieldException, IllegalAccessException {

        return new ResponseEntity<>(new Response(null,Variables.MODEL_TOKEN, tokenManagement.delete(userId, token).value(),lang),HttpStatus.OK);

    }


    @PostMapping
    ResponseEntity<Response> create(@RequestParam("username") String userId, @RequestParam("token") String token, @RequestParam(value = "lang", defaultValue = "fa") String lang) throws NoSuchFieldException, IllegalAccessException {
        return new ResponseEntity<>(new Response(null,Variables.MODEL_TOKEN, tokenManagement.create(userId, token).value(),lang), HttpStatus.OK);
    }

    @GetMapping("/valid")
    ResponseEntity<Response> retrieve(@RequestParam("username") String userId, @RequestParam("token") String token, @RequestParam(value = "lang", defaultValue = "fa") String lang) throws NoSuchFieldException, IllegalAccessException {
        return new ResponseEntity<>(new Response(tokenManagement.valid(userId, token),Variables.MODEL_TOKEN,HttpStatus.OK.value(), lang), HttpStatus.OK);
    }

}
