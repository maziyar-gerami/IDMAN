package parsso.idman.RepoImpls.transcripts;


import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import parsso.idman.Helpers.Service.ExtractLicensedAndUnlicensed;
import parsso.idman.Helpers.TimeHelper;
import parsso.idman.Helpers.User.UsersLicense;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.License.License;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.Logs.Transcript;
import parsso.idman.Models.Services.Service;
import parsso.idman.Models.Services.ServiceType.MicroService;
import parsso.idman.Models.Users.UsersExtraInfo;
import parsso.idman.Models.other.Time;
import parsso.idman.Repos.GroupRepo;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.Repos.UserRepo;
import parsso.idman.Repos.logs.transcripts.TranscriptRepo;

import java.io.IOException;
import java.time.ZoneId;
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
	public List<ReportMessage> accessManaging(int page, int nRows, long id, String date, String doerId, String instanceName) throws java.text.ParseException {
		int skip;

		Query query = new Query();
		if(id !=0)
			query.addCriteria(Criteria.where("instance").is(id));
		if(!doerId.equals(""))
			query.addCriteria(Criteria.where("doerID").is(doerId));
		if (!instanceName.equals(""))
			query.addCriteria(Criteria.where("instanceName").is(instanceName));
		if (!date.equals("")){
			Time time = new Time(Integer.parseInt(date.substring(4)),
					Integer.parseInt(date.substring(2, 4)),
					Integer.parseInt(date.substring(0, 2)));
			long[] range = TimeHelper.specificDateToEpochRange(time, ZoneId.of(Variables.ZONE));
			query.addCriteria(Criteria.where("_id").gte(range[0]).lte(range[1]));
		}
		if (page!=0 && nRows !=0) {
			skip = (page - 1) * nRows;
			query.skip(skip).limit(nRows);
		}

		return mongoTemplate.find(query, ReportMessage.class, Variables.col_idmanlog);
	}
}







