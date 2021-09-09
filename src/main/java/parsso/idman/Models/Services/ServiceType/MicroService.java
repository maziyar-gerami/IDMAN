package parsso.idman.Models.Services.ServiceType;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.Models.Services.Service;

import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class MicroService {
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String notificationApiURL;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String notificationApiKey;

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
        try {
            this.notificationApiKey = Objects.requireNonNull(microService).getNotificationApiKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.notificationApiURL = microService.getNotificationApiURL();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public MicroService(Service service) {
        this._id = service.getId();
        this.serviceId = service.getServiceId();
        this.description = service.getDescription();
    }

}
