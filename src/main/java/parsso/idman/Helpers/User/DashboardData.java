package parsso.idman.Helpers.User;

import io.jsonwebtoken.io.IOException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Event;
import parsso.idman.Models.User;
import parsso.idman.RepoImpls.EventRepoImpl;
import parsso.idman.RepoImpls.ServiceRepoImpl;
import parsso.idman.Repos.EventRepo;
import parsso.idman.Repos.UserRepo;

import java.text.ParseException;
import java.util.List;


@Service
public  class DashboardData {
    @Autowired
    UserRepo userRepo;
    @Autowired
    EventRepo eventRepo;

    public JSONObject retrieveDashboardData() throws IOException, java.text.ParseException, java.io.IOException, org.json.simple.parser.ParseException {
        JSONObject jsonObject = new JSONObject();

        //________users data____________
        JSONObject userJson = new JSONObject();
        List<User> usersList = userRepo.retrieveUsersFull();
        int nUsers = userRepo.retrieveUsersMain().size();
        int nActive = 0;
        int nLocked = 0;
        int nDisabled = 0;

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
        List<Event> events = eventRepo.getListUserEvents();
        int nSuccessful = 0;
        int nUnSucceful = 0;

        for (Event event : events) {
            if (event.getType().equals("Unsuccessful Login")) {
                nUnSucceful++;

            } else if (event.getType().equals("Successful Login"))
                nSuccessful++;
        }
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