package parsso.idman.repoImpls;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.models.other.Devices;
import parsso.idman.repos.AuthenticatorRepo;

import java.util.LinkedList;

@Service
public class AuthenticatorRepoImpl implements AuthenticatorRepo {
    @Override
    public LinkedList<Devices> retrieve(String username) {
        return null;
    }

    @Override
    public HttpStatus deleteByDeviceName(String name) {
        return null;
    }

    @Override
    public HttpStatus deleteByUsername(String usename) {
        return null;
    }
}
