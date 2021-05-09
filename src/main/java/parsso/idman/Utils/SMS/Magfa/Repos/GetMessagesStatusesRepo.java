package parsso.idman.Utils.SMS.Magfa.Repos;

import java.net.MalformedURLException;
import java.util.ArrayList;

public interface GetMessagesStatusesRepo {
    public ArrayList<String> GetMessagesStatuses(Long mid) throws MalformedURLException;
}
