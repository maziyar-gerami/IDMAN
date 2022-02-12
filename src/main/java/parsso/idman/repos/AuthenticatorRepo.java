package parsso.idman.repos;

import org.springframework.http.HttpStatus;
import parsso.idman.models.other.Devices;

public interface AuthenticatorRepo {
    Devices.DeviceList retrieve(String username, String deviceName, int skip, int limit);

    HttpStatus deleteByDeviceName(String name,String doer);

    HttpStatus deleteByUsername(String username,String doer);

    Boolean retrieveUsersDevice(String username);

    HttpStatus deleteByUsernameAndDeviceName(String username, String deviceName,String doer);
}
