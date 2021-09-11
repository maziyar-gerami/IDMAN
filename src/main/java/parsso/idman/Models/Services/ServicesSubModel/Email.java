package parsso.idman.Models.Services.ServicesSubModel;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Email {
	@JsonProperty("@class")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String atClass;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Object[] values;

}
