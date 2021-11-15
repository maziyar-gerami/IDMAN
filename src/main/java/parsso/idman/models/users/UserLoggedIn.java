package parsso.idman.models.users;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoggedIn{
    String userId;
    boolean loggedIn;
}
