package parsso.idman.RepoImpls;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import parsso.idman.Models.PublicMessage;
import parsso.idman.Repos.PubMessageRepo;

import java.util.List;

@Service
public class PubMessageRepoImpl implements PubMessageRepo {
    @Override
    public List<PublicMessage> showPubicMessages(String id) {
        return null;
    }

    @Override
    public HttpStatus postPubicMessage(String doer, String message) {
        return null;
    }

    @Override
    public HttpStatus editPubicMessage(String doer, String message) {
        return null;
    }

    @Override
    public HttpStatus deletePubicMessage(String doer, String id) {
        return null;
    }
}
