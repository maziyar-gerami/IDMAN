package parsso.idman.repos;

import org.springframework.http.HttpStatus;
import parsso.idman.models.other.Devices;

import java.util.LinkedList;
import java.util.List;

public interface AuthenticatorRepo {
    Devices.DeviceList retrieve(String username, String deviceName, int skip, int limit);
    HttpStatus deleteByDeviceName(String name);
    HttpStatus deleteByUsername(String usename);
}
