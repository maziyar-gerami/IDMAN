package parsso.idman.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.helpers.Variables;
import parsso.idman.models.response.Response;
import parsso.idman.repos.TokenManagementRepo;

@RestController
@RequestMapping("/api/public/tokenManagment")
public class TokenManagementController {
    TokenManagementRepo tokenManagement;
    String model = Variables.MODEL_TOKEN;
    @GetMapping
    ResponseEntity<Response> retrieve(@RequestParam("username") String userId,@RequestParam(value = "lang",defaultValue = "fa") String lang){
        return new ResponseEntity<>(new Response(HttpStatus.OK,tokenManagement.retrieve(userId),lang), HttpStatus.OK);
    }

    /*
    @DeleteMapping()
    ResponseEntity<Response> deleteAll(@RequestParam(value = "username") String userId,@RequestParam(value = "lang",defaultValue = "fa") String lang){
        return new ResponseEntity<>(new Response(tokenManagement.delete(userId),model,lang),HttpStatus.OK);
    }

     */

    @DeleteMapping
    ResponseEntity<Response> delete(@RequestParam(value = "username") String userId, @RequestParam("token") String token,@RequestParam(value = "lang",defaultValue = "fa") String lang){
        return new ResponseEntity<>(new Response(tokenManagement.delete(userId,token),model,lang),HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<Response> create(@RequestParam("username") String userId, @RequestParam("token") String token,@RequestParam(value = "lang",defaultValue = "fa") String lang){
        return new ResponseEntity<>(new Response(tokenManagement.create(userId,token),model,lang),HttpStatus.OK);

    }
}
