package parsso.idman.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.models.other.Property;
import parsso.idman.models.other.Setting;
import parsso.idman.models.response.Response;
import parsso.idman.repos.SettingsRepo;

import java.util.List;

@RestController
public class SettingsController {
    SettingsRepo settingsRepo;
    @Autowired
    SettingsController(SettingsRepo settingsRepo){
        this.settingsRepo = settingsRepo;
    }
    @GetMapping("/api/settings")
    public ResponseEntity<Response> retrieveSettings(@RequestParam(value = "lang",defaultValue = "fa") String lang) {
        List<Setting> settings = settingsRepo.retrieve();
        List<Property> properties = new Property().settingsToProperty(settings,lang);
        return new ResponseEntity<>(new Response(properties,lang), HttpStatus.OK);
    }

}
