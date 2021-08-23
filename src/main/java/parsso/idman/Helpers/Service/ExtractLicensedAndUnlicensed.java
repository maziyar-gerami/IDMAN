package parsso.idman.Helpers.Service;


import org.json.simple.JSONArray;
import org.springframework.stereotype.Component;
import parsso.idman.Models.Services.Service;
import parsso.idman.Models.Services.ServiceType.MicroService;
import parsso.idman.Models.Users.UsersExtraInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
public class ExtractLicensedAndUnlicensed {
    public List<MicroService> licensedServicesForGroups(UsersExtraInfo user, List<MicroService> licensed, Service service) {
        Object ou = null;
        Object uid = null;
        try {
            ou = service.getAccessStrategy().getRequiredAttributes().get("ou");
        } catch (Exception e) {
        }
        try {
            uid = service.getAccessStrategy().getRequiredAttributes().get("uid");
        } catch (Exception e) {
        }

        if (uid != null && ou != null)
            for (String group:user.getMemberOf())
                if(((ArrayList)((ArrayList) service.getAccessStrategy().getRequiredAttributes().get("ou")).get(1)).contains(group))
                    licensed.add(new MicroService(service));



        return licensed;
    }

    public List<List<MicroService>> licensedServiceWithGroupID(List<MicroService> licensed, List<MicroService> unLicensed, Service service, List<String> memberOf) {

        for (String groupStr : memberOf)
            if (service.getAccessStrategy().getRequiredAttributes() != null && service.getAccessStrategy().getRequiredAttributes().get("ou") != null)
                for (Object name : (JSONArray) ((JSONArray) (service.getAccessStrategy().getRequiredAttributes().get("ou"))).get(1))
                    if (groupStr.equalsIgnoreCase(name.toString()))
                        if (!contains(unLicensed, service.getId())) {
                            if (contains(licensed, service.getId()))
                                remove(licensed, service.getId());
                            licensed.add(new MicroService(service));
                        }

        List list = new LinkedList();
        list.add(licensed);
        list.add(unLicensed);

        return list;
    }

    public List<List<MicroService>> unLicensedServicesForUserID(String userId, List<MicroService> licensed, List<MicroService> unLicensed, Service service) {
        if (service.getAccessStrategy().getRejectedAttributes().get("uid") != null)
            for (Object name : (JSONArray) ((JSONArray) (service.getAccessStrategy().getRejectedAttributes().get("uid"))).get(1))
                if (userId.equalsIgnoreCase(name.toString())) {
                    if (contains(licensed, service.getId()))
                        remove(licensed, service.getId());
                    unLicensed.add(new MicroService(service));

                }
        List list = new LinkedList();
        list.add(licensed);
        list.add(unLicensed);

        return list;
    }

    public List<MicroService> licensedServicesForUserID(String userId, List<MicroService> licensed, Service service) {

        if (service.getAccessStrategy().getRequiredAttributes().get("uid") != null)
            for (Object name : (JSONArray) ((JSONArray) (service.getAccessStrategy().getRequiredAttributes().get("uid"))).get(1))
                if (userId.equalsIgnoreCase(name.toString()) && !contains(licensed, service.getId()))
                    licensed.add(new MicroService(service));

        return licensed;
    }

    private boolean contains(List<MicroService> microServices, Long id) {
        for (MicroService microservice : microServices)
            if (microservice.get_id() == id)
                return true;

        return false;
    }

    private List<MicroService> remove(List<MicroService> licensed, Long id) {

        licensed.removeIf(n -> (n.get_id() == id));

        return licensed;
    }
}
