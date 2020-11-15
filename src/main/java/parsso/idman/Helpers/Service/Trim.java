package parsso.idman.Helpers.Service;

import lombok.Getter;
import lombok.Setter;
import parsso.idman.Models.ServiceType.CasService;

@Setter
@Getter
public class Trim {
    String serviceId;

    public static String trimServiceId(String serviceId){
        if (serviceId.contains("https")){
            serviceId = serviceId.substring(8);


        }else if (serviceId.contains("http")){
            serviceId = serviceId.substring(7);

        }
        if (!(serviceId.contains("www.")))
            serviceId = "www."+serviceId;

        int index = serviceId.indexOf("/");
        if (index>0)
            serviceId = serviceId.substring(0,index);

        return serviceId;
    }
}
