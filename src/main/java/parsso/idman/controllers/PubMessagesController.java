package parsso.idman.controllers;

import javax.servlet.http.HttpServletRequest;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import parsso.idman.helpers.Variables;
import parsso.idman.models.other.PublicMessage;
import parsso.idman.models.response.Response;
import parsso.idman.repos.PubMessageRepo;


@RestController
public class PubMessagesController {
  final PubMessageRepo pubMessageRepo;

  public PubMessagesController(PubMessageRepo pubMessageRepo) {
    this.pubMessageRepo = pubMessageRepo;
  }

  @GetMapping("/api/public/publicMessages")
  public ResponseEntity<Response> getVisiblePublicMessage(
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(new Response(pubMessageRepo.showVisiblePubicMessages(),
        Variables.MODEL_PUBICMESSAGE, HttpStatus.OK.value(), lang), HttpStatus.OK);
  }

  @GetMapping("/api/users/publicMessages")
  public ResponseEntity<Response> getAllPublicMessage(
      @RequestParam(name = "id", defaultValue = "") String id,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(new Response(
          pubMessageRepo.showAllPubicMessages(id), Variables.MODEL_PUBICMESSAGE,
        HttpStatus.OK.value(), lang), HttpStatus.OK);
  }

  @PostMapping("/api/users/publicMessage")
  public ResponseEntity<Response> postPublicMessage(
      HttpServletRequest request, @RequestBody PublicMessage message,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(
        new Response(null, Variables.MODEL_PUBICMESSAGE, pubMessageRepo.postPubicMessage(
          request.getUserPrincipal().getName(), message).value(), lang),
        HttpStatus.OK);
  }

  @PutMapping("/api/users/publicMessage")
  public ResponseEntity<Response> editPublicMessage(
        HttpServletRequest request, @RequestBody PublicMessage message,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(
        new Response(null, Variables.MODEL_PUBICMESSAGE,
            pubMessageRepo.editPubicMessage(
                  request.getUserPrincipal().getName(), message).value(), lang),
        HttpStatus.OK);
  }

  @DeleteMapping("/api/users/publicMessages")
  public ResponseEntity<Response> deletePublicMessage(
        HttpServletRequest request, @RequestBody JSONObject jsonObject,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(new Response(null, Variables.MODEL_PUBICMESSAGE,
        pubMessageRepo.deletePubicMessage(
            request.getUserPrincipal().getName(), jsonObject).value(), lang),
        HttpStatus.OK);
  }
}
