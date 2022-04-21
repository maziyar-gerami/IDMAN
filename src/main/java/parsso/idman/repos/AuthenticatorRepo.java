package parsso.idman.repos;

import org.springframework.http.HttpStatus;
import parsso.idman.models.other.Devices;

public class AuthenticatorRepo {

  public interface Retrieve {
    Boolean retrieveUsersDevice(String username);

    Devices.DeviceList retrieve(String username, String deviceName, int skip, int limit);

  }

  public interface Delete {
    HttpStatus deleteByDeviceName(String name, String doer);

    HttpStatus deleteByUsername(String username, String doer);

    HttpStatus deleteByUsernameAndDeviceName(String username, String deviceName, String doer);
  }
}
