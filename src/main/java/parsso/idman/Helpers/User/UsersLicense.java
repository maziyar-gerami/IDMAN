package parsso.idman.Helpers.User;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import parsso.idman.Models.Users.UsersExtraInfo;
import parsso.idman.Repos.ServiceRepo;
import parsso.idman.Repos.UserRepo;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Component
public class UsersLicense {
    @Autowired
    ServiceRepo serviceRepo;
    @Autowired
    UserRepo userRepo;

    public List<UsersExtraInfo> licensedUsers(long serviceId) throws IOException, ParseException {
        List<UsersExtraInfo> users = new LinkedList<>();
        parsso.idman.Models.Services.Service service = serviceRepo.retrieveService(serviceId);

        if (service == null)
            return users;

        JSONArray jsonArray;
        if((JSONArray) ((JSONArray) (service.getAccessStrategy().getRequiredAttributes().get("uid")))!=null){
        jsonArray = (JSONArray) ((JSONArray) (service.getAccessStrategy().getRequiredAttributes().get("uid")));
        for (Object name : jsonArray)
            try {
                users.add(new UsersExtraInfo(userRepo.retrieveUsers(name.toString())));
            } catch (NullPointerException e){}
        }

        if(users.size()==0)
            return null;

        return users;
    }

    public List<UsersExtraInfo> unLicensedUsers(long serviceId) throws IOException, ParseException {
        List<UsersExtraInfo> uids = new LinkedList<>();
        parsso.idman.Models.Services.Service service = serviceRepo.retrieveService(serviceId);

        if (service == null)
            return null;

        JSONArray jsonArray = (JSONArray) ((JSONArray) (service.getAccessStrategy().getRejectedAttributes().get("uid"))).get(1);
        for (Object name : jsonArray)
            try {
                uids.add(new UsersExtraInfo(userRepo.retrieveUsers(name.toString())));

            } catch (NullPointerException e) { }

        if(uids.size()==0)
            return null;

        return uids;
    }

    public Users users(long serviceId) throws IOException, ParseException {
        List users = new LinkedList();
        users.add(licensedUsers(serviceId));
        users.add(unLicensedUsers(serviceId));

        return new Users(licensedUsers(serviceId), unLicensedUsers(serviceId));
    }

    @Setter
    @Getter
    private class Users{
        @JsonInclude(JsonInclude.Include.NON_NULL)

        List licensed;
        @JsonInclude(JsonInclude.Include.NON_NULL)

        List unLicensed;

        Users(List licensed,List unLicensed){
            this.licensed = licensed;
            this.unLicensed = unLicensed;
        }

    }
}
