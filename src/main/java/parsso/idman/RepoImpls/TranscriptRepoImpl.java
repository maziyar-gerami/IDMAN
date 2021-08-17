package parsso.idman.RepoImpls;


import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import parsso.idman.Helpers.User.UsersLicense;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.License.License;
import parsso.idman.Models.Logs.Transcript;
import parsso.idman.Models.Services.Service;
import parsso.idman.Models.Services.ServiceType.MicroService;
import parsso.idman.Models.Users.UsersExtraInfo;
import parsso.idman.Repos.GroupRepo;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.Repos.TranscriptRepo;
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

    @Override
    public License servicesOfGroup(String ouid) throws IOException, ParseException {
        List<MicroService> licensed = new LinkedList<>();

        List<Service> allServices = serviceRepo.listServicesFull();

        for (Service service:allServices)
            if(service.getAccessStrategy().getRequiredAttributes().get("ou")!=null)
                for (Object name: (JSONArray) ((JSONArray) (service.getAccessStrategy().getRequiredAttributes().get("ou"))).get(1))
                    if (ouid.equalsIgnoreCase(name.toString()))
                        licensed.add(new MicroService(service));


        return new License(licensed,null);

    }

    @Override
    public License servicesOfUser(String userId) throws IOException, ParseException {
        List<MicroService> licensed = new LinkedList<>();
        List<MicroService> unLicensed = new LinkedList<>();

        List<Service> allServices = serviceRepo.listServicesFull();

        UsersExtraInfo user = mongoTemplate.findOne(new Query(), UsersExtraInfo.class, Variables.col_usersExtraInfo);

        List<String> memberOf = user.getMemberOf();

        for (Service service:allServices) {
            if (service.getAccessStrategy().getRequiredAttributes().get("uid") != null)
                for (Object name : (JSONArray) ((JSONArray) (service.getAccessStrategy().getRequiredAttributes().get("uid"))).get(1))
                    if (userId.equalsIgnoreCase(name.toString()))
                        licensed.add(new MicroService(service));

            if (service.getAccessStrategy().getRejectedAttributes().get("uid") != null)
                for (Object name : (JSONArray) ((JSONArray) (service.getAccessStrategy().getRejectedAttributes().get("uid"))).get(1))
                    if (userId.equalsIgnoreCase(name.toString()))
                        unLicensed.add(new MicroService(service));
        }

                    for (Service service:allServices) {
                        for (String groupStr : memberOf)
                            if (service.getAccessStrategy().getRequiredAttributes() != null && service.getAccessStrategy().getRequiredAttributes().get("ou") != null)
                                for (Object name : (JSONArray) ((JSONArray) (service.getAccessStrategy().getRequiredAttributes().get("ou"))).get(1))
                                    if (groupStr.equalsIgnoreCase(name.toString()))
                                        if (!contains(unLicensed,service.getId()) && !contains(licensed,service.getId()))
                                            licensed.add(new MicroService(service));
                    }

        return new License(licensed,  unLicensed);
    }

    private boolean contains(List<MicroService> microServices, Long id){
        for (MicroService microservice:microServices)
            if (microservice.get_id() == id)
                return true;

            return false;
    }

    @Override
    public Transcript usersAndGroupsOfService(long serviceId) throws IOException, ParseException {
        return new Transcript(usersLicense.users(serviceId),groupLicense.groups(serviceId));
    }
}


/*

    @Override



    @Override
    public Transcript licensed(String userId) throws IOException, ParseException {

        List<Long> licensedServiceId = licensedService(userId);
        List<NanoService> licensedService = new LinkedList<>();

        for (long s : licensedServiceId)
            licensedService.add(new NanoService(s, serviceRepo.retrieveService(s).getName()));

        return new Transcript(null, null, licensedService);
    }

    @Override
    public Transcript unLicensed(String userId) throws IOException, ParseException {
        List<Long> serviceList = licensedService(userId);
        List<NanoService> resultService = new LinkedList<>();
        for (Service service : serviceRepo.listServicesFull())
            if (!serviceList.contains(service.getId()))
                resultService.add(new NanoService(service.getId(), service.getName()));


        return new Transcript(null, null, resultService);
    }

    List<Long> licensedService(String userId) throws IOException, ParseException {
        List<MicroService> s = serviceRepo.listUserServices(userRepo.retrieveUsers(userId));
        List<Long> serviceList = new LinkedList();
        for (MicroService microservice : s)
            serviceList.add(microservice.get_id());

        return serviceList;
    }

    @Setter
    @Getter
    class NanoService {
        long id;
        String name;

        public NanoService(long serviceId, String serviceName) {
            this.id = serviceId;
            this.name = serviceName;
        }
    }

    @Setter
    @Getter
    class NanoUser {
        String id;
        String name;

        public NanoUser(String userId, String displayName) {
            this.id = userId;
            this.name = displayName;
        }
    }

    @Setter
    @Getter
    class NanoOU {
        String id;
        String name;

        public NanoOU(String ouid, String groupName) {
            this.id = ouid;
            this.name = groupName;
        }
    }
*/




