package parsso.idman.repos;


import parsso.idman.models.other.SkyRoom;
import parsso.idman.models.users.User;

import java.io.IOException;

public interface SkyroomRepo {
    SkyRoom run(User user) throws IOException;

}
