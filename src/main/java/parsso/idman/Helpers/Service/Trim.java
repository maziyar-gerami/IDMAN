package parsso.idman.Helpers.Service;

import parsso.idman.Models.Service;

public class Trim {
    Service service;
    public Trim(Service service){
        this.service = service;
    }
     public String trimServiceId(){
        String serviceId = service.getServiceId();
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
