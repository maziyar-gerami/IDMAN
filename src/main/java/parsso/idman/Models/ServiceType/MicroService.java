package parsso.idman.Models.ServiceType;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.net.InetAddress;
import java.util.List;

@Setter
@Getter
public class MicroService {
    private String serviceId;
    private List<String> IPaddresses;
    private ObjectId _id;

    public MicroService(String serviceId, List<String> machines) {
        this.serviceId = serviceId;
        this.IPaddresses = machines;
    }
}
