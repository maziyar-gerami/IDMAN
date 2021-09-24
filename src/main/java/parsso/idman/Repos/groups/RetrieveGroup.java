package parsso.idman.Repos.groups;


import org.json.simple.parser.ParseException;
import parsso.idman.Models.Groups.Group;
import parsso.idman.Models.Users.User;

import java.io.IOException;
import java.util.List;

public interface RetrieveGroup {
	Group retrieveOu(boolean simple, String name) throws IOException, ParseException;

	List<Group> retrieveCurrentUserGroup(User user);
}
