package parsso.idman.Models.ServiceType;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import parsso.idman.Models.Service;

import java.net.InetAddress;
import java.util.List;

@Setter
@Getter
public class MicroService implements Comparable{
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int position;

    public MicroService(String serviceId, List<String> machines) {
        this.serviceId = serviceId;
        this.IPaddresses = machines;
        this.url = serviceId;
    }

    public MicroService() {
        this.url = serviceId;
        this.position = 0;
    }

    public MicroService(long id, String name, String serviceId, String description, String logo) {
        this._id = id;
        this.name = name;
        this.serviceId = serviceId;
        this.description =description;
        this.logo = logo;
    }


    public MicroService(Service service, MicroService microService) {
        this._id = service.getId();
        this.name = service.getName();
        this.serviceId = service.getServiceId();
        this.description =service.getDescription();
        this.logo = service.getLogo();
        this.url = microService.getUrl();
        this.position = microService.position;

    }



    @Override
    public int compareTo(Object o) {
        MicroService second = (MicroService) o;
        if (this.get_id()>second.get_id())
            return -1;
        else if (this.get_id()<second.get_id())
            return 1;
        else
            return 0;
    }
}
