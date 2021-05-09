package parsso.idman.Utils.SMS.Magfa.Repos;

import java.net.MalformedURLException;

public interface GetMessageIdRepo {
    public String GetMessage(Long id) throws MalformedURLException;
}
