package parsso.idman.Models.DashboardData;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Dashboard {
	@JsonIgnore
	private String id;
	private Services services;
	private Logins logins;
	private Users users;

	public Dashboard(Services services, Logins logins, Users users) {
		this.services = services;
		this.logins = logins;
		this.users = users;
	}
}

