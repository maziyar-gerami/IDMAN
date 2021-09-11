package parsso.idman.Models.License;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class License {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	List licensed;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	List unLicensed;

	public License(List licensed, List unLicensed) {
		this.licensed = licensed;
		this.unLicensed = unLicensed;
	}

}
