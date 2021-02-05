package parsso.idman.Controllers;


import lombok.SneakyThrows;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parsso.idman.Helpers.ReloadConfigs.PasswordSettings;
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
    PasswordSettings passwordSettings;

    @Value("${interval.check.pass.days}")
    private long intervalCheckPassDays;

    @GetMapping("/api/configs")
    public ResponseEntity<String> retrieveSettings() throws IOException {
        return new ResponseEntity<>(configRepo.retrieveSetting(), HttpStatus.OK);
    }

    @GetMapping("/api/configs/list")
    public ResponseEntity<List<Config>> listAllConfigs() throws IOException, ParseException {
        return new ResponseEntity<>(configRepo.listBackedUpConfigs(), HttpStatus.OK);
    }

    @GetMapping("/api/configs/notification/email")
    public ResponseEntity<HttpStatus> emailNotification() {

        Runnable runnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    //Thread.sleep(20000);

                    configRepo.emailNotification();
                    Thread.sleep(0200);

                }
            }
        };


        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals("thread-pulling-passExpire")) {
                t.interrupt();
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                Thread thread = new Thread(runnable);
                thread.setName("thread-pulling-passExpire");

                thread.start();
            }
        }

            return new ResponseEntity<>(HttpStatus.OK);


    }

    @GetMapping("/api/configs/notification/message")
    public ResponseEntity<List<Config>> messageNotification() {
        Runnable runnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true){
                    Thread.sleep(intervalCheckPassDays*86400000);

                    configRepo.messageNotification();

                }
            }
        };
        Thread thread = new Thread(runnable);

        if (thread.isAlive())
            thread.interrupt();
        else
            thread.start();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/api/configs")
    public ResponseEntity<String> updateSettings(@RequestBody List<Setting> settings) throws IOException {
        configRepo.updateSettings(settings);
        passwordSettings.update(settings);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/api/configs/system/{system}")
    public ResponseEntity<String> updateSettingsSystem(@RequestBody List<Setting> settings) throws IOException {
        configRepo.updateSettings(settings);
        passwordSettings.update(settings);
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
