package parsso.idman.Models.Users;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsersGroups {
	private Inconsistency users;
	private Inconsistency groups;

	public UsersGroups(Inconsistency users, Inconsistency groups) {
		this.users = users;
		this.groups = groups;
	}
}
