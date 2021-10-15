package parsso.idman.repos;


import org.springframework.stereotype.Service;

@Service
public interface MagfaSMSSendRepo {
    void SendMessage(String message, String PhoneNumber, Long id);

}
