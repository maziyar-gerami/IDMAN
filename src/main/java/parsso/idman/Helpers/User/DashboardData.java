package parsso.idman.Helpers.User;


import io.jsonwebtoken.io.IOException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Models.DashboardData.Dashboard;
import parsso.idman.Models.DashboardData.Logins;
import parsso.idman.Models.DashboardData.Services;
import parsso.idman.Models.DashboardData.Users;
import parsso.idman.Models.Logs.Event;
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

    Users fUsers;
    Services fServices;
    Logins fLogins;



    public Dashboard retrieveDashboardData() throws IOException, InterruptedException {


        Thread thread = new Thread(){
            @SneakyThrows
            public void run(){
                updateDashboardData();
            }
        };

        thread.start();

        return mongoTemplate.findOne(new Query(Criteria.where("_id").is("Dashboard")),Dashboard.class,
                "IDMAN_ExtraInfo");

    }

    public void updateDashboardData() throws InterruptedException {

        Thread userData = new Thread(() -> {
            //________users data____________
            int nUsers = userRepo.retrieveUsersSize("","","","");

            int nDisabled = ldapTemplate.search(query().where("pwdAccountLockedTime").is("40400404040404.950Z"), simpleUserAttributeMapper).size();
            int nLocked = ldapTemplate.search(query().where("pwdAccountLockedTime").lte("40400404040404.950Z"), simpleUserAttributeMapper).size();
            int temp = nUsers-nLocked-nDisabled;
            int nActive = (temp)>nUsers?nUsers:temp;

            fUsers = new Users(nUsers,nActive,nDisabled,nLocked);

        });
        userData.start();

        Thread servicesData = new Thread(() -> {

            //________services data____________
            List<parsso.idman.Models.Services.Service> services = null;
            try {
                services = serviceRepo.listServicesFull();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }
            int nServices = services.size();
            int nEnabledServices = 0;

            for (parsso.idman.Models.Services.Service service : services) {
                if (service.getAccessStrategy() != null && service.getAccessStrategy().isEnabled())
                    nEnabledServices++;
            }

            int nDisabledServices = nServices - nEnabledServices;

            fServices =new Services(nServices,nDisabledServices,nEnabledServices);

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

            fLogins = new Logins (nSuccessful+nUnSucceful,nUnSucceful,nSuccessful);
        });


        loginData.start();
        servicesData.start();

        loginData.join();
        userData.join();
        servicesData.join();

        Dashboard dashboard = new Dashboard(fServices,fLogins,fUsers);

        dashboard.setId("Dashboard");


        mongoTemplate.remove(new Query(Criteria.where("_id").is("id")),
                "IDMAN_ExtraInfo");

        mongoTemplate.save(dashboard, "IDMAN_ExtraInfo");

    }
}