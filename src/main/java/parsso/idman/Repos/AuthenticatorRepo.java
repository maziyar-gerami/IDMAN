package parsso.idman.repos;

import org.springframework.http.HttpStatus;
import parsso.idman.models.other.Devices;

import java.util.LinkedList;

public interface AuthenticatorRepo {
    LinkedList<Devices> retrieve(String username);
    HttpStatus deleteByDeviceName(String name);
    HttpStatus deleteByUsername(String usename);
}
