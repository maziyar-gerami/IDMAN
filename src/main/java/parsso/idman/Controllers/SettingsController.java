package parsso.idman.Controllers;


import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.Helpers.Communicate.Email;
import parsso.idman.Helpers.Communicate.InstantMessage;
import parsso.idman.Helpers.ReloadConfigs.PasswordSettings;
import parsso.idman.Models.Setting;
import parsso.idman.Repos.ConfigRepo;
import parsso.idman.Repos.SettingsRepo;
import parsso.idman.Repos.UserRepo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@RestController
public class SettingsController {

    int millis= 3600000;

    @Autowired
    private SettingsRepo settingsRepo;

    @Autowired
    PasswordSettings passwordSettings;

    @Value("${interval.check.pass.hours}")
    private long intervalCheckPassTime;

    @Autowired
    UserRepo userRepo;


    @Autowired
    InstantMessage instantMessage;

    @Autowired
    Email email;

    @Autowired
    ConfigRepo configRepo;


    @GetMapping("/api/settings/notification/email")
    public ResponseEntity<HttpStatus> enableEmailNotification() {

        ScheduledExecutorService executor =
                Executors.newSingleThreadScheduledExecutor();

        Runnable runnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {

                    settingsRepo.emailNotification();
                    Thread.sleep(intervalCheckPassTime*millis);


                }
            }
        };

        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals("thread-pulling-email-passExpire")) {
                try {
                    t.interrupt();
                }catch (Exception e){

                }
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        Thread thread = new Thread(runnable);
        thread.setName("thread-pulling-email-passExpire");

        if (thread.isAlive())
            thread.interrupt();
        else
            thread.start();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/settings/notification/message")
    public ResponseEntity<String> enableMessageNotification() {

        Runnable runnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true){
                    settingsRepo.messageNotification();
                    Thread.sleep(intervalCheckPassTime*millis);
                }
            }
        };

        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals("thread-pulling-sms-passExpire")) {
                try {
                    t.interrupt();
                }catch (Exception e){

                }
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }

        Thread thread = new Thread(runnable);
        thread.setName("thread-pulling-sms-passExpire");

        if (thread.isAlive()) {
            thread.interrupt();
        }

        else
            thread.start();


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/settings/switches")
    public ResponseEntity<List<Setting>> listSwitches() throws IOException {

        return new ResponseEntity<>(settingsRepo.retrieveTFSetting(),HttpStatus.OK);

    }
}
