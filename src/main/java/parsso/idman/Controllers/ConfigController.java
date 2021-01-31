package parsso.idman.Controllers;


import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Helpers.Config.PasswordRegulation;
import parsso.idman.Models.Config;
import parsso.idman.Models.Setting;
import parsso.idman.Repos.ConfigRepo;

import java.io.IOException;
import java.util.List;

@RestController
public class ConfigController {

    @Qualifier("configRepoImpl")

    @Autowired
    private ConfigRepo configRepo;

    @Autowired
    PasswordRegulation passwordRegulation;


    @GetMapping("/api/configs")
    public ResponseEntity<String> retrieveSettings() throws IOException {
        return new ResponseEntity<>(configRepo.retrieveSetting(), HttpStatus.OK);
    }

    @GetMapping("/api/configs/list")
    public ResponseEntity<List<Config>> listAllConfigs() throws IOException, ParseException {
        return new ResponseEntity<>(configRepo.listBackedUpConfigs(), HttpStatus.OK);
    }

    @PutMapping("/api/configs")
    public ResponseEntity<String> updateSettings(@RequestBody List<Setting> settings) throws IOException {
        configRepo.updateSettings(settings);
        passwordRegulation.update();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/api/configs/system/{system}")
    public ResponseEntity<String> updateSettingsSystem(@RequestBody List<Setting> settings) throws IOException {
        configRepo.updateSettings(settings);
        passwordRegulation.update();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/configs/backup")
    public ResponseEntity<String> backupSettings() {
        return new ResponseEntity<>(configRepo.backupConfig());
    }

    @GetMapping("/api/configs/restore/{cname}")
    public ResponseEntity<HttpStatus> restoreSettings(@PathVariable String cname)
            throws IOException, ParseException, java.text.ParseException {
        return new ResponseEntity<>(configRepo.restore(cname));
    }

    @GetMapping("/api/configs/reset")
    public ResponseEntity<HttpStatus> resetFactory() throws IOException {
        return new ResponseEntity<>(configRepo.factoryReset());
    }


}
