package parsso.idman.RepoImpls.logs;


import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import parsso.idman.Helpers.Group.GroupLicense;
import parsso.idman.Helpers.Service.ExtractLicensedAndUnlicensed;
import parsso.idman.Helpers.User.UsersLicense;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.License.License;
import parsso.idman.Models.Services.Service;
import parsso.idman.Models.Services.ServiceType.MicroService;
import parsso.idman.Models.Users.UsersExtraInfo;
import parsso.idman.repos.LogsRepo;
import parsso.idman.repos.ServiceRepo;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Component
public class RetrieveTranscripts implements LogsRepo.TranscriptRepo {

    final ServiceRepo serviceRepo;

    final MongoTemplate mongoTemplate;
    final UsersLicense usersLicense;
    final parsso.idman.Helpers.Group.GroupLicense groupLicense;
    final ExtractLicensedAndUnlicensed extract;

    @Autowired
    public RetrieveTranscripts(ServiceRepo serviceRepo,
                               MongoTemplate mongoTemplate, UsersLicense usersLicense,
                               GroupLicense groupLicense, ExtractLicensedAndUnlicensed extract) {
        this.serviceRepo = serviceRepo;
        this.mongoTemplate = mongoTemplate;
        this.usersLicense = usersLicense;
        this.groupLicense = groupLicense;
        this.extract = extract;

    }

    @Override
    public License servicesOfGroup(String ouID) {
        List<MicroService> licensed = new LinkedList<>();

        List<Service> allServices = serviceRepo.listServicesFull();

        for (Service service : allServices)
            if (service.getAccessStrategy().getRequiredAttributes().get("ou") != null)
                for (Object name : (JSONArray) ((JSONArray) (service.getAccessStrategy().getRequiredAttributes().get("ou"))).get(1))
                    if (ouID.equalsIgnoreCase(name.toString()))
                        licensed.add(new MicroService(service));

        return new License(licensed, null);

    }

    @Override
    public License servicesOfUser(String userId) {
        List<MicroService> licensed = new LinkedList<>();
        List<MicroService> unLicensed = new LinkedList<>();

        List<Service> allServices = serviceRepo.listServicesFull();

        UsersExtraInfo user = mongoTemplate.findOne(new Query(Criteria.where("userId").is(userId)), UsersExtraInfo.class, Variables.col_usersExtraInfo);

        List<String> memberOf;
        try {
            memberOf = Objects.requireNonNull(user).getMemberOf();

        } catch (NullPointerException e) {
            memberOf = null;
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

}







