package parsso.idman.helpers.service;

import org.json.simple.JSONArray;
import org.springframework.stereotype.Component;
import parsso.idman.models.services.Service;
import parsso.idman.models.services.serviceType.MicroService;
import parsso.idman.models.users.UsersExtraInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
@SuppressWarnings({ "unchecked", "rawtypes" })
public class LicensedAndUnlicensed {
  public List<MicroService> licensedServicesForGroups(UsersExtraInfo user, List<MicroService> licensed,
      Service service) {
    Object ou = null;
    Object uid = null;
    try {
      ou = service.getAccessStrategy().getRequiredAttributes().get("ou");
    } catch (Exception ignored) {
    }
    try {
      uid = service.getAccessStrategy().getRequiredAttributes().get("uid");
    } catch (Exception ignored) {
    }

    try {
      if (uid != null && ou != null && user.getMemberOf() != null)
        for (String group : user.getMemberOf())
          if (((ArrayList) ((ArrayList) service.getAccessStrategy().getRequiredAttributes().get("ou")).get(1))
              .contains(group))
            licensed.add(new MicroService(service));
    } catch (Exception ignored) {
    }

    return licensed;
  }

  public List licensedServiceWithGroupID(List<MicroService> licensed, List<MicroService> unLicensed, Service service,
      List<String> memberOf) {

    if (memberOf != null)
      for (String groupStr : memberOf)
        if (service.getAccessStrategy().getRequiredAttributes() != null
            && service.getAccessStrategy().getRequiredAttributes().get("ou") != null)
          for (Object name : (JSONArray) ((JSONArray) (service.getAccessStrategy().getRequiredAttributes()
              .get("ou"))).get(1))
            if (groupStr.equalsIgnoreCase(name.toString()))
              if (!contains(unLicensed, service.getId())) {
                if (contains(licensed, service.getId()))
                  remove(licensed, service.getId());
                licensed.add(new MicroService(service));
              }

    LinkedList<Object> list = new LinkedList<>();
    list.add(licensed);
    list.add(unLicensed);

    return list;
  }

  public List unLicensedServicesForUserID(String userId, List<MicroService> licensed, List<MicroService> unLicensed,
      Service service) {
    if (service.getAccessStrategy().getRejectedAttributes().get("uid") != null)
      for (Object name : (JSONArray) ((JSONArray) (service.getAccessStrategy().getRejectedAttributes()
          .get("uid"))).get(1))
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
      for (Object name : (JSONArray) ((JSONArray) (service.getAccessStrategy().getRequiredAttributes()
          .get("uid"))).get(1))
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

  private void remove(List<MicroService> licensed, Long id) {

    licensed.removeIf(n -> (n.get_id() == id));

  }
}
