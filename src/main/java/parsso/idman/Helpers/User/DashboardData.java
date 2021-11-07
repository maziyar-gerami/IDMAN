package parsso.idman.helpers.user;


import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.Variables;
import parsso.idman.models.dashboardData.Dashboard;
import parsso.idman.models.logs.Event;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.utils.convertor.DateUtils;
import parsso.idman.repos.LogsRepo;
import parsso.idman.repos.ServiceRepo;
import parsso.idman.repos.UserRepo;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class DashboardData {
    final ZoneId zoneId = ZoneId.of(Variables.ZONE);
    @Autowired
    UserRepo userRepo;
    @Autowired
    LogsRepo.EventRepo eventRepo;
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
    Dashboard.Users fUsers;
    Dashboard.Services fServices;
    Dashboard.Logins fLogins;

    public Dashboard retrieveDashboardData() throws IOException {

        Thread thread = new Thread(() -> {
            try {
                updateDashboardData();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();

        return mongoTemplate.findOne(new Query(Criteria.where("_id").is("Dashboard")), Dashboard.class,
                Variables.col_extraInfo);

    }

    public void updateDashboardData() throws InterruptedException {

        Thread userData = new Thread(() -> {
            //________users data____________
            int nUsers = userRepo.retrieveUsersLDAPSize();

            int nDisabled = (int) mongoTemplate.count(new Query(Criteria.where("status").is("disable")), UsersExtraInfo.class, Variables.col_usersExtraInfo);
            int nLocked = (int) mongoTemplate.count(new Query(Criteria.where("status").is("lock")), UsersExtraInfo.class, Variables.col_usersExtraInfo);
            int temp = nUsers - nLocked - nDisabled;
            int nActive = Math.min((temp), nUsers);

            fUsers = new Dashboard.Users(nUsers, nActive, nDisabled, nLocked);

        });
        userData.start();

        Thread servicesData = new Thread(() -> {

            //________services data____________
            List<parsso.idman.models.services.Service> services;
            services = serviceRepo.listServicesFull();
            int nServices = 0;
            if (services != null) {
                nServices = services.size();
            }
            int nEnabledServices = 0;

            for (parsso.idman.models.services.Service service : Objects.requireNonNull(services)) {
                if (service.getAccessStrategy() != null && service.getAccessStrategy().isEnabled())
                    nEnabledServices++;
            }

            int nDisabledServices = nServices - nEnabledServices;

            fServices = new Dashboard.Services(nServices, nDisabledServices, nEnabledServices);

        });

        Thread loginData = new Thread(() -> {
            //__________________login data____________

            List<Event> events = eventRepo.analyze(0, 0);
            int nSuccessful = 0;
            int nUnSucceful = 0;

            for (Event event : events) {
                ZonedDateTime eventDate = OffsetDateTime.parse(event.getCreationTime()).atZoneSameInstant(zoneId);

                if (event.getType().equals("Unsuccessful Login") && DateUtils.isToday(eventDate)) {
                    nUnSucceful++;
                } else if (event.getType().equals("Successful Login") && DateUtils.isToday(eventDate))
                    nSuccessful++;
            }

            fLogins = new Dashboard.Logins(nSuccessful + nUnSucceful, nUnSucceful, nSuccessful);
        });

        loginData.start();
        servicesData.start();

        loginData.join();
        userData.join();
        servicesData.join();

        Dashboard dashboard = new Dashboard(fServices, fLogins, fUsers);

        dashboard.setId("Dashboard");

        mongoTemplate.remove(new Query(Criteria.where("_id").is("id")),
                Variables.col_extraInfo);

        mongoTemplate.save(dashboard, Variables.col_extraInfo);

    }
}