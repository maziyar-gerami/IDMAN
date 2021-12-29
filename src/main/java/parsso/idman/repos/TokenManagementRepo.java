package parsso.idman.repos;

import org.springframework.http.HttpStatus;

public interface TokenManagementRepo {
    String retrieve(String userId);

    HttpStatus delete(String username, String token);

    HttpStatus delete(String username);

    HttpStatus create(String username, String token);

    boolean valid(String userId, String token);
}
