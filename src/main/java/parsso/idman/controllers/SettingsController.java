package parsso.idman.controllers;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import parsso.idman.configs.Prefs;
import parsso.idman.helpers.Settings;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Property;
import parsso.idman.models.other.Setting;
import parsso.idman.models.response.Response;
import parsso.idman.repos.SettingsRepo;

@RestController
@RequestMapping(("/api/properties/settings"))
public class SettingsController {
  final SettingsRepo settingsRepo;
  final MongoTemplate mongoTemplate;

  @Autowired
  SettingsController(SettingsRepo settingsRepo, MongoTemplate mongoTemplate) {
    this.settingsRepo = settingsRepo;
    this.mongoTemplate = mongoTemplate;
  }

  @GetMapping
  public ResponseEntity<Response> retrieveSettings(@RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    List<Setting> settings = settingsRepo.retrieve();
    List<Property> properties = new Property().settingsToProperty(settings, lang);
    return new ResponseEntity<>(new Response(properties, Variables.MODEL_SETTINGS, HttpStatus.OK.value(), lang),
        HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<Response> update(HttpServletRequest request, @RequestBody List<Property> properties,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    int status = settingsRepo.update(request.getUserPrincipal().getName(), properties).value();
    if (status == 200) {
      new Prefs(settingsRepo.retrieve());
      return new ResponseEntity<>(new Response(null, Variables.MODEL_SETTINGS, HttpStatus.OK.value(), lang),
          HttpStatus.OK);
    }
    return new ResponseEntity<>(new Response(null, Variables.MODEL_SETTINGS, HttpStatus.BAD_REQUEST.value(), lang),
        HttpStatus.OK);
  }

  @GetMapping("/reset")
  public ResponseEntity<Response> reset(HttpServletRequest request,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException, IOException {
    return new ResponseEntity<>(new Response(null, Variables.MODEL_SETTINGS,
        settingsRepo.reset(request.getUserPrincipal().getName()).value(), lang), HttpStatus.OK);
  }

  @PostMapping("/backup")
  public ResponseEntity<Response> backup(HttpServletRequest request,
      @RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException, IOException {
    return new ResponseEntity<>(new Response(null, Variables.MODEL_SETTINGS,
        settingsRepo.backup(request.getUserPrincipal().getName()).value(), lang), HttpStatus.OK);
  }

  @GetMapping("/backup")
  public ResponseEntity<Response> listBackups(@RequestParam(value = "lang", defaultValue = "fa") String lang)
      throws NoSuchFieldException, IllegalAccessException {
    return new ResponseEntity<>(
        new Response(settingsRepo.listBackups(lang), Variables.MODEL_SETTINGS, HttpStatus.OK.value(), lang),
        HttpStatus.OK);
  }

  @GetMapping("/restore")
  public ResponseEntity<Response> restore(HttpServletRequest request,
      @RequestParam(value = "lang", defaultValue = "fa") String lang, @RequestParam(value = "id") String id)
      throws NoSuchFieldException, IllegalAccessException {

    return new ResponseEntity<>(new Response(null, Variables.MODEL_SETTINGS,
        settingsRepo.restore(request.getUserPrincipal().getName(), id).value(), lang), HttpStatus.OK);

  }

  @RestController
  @RequestMapping(("/api/public/properties/settings"))
  public class PublicSettingsController {
    @GetMapping
    public ResponseEntity<Response> password(@RequestParam(value = "lang", defaultValue = "fa") String lang)
        throws NoSuchFieldException, IllegalAccessException {
      return new ResponseEntity<>(
          new Response(new Settings(mongoTemplate).password(), Variables.MODEL_SETTINGS, HttpStatus.OK.value(), lang),
          HttpStatus.OK);
    }
  }
}
