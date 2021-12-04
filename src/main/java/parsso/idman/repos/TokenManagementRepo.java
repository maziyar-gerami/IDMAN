package parsso.idman.repos;

import org.springframework.http.HttpStatus;

public interface TokenManagementRepo {
    String retrieve(String userId);
    HttpStatus delete(String userName, String token);
    HttpStatus delete(String userName);
    HttpStatus create(String userName, String token);

    boolean valid(String userId, String token);
}
