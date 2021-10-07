package parsso.idman.Helpers.User;


import io.jsonwebtoken.io.IOException;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.DashboardData.Dashboard;
import parsso.idman.Models.Logs.Event;
import parsso.idman.Models.Users.UsersExtraInfo;
import parsso.idman.Repos.logs.events.EventRepo;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.Repos.UserRepo;
import parsso.idman.Utils.Convertor.DateUtils;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class DashboardData {
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
	ZoneId zoneId = ZoneId.of(Variables.ZONE);
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
			List<parsso.idman.Models.Services.Service> services = null;
			try {
				services = serviceRepo.listServicesFull();
			} catch (java.io.IOException | ParseException e) {
				e.printStackTrace();
			}
			int nServices = 0;
			if (services != null) {
				nServices = services.size();
			}
			int nEnabledServices = 0;

			for (parsso.idman.Models.Services.Service service : Objects.requireNonNull(services)) {
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