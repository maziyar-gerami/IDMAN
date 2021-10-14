package parsso.idman.controllers;


import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import parsso.idman.Helpers.ReloadConfigs.PasswordSettings;
import parsso.idman.Models.Logs.Config;
import parsso.idman.Models.Logs.Setting;
import parsso.idman.repos.ConfigRepo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
public class ConfigsController {
    private final ConfigRepo configRepo;
    PasswordSettings passwordSettings;

    public ConfigsController(PasswordSettings passwordSettings, @Qualifier("configRepoImpl") ConfigRepo configRepo) {
        this.passwordSettings = passwordSettings;
        this.configRepo = configRepo;
    }

    @GetMapping("/api/config")
    public ResponseEntity<String> retrieveSettings() throws IOException {
        return new ResponseEntity<>(configRepo.retrieveSetting(), HttpStatus.OK);
    }

    @GetMapping("/api/configs")
    public ResponseEntity<List<Config>> listAllConfigs() throws IOException, ParseException {
        return new ResponseEntity<>(configRepo.listBackedUpConfigs(), HttpStatus.OK);
    }

    @GetMapping("/api/configs/save")
    public ResponseEntity<HttpStatus> saveAllConfigs() throws IOException {
        return new ResponseEntity<>(configRepo.saveToMongo());
    }

    @PutMapping("/api/configs")
    public ResponseEntity<String> updateSettings(HttpServletRequest request, @RequestBody List<Setting> settings) throws IOException, ParseException {
        configRepo.updateSettings(request.getUserPrincipal().getName(), settings);
        passwordSettings.update(settings);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/configs/backup")
    public ResponseEntity<String> backupSettings() {
        return new ResponseEntity<>(configRepo.backupConfig());
    }

    @GetMapping("/api/configs/restore/{cname}")
    public ResponseEntity<HttpStatus> restoreSettings(HttpServletRequest request, @PathVariable String cname)
            throws IOException, ParseException, java.text.ParseException {
        return new ResponseEntity<>(configRepo.restore(request.getUserPrincipal().getName(), cname));
    }

    @GetMapping("/api/configs/reset")
    public ResponseEntity<HttpStatus> resetFactory(HttpServletRequest request) throws IOException, ParseException {
        return new ResponseEntity<>(configRepo.factoryReset(request.getUserPrincipal().getName()));
    }
}
