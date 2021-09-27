package parsso.idman.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.Helpers.ReloadConfigs.PasswordSettings;
import parsso.idman.Models.Logs.Setting;
import parsso.idman.Repos.SettingsRepo;
import parsso.idman.Repos.UserRepo;

import java.io.IOException;
import java.util.List;

import static java.lang.Thread.sleep;

@RestController
public class SettingsController {
	final int millis = 3600000;
	private final SettingsRepo settingsRepo;
	@Value("${interval.check.pass.hours}")
	private long intervalCheckPassTime;

	public SettingsController(SettingsRepo settingsRepo) {
		this.settingsRepo = settingsRepo;
	}

	@Autowired
	public SettingsController(UserRepo userRepo, PasswordSettings passwordSettings, SettingsRepo settingsRepo) {
		this.settingsRepo = settingsRepo;
	}

	@GetMapping("/api/settings/notification/email")
	public ResponseEntity<HttpStatus> enableEmailNotification() {

		Runnable runnable = () -> {
			while (true) {

				settingsRepo.emailNotification();
				try {
					//noinspection BusyWait
					sleep(intervalCheckPassTime * millis);
				} catch (InterruptedException e) {
					break;
				}

			}
		};

		for (Thread t : Thread.getAllStackTraces().keySet()) {
			if (t.getName().equals("thread-pulling-email-passExpire")) {
				try {
					t.interrupt();
				} catch (Exception e) {
					e.printStackTrace();
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

	@SuppressWarnings("BusyWait")
	@GetMapping("/api/settings/notification/message")
	public ResponseEntity<String> enableMessageNotification() {

		Runnable runnable = () -> {
			while (true) {
				settingsRepo.instantMessageNotification();
				try {
					sleep(intervalCheckPassTime * millis);
				} catch (InterruptedException e) {
					break;
				}
			}
		};

		for (Thread t : Thread.getAllStackTraces().keySet()) {
			if (t.getName().equals("thread-pulling-sms-passExpire")) {
				try {
					t.interrupt();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}

		Thread thread = new Thread(runnable);
		thread.setName("thread-pulling-sms-passExpire");

		if (thread.isAlive()) {
			thread.interrupt();
		} else
			thread.start();

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/api/settings/switches")
	public ResponseEntity<List<Setting>> listSwitches() throws IOException {

		return new ResponseEntity<>(settingsRepo.retrieveTFSetting(), HttpStatus.OK);

	}
}