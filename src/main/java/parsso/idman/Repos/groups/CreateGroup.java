package parsso.idman.Repos.groups;


import org.springframework.http.HttpStatus;
import parsso.idman.Models.Groups.Group;

public interface CreateGroup {
	HttpStatus create(String doerId, Group ou);

}
