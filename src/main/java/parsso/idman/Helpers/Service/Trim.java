package parsso.idman.Helpers.Service;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Trim {
    String serviceId;

    public static String trimServiceId(String serviceId) {
        if (serviceId.contains("https")) {
            serviceId = serviceId.substring(8);


        } else if (serviceId.contains("http")) {
            serviceId = serviceId.substring(7);

        }

        int index = serviceId.indexOf("/");
        if (index > 0)
            serviceId = serviceId.substring(0, index);

        return serviceId;
    }

    public static long extractIdFromFile(String file) {
        return Long.valueOf(file.substring(file.length() - 18, file.length() - 5));
    }

}
