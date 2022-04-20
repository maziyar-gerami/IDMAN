package parsso.idman.helpers.user;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import parsso.idman.models.users.UsersExtraInfo;
import parsso.idman.repos.ServiceRepo;
import parsso.idman.repos.UserRepo;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings({"unchecked","rawtypes"})
public class UsersLicense {
    ServiceRepo.Retrieve serviceRepo;
    UserRepo.UsersOp.Retrieve usersOpRetrieve;


    public UsersLicense(ServiceRepo.Retrieve serviceRepo, UserRepo.UsersOp.Retrieve usersOpRetrieve) {
        this.serviceRepo = serviceRepo;
        this.usersOpRetrieve = usersOpRetrieve;
    }

    public List<UsersExtraInfo> licensedUsers(long serviceId) {
        List<UsersExtraInfo> users = new LinkedList<>();
        parsso.idman.models.services.Service service = serviceRepo.retrieveService(serviceId);

        if (service == null)
            return users;

        JSONArray jsonArray;
        if (service.getAccessStrategy().getRequiredAttributes().get("uid") != null) {
            jsonArray = (JSONArray) service.getAccessStrategy().getRequiredAttributes().get("uid");
            for (Object name : jsonArray)
                try {
                    users.add(new UsersExtraInfo(usersOpRetrieve.retrieveUsers(name.toString())));
                } catch (NullPointerException ignored) {
                }
        }

        if (users.size() == 0)
            return null;

        return users;
    }

    public List<UsersExtraInfo> unLicensedUsers(long serviceId) {
        List<UsersExtraInfo> uids = new LinkedList<>();
        parsso.idman.models.services.Service service = serviceRepo.retrieveService(serviceId);

        if (service == null)
            return null;

        JSONArray jsonArray = (JSONArray) ((JSONArray) (service.getAccessStrategy().getRejectedAttributes().get("uid"))).get(1);
        for (Object name : jsonArray)
            try {
                uids.add(new UsersExtraInfo(usersOpRetrieve.retrieveUsers(name.toString())));

            } catch (NullPointerException ignored) {
            }

        if (uids.size() == 0)
            return null;

        return uids;
    }

    public Users users(long serviceId) {
        List users = new LinkedList();
        users.add(licensedUsers(serviceId));
        users.add(unLicensedUsers(serviceId));

        return new Users(licensedUsers(serviceId), unLicensedUsers(serviceId));
    }

    @Setter
    @Getter
    private static class Users {
        @JsonInclude(JsonInclude.Include.NON_NULL)

        List<Object> licensed;
        @JsonInclude(JsonInclude.Include.NON_NULL)

        List<Object> unLicensed;

        Users(List licensed, List unLicensed) {
            this.licensed = licensed;
            this.unLicensed = unLicensed;
        }

    }
}
