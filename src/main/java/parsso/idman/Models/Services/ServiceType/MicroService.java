package parsso.idman.models.services.serviceType;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import parsso.idman.models.services.Schedule;
import parsso.idman.models.services.Service;
import parsso.idman.models.services.ServiceGist;

import java.util.List;

@SuppressWarnings("rawtypes")
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ServiceGist notification;
    private List<Schedule> dailyAccess;


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
        try {
            this.notificationApiURL = microService.getNotificationApiURL();
        } catch (NullPointerException e) {
            this.notificationApiURL = null;
        }

        try {
            this.notificationApiKey = microService.getNotificationApiKey();
        } catch (NullPointerException e) {
            this.notificationApiKey = null;
        }

        try {
            this.url = (null != microService && null != microService.getUrl() ? microService.getUrl() : service.getServiceId());
        } catch (NullPointerException ignored) {
        }
        try {
            this.position = (null != microService ? microService.getPosition() : 0);
        } catch (NullPointerException ignored) {
        }

        try {
            this.dailyAccess = microService.getDailyAccess();
        } catch (NullPointerException ignored) {
        }

    }

    public MicroService(Service service) {
        this._id = service.getId();
        this.serviceId = service.getServiceId();
        this.description = service.getDescription();


    }

    @Override
    public int compareTo(Object o) {
        MicroService second = (MicroService) o;
        return Integer.compare(second.getPosition(), this.getPosition());
    }


}
