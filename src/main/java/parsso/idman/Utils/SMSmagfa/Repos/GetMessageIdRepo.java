package parsso.idman.Utils.SMSmagfa.Repos;

import java.net.MalformedURLException;

public interface GetMessageIdRepo {
    public String GetMessage(Long id) throws MalformedURLException;
}
