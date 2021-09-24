package parsso.idman.RepoImpls.transcripts;


import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import parsso.idman.Helpers.Service.ExtractLicensedAndUnlicensed;
import parsso.idman.Helpers.User.UsersLicense;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.License.License;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.Logs.Transcript;
import parsso.idman.Models.Services.Service;
import parsso.idman.Models.Services.ServiceType.MicroService;
import parsso.idman.Models.Users.UsersExtraInfo;
import parsso.idman.Repos.GroupRepo;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.Repos.transcripts.TranscriptRepo;
import parsso.idman.Repos.UserRepo;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Component
public class TranscriptRepoImpl implements TranscriptRepo {
	@Autowired
	ServiceRepo serviceRepo;
	@Autowired
	GroupRepo groupRepo;
	@Autowired
	UserRepo userRepo;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	UsersLicense usersLicense;
	@Autowired
	parsso.idman.Helpers.Group.GroupLicense groupLicense;
	@Autowired
	ExtractLicensedAndUnlicensed extract;

	@Override
	public License servicesOfGroup(String ouid) throws IOException, ParseException {
		List<MicroService> licensed = new LinkedList<>();

		List<Service> allServices = serviceRepo.listServicesFull();

		for (Service service : allServices)
			if (service.getAccessStrategy().getRequiredAttributes().get("ou") != null)
				for (Object name : (JSONArray) ((JSONArray) (service.getAccessStrategy().getRequiredAttributes().get("ou"))).get(1))
					if (ouid.equalsIgnoreCase(name.toString()))
						licensed.add(new MicroService(service));

		return new License(licensed, null);

	}

	@Override
	public License servicesOfUser(String userId) throws IOException, ParseException {
		List<MicroService> licensed = new LinkedList<>();
		List<MicroService> unLicensed = new LinkedList<>();

		List<Service> allServices = serviceRepo.listServicesFull();

		UsersExtraInfo user = mongoTemplate.findOne(new Query(Criteria.where("userId").is(userId)), UsersExtraInfo.class, Variables.col_usersExtraInfo);

		List<String> memberOf = null;
		try {
			memberOf = user.getMemberOf();

		} catch (NullPointerException e) {
		}

		for (Service service : allServices) {

			licensed = extract.licensedServicesForGroups(user, licensed, service);

			licensed = extract.licensedServicesForUserID(userId, licensed, service);

			List<List<MicroService>> temp = extract.unLicensedServicesForUserID(userId, licensed, unLicensed, service);

			licensed = temp.get(0);
			unLicensed = temp.get(1);

			temp = extract.licensedServiceWithGroupID(licensed, unLicensed, service, memberOf);

			licensed = temp.get(0);
			unLicensed = temp.get(1);

		}

		return new License(licensed, unLicensed);
	}

	@Override
	public Transcript usersAndGroupsOfService(long serviceId) throws IOException, ParseException {
		return new Transcript(usersLicense.users(serviceId), groupLicense.groups(serviceId));
	}

	@Override
	public List<ReportMessage> accessManaging(Long id, String type, String item) {
		Query query = new Query();

		query.addCriteria(Criteria.where("model").is(Variables.MODEL_SERVICE));
		query.addCriteria(Criteria.where("instance").is(id));
		query.addCriteria(Criteria.where("attribute").is(Variables.ACCESS_STRATEGY));
		if (!type.equals(""))
			query.addCriteria(Criteria.where("type").is(type));
		if (!item.equals(""))
			query.addCriteria(Criteria.where("item").is(item));

		return mongoTemplate.find(query, ReportMessage.class, Variables.col_idmanlog);

	}
}







