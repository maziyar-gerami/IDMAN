package parsso.idman.controllers.ok;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parsso.idman.Repos.settings.SettingsRepo;

import static java.lang.Thread.sleep;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {
	final int millis = 3600000;
	private final SettingsRepo settingsRepo;
	@Value("${interval.check.pass.hours}")
	private long intervalCheckPassTime;


	@Autowired
	public SettingsController(SettingsRepo settingsRepo) {
		this.settingsRepo = settingsRepo;
	}

	@GetMapping("/notification/email")
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
	@GetMapping("/notification/message")
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

}
