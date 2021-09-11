package parsso.idman.Models.Services.ServicesSubModel;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Contact {
	@JsonProperty("@class")
	private String atClass;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String name;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String email;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String phone;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String department;

	public Contact() {
		atClass = "org.apereo.cas.services.DefaultRegisteredServiceContact";
	}
}
