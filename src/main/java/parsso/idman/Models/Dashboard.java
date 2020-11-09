package parsso.idman.Models;

import net.minidev.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import parsso.idman.RepoImpls.EventRepoImpl;
import parsso.idman.RepoImpls.ServiceRepoImpl;
import parsso.idman.Repos.EventRepo;
import parsso.idman.Repos.UserRepo;

import java.io.IOException;
import java.util.List;

public class Dashboard {


    JSONObject userJson;
    List<User> usersList;
    int nUsers;
    int nActive;
    int nLocked;
    int nDisabled;
    @Autowired
    UserRepo userRepo;


    public Dashboard() {

        //________users data____________
        userJson = new JSONObject();
        usersList = userRepo.retrieveUsersFull();
        nUsers = usersList.size();
        nActive = 0;
        nLocked = 0;
        nDisabled = 0;

    }


    public JSONObject retrieveDashboardData() throws IOException, ParseException, java.text.ParseException {
        JSONObject jsonObject = new JSONObject();

        for (User user : usersList) {
            if (user.getStatus().equals("active"))
                nActive++;
            else if (user.getStatus().equals("disabled"))
                nDisabled++;
            else if (user.getStatus().equals("locked"))
                nLocked++;
        }
        userJson.put("total", nUsers);
        userJson.put("active", nActive);
        userJson.put("disabled", nDisabled);
        userJson.put("locked", nLocked);

        //________services data____________
        JSONObject servicesJson = new JSONObject();
        ServiceRepoImpl serviceRepo = new ServiceRepoImpl();
        List<parsso.idman.Models.Service> services = serviceRepo.listServices();
        int nServices = services.size();
        int nEnabledServices = 0;

        for (parsso.idman.Models.Service service : services) {
            if (service.getAccessStrategy().isEnabled())
                nEnabledServices++;
        }

        int nDisabledServices = nServices - nEnabledServices;

        servicesJson.put("total", nServices);
        servicesJson.put("enabled", nEnabledServices);
        servicesJson.put("disabled", nDisabledServices);

        //__________________login data____________
        JSONObject loginJson = new JSONObject();
        EventRepo eventRepo = new EventRepoImpl();
        List<Event> events = eventRepo.analyze();
        int nSuccessful = 0;
        int nUnSucceful = 0;

        /*for (Event event : events) {
            if (event.getAction().equals("AUTHENTICATION_FAILED")) {
                nUnSucceful++;

            } else if (event.getAction().equals("AUTHENTICATION_SUCCESS"))
                nSuccessful++;
        }*/
        loginJson.put("total", nSuccessful + nUnSucceful);
        loginJson.put("unsuccessful", nUnSucceful);
        loginJson.put("successful", nSuccessful);

        //_________summary________________
        jsonObject.put("users", userJson);
        jsonObject.put("services", servicesJson);
        jsonObject.put("logins", loginJson);

        return jsonObject;

    }
}
