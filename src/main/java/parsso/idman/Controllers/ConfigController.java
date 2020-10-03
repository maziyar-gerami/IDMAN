package parsso.idman.Controllers;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.Models.Setting;
import parsso.idman.Repos.ConfigRepo;


import java.io.IOException;
import java.util.List;

@RestController
public class ConfigController {

    @Qualifier("configRepoImpl")

    @Autowired
    private ConfigRepo configRepo;


    @GetMapping("/api/configs")
    public ResponseEntity<String> retrieveSettings() throws IOException {
        return new ResponseEntity<>(configRepo.retrieveSetting(), HttpStatus.OK);
    }

    @PutMapping("/api/configs")
    public ResponseEntity<String> updateSettings(@RequestBody List<Setting> settings) throws IOException {
        return new ResponseEntity<>(configRepo.updateSettings(settings), HttpStatus.OK);
    }

}
