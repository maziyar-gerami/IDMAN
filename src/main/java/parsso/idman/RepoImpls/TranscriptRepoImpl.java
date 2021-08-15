package parsso.idman.RepoImpls;


import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import parsso.idman.Models.Logs.Transcript;
import parsso.idman.Models.Services.Service;
import parsso.idman.Models.Services.ServiceType.MicroService;
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

    @Override
    public Transcript servicesOfGroup(String ouid) throws IOException, ParseException {
        List<MicroService> licensed = new LinkedList<>();

        List<Service> allServices = serviceRepo.listServicesFull();

            for (Service service:allServices)
                if(service.getAccessStrategy().getRequiredAttributes().get("ou")!=null)
                    for (Object name: (JSONArray) ((JSONArray) (service.getAccessStrategy().getRequiredAttributes().get("ou"))).get(1))
                        if (ouid.equalsIgnoreCase(name.toString()))
                            licensed.add(new MicroService(service));

        return new Transcript(licensed,null);
    }

    @Override
    public Transcript servicesOfUser(String userId) throws IOException, ParseException {
        List<MicroService> licensed = new LinkedList<>();
        List<MicroService> unLicensed = new LinkedList<>();

        List<Service> allServices = serviceRepo.listServicesFull();

        for (Service service:allServices)
            if(service.getAccessStrategy().getRequiredAttributes().get("uid")!=null)
                for (Object name: (JSONArray) ((JSONArray) (service.getAccessStrategy().getRequiredAttributes().get("uid"))).get(1))
                    if (userId.equalsIgnoreCase(name.toString()))
                        licensed.add(new MicroService(service));

        for (Service service:allServices)
            if(service.getAccessStrategy().getRejectedAttributes().get("uid")!=null)

                for (Object name: (JSONArray) ((JSONArray) (service.getAccessStrategy().getRejectedAttributes().get("uid"))).get(1))
                if (userId.equalsIgnoreCase(name.toString()))
                    unLicensed.add(new MicroService(service));

        return new Transcript(licensed,unLicensed);
    }

}


