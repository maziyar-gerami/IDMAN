package parsso.idman.Models.Services.ServiceType;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.Models.Services.Service;
import parsso.idman.Models.Services.ServiceGist;

import java.util.List;

@Setter
@Getter
public class MicroService implements Comparable {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String serviceId;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<String> IPaddresses;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private long _id;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String name;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String description;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String logo;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String url;
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private int position;
	@JsonIgnore
	private String notificationApiURL;
	@JsonIgnore
	private String notificationApiKey;
	private ServiceGist notification;

	public MicroService(String serviceId, List<String> machines) {
		this.serviceId = serviceId;
		this.IPaddresses = machines;
		this.url = serviceId;

	}

	public MicroService(Long id, String url) {
		this._id = id;
		this.url = url;
		this.position = 0;
	}

	public MicroService() {
		this.url = serviceId;
		this.position = 0;
	}

	public MicroService(Service service, MicroService microService) {
		this._id = service.getId();
		this.name = service.getName();
		this.serviceId = service.getServiceId();
		this.description = service.getDescription();
		this.logo = service.getLogo();
		this.url = (null != microService && null != microService.getUrl() ? microService.getUrl() : service.getServiceId());
		this.position = (null != microService ? microService.getPosition() : 0);
	}

	public MicroService(Service service) {
		this._id = service.getId();
		this.serviceId = service.getServiceId();
		this.description = service.getDescription();
	}

	@Override
	public int compareTo(Object o) {
		MicroService second = (MicroService) o;
		if (this.getPosition() > second.getPosition())
			return -1;
		else if (this.getPosition() < second.getPosition())
			return 1;
		else
			return 0;
	}


}
