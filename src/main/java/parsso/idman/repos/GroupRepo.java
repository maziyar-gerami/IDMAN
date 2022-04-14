package parsso.idman.repos;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import parsso.idman.models.groups.Group;
import parsso.idman.models.users.User;

import java.util.List;

@SuppressWarnings("SameReturnValue")
public class GroupRepo {
    public interface Retrieve {
        List<Group> retrieve();

        Group retrieve(boolean simple, String name);
        
        List<Group> retrieve(User user);

    }

    public interface Update {
        HttpStatus update(String doerID, String name, Group ou);
    }

    public interface Create {
        HttpStatus create(String doerId, Group ou);

    }

    public interface Delete {
        HttpStatus remove(String doerId, JSONObject jsonObject);
    }
}