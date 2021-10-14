package parsso.idman.repos;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public interface SettingsRepo {
	HttpStatus emailNotification();

	HttpStatus instantMessageNotification();

}
