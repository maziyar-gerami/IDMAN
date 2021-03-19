package parsso.idman.Helpers.User;


import io.jsonwebtoken.io.IOException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Event;
import parsso.idman.Repos.EventRepo;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.Repos.UserRepo;
import parsso.idman.Utils.Convertor.DateUtils;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class DashboardData {
    public static String mainCollection = "MongoDbCasEventRepository";
    @Autowired
    UserRepo userRepo;
    @Autowired
    EventRepo eventRepo;
    @Autowired
    ServiceRepo serviceRepo;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    UserAttributeMapper userAttributeMapper;
    @Autowired
    SimpleUserAttributeMapper simpleUserAttributeMapper;
    @Autowired
    LdapTemplate ldapTemplate;

    ZoneId zoneId = ZoneId.of("UTC+03:30");


    public JSONObject retrieveDashboardData() throws IOException, InterruptedException {

        JSONObject jsonObject = new JSONObject();
        JSONObject userJson = new JSONObject();
        JSONObject servicesJson = new JSONObject();
        JSONObject loginJson = new JSONObject();


        Thread userData = new Thread(() -> {
            //________users data____________
            int nUsers = userRepo.retrieveUsersSize();

            int nDisabled = ldapTemplate.search(query().where("pwdAccountLockedTime").is("40400404040404.950Z"), simpleUserAttributeMapper).size();
            int nLocked = ldapTemplate.search(query().where("pwdAccountLockedTime").lte("40400404040404.950Z"), simpleUserAttributeMapper).size();;
            int temp = nUsers-nLocked-nDisabled;
            int nActive = (temp)>nUsers?nUsers:temp;

            userJson.put("total", nUsers);
            userJson.put("active", nActive);
            userJson.put("disabled", nDisabled);
            userJson.put("locked", nLocked);

        });
        userData.start();


        Thread servicesData = new Thread(() -> {

            //________services data____________
            List<parsso.idman.Models.Service> services = null;
            try {
                services = serviceRepo.listServicesFull();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }
            int nServices = services.size();
            int nEnabledServices = 0;

            for (parsso.idman.Models.Service service : services) {
                if (service.getAccessStrategy() != null && service.getAccessStrategy().isEnabled())
                    nEnabledServices++;
            }

            int nDisabledServices = nServices - nEnabledServices;

            servicesJson.put("total", nServices);
            servicesJson.put("enabled", nEnabledServices);
            servicesJson.put("disabled", nDisabledServices);

        });

        Thread loginData = new Thread(() -> {
            //__________________login data____________

            List<Event> events = eventRepo.analyze(mainCollection, 0, 0);
            int nSuccessful = 0;
            int nUnSucceful = 0;

            for (Event event : events) {
                //TODO: This is date
                ZonedDateTime eventDate = OffsetDateTime.parse(event.getCreationTime()).atZoneSameInstant(zoneId);


                if (event.getType().equals("Unsuccessful Login") && DateUtils.isToday(eventDate)) {
                    nUnSucceful++;
                } else if (event.getType().equals("Successful Login") && DateUtils.isToday(eventDate))
                    nSuccessful++;
            }
            loginJson.put("total", nSuccessful + nUnSucceful);
            loginJson.put("unsuccessful", nUnSucceful);
            loginJson.put("successful", nSuccessful);
        });

        //_________summary________________
        jsonObject.put("users", userJson);
        jsonObject.put("services", servicesJson);
        jsonObject.put("logins", loginJson);

        loginData.start();
        servicesData.start();

        loginData.join();
        userData.join();
        servicesData.join();

        return jsonObject;

    }
}