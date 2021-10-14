package parsso.idman.repos;


import org.springframework.stereotype.Service;

@Service
public interface SettingsRepo {
	void emailNotification();

	void instantMessageNotification();

}
