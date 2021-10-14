package parsso.idman.Repos.tickets;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public interface DeleteTicket {
	HttpStatus deleteTicket(String doer, JSONObject jsonObject);
}
