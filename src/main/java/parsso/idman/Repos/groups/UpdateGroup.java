package parsso.idman.Repos.groups;


import org.springframework.http.HttpStatus;
import parsso.idman.Models.Groups.Group;

public interface UpdateGroup {
	HttpStatus update(String doerID, String name, Group ou);

}
