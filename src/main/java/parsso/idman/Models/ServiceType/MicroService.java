package parsso.idman.Models.ServiceType;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.net.InetAddress;
import java.util.List;

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

    public MicroService(String serviceId, List<String> machines) {
        this.serviceId = serviceId;
        this.IPaddresses = machines;
    }

    public MicroService(long id, String name,String serviceId, String description, String logo) {
        this._id = id;
        this.name = name;
        this.serviceId = serviceId;
        this.description =description;
        this.logo = logo;
    }
}
