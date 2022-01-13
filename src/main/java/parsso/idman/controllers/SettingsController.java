package parsso.idman.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.helpers.Variables;
import parsso.idman.models.other.Property;
import parsso.idman.models.other.Setting;
import parsso.idman.models.response.Response;
import parsso.idman.repos.SettingsRepo;

import java.util.List;

@RestController
@RequestMapping(("/api/settings"))
public class SettingsController {
    final SettingsRepo settingsRepo;
    @Autowired
    SettingsController(SettingsRepo settingsRepo){
        this.settingsRepo = settingsRepo;
    }
    @GetMapping
    public ResponseEntity<Response> retrieveSettings(@RequestParam(value = "lang",defaultValue = "fa") String lang) {
        List<Setting> settings = settingsRepo.retrieve();
        List<Property> properties = new Property().settingsToProperty(settings,lang);
        return new ResponseEntity<>(new Response(properties,lang), HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<Response> update(@RequestBody List<Property> properties,@RequestParam(value = "lang",defaultValue = "fa")String lang) throws NoSuchFieldException, IllegalAccessException {
        int status = settingsRepo.update(properties).value();
        if(status==200)
            return new ResponseEntity<>(new Response(Variables.MODEL_SETTINGS,lang),HttpStatus.OK);
        return new ResponseEntity<>(new Response(lang,Variables.MODEL_SETTINGS,400),HttpStatus.OK);
    }
}
