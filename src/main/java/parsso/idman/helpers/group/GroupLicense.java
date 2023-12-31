package parsso.idman.helpers.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import parsso.idman.models.groups.Group;
import parsso.idman.repos.GroupRepo;
import parsso.idman.repos.ServiceRepo;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings({ "rawtypes" })
public class GroupLicense {
  GroupRepo.Retrieve groupRepoRetrieve;
  ServiceRepo.Retrieve serviceRepo;

  public GroupLicense(GroupRepo.Retrieve groupRepoRetrieve, ServiceRepo.Retrieve serviceRepo) {
    this.groupRepoRetrieve = groupRepoRetrieve;
    this.serviceRepo = serviceRepo;
  }

  public List<Group> licensedGroups(long serviceId) {
    List<Group> groups = new LinkedList<>();

    parsso.idman.models.services.Service service = serviceRepo.retrieveService(serviceId);
    JSONArray jsonArray = (JSONArray) ((JSONArray) (service.getAccessStrategy().getRequiredAttributes().get("ou")))
        .get(1);
    for (Object name : jsonArray)
      try {
        groups.add(groupRepoRetrieve.retrieve(true, name.toString()));
      } catch (NullPointerException ignored) {
      }
    return groups;
  }

  public Groups groups(long serviceId) {

    return new Groups(licensedGroups(serviceId));
  }

  @Setter
  @Getter
  private static class Groups {
    @JsonInclude(JsonInclude.Include.NON_NULL)

    List licensed;
    @JsonInclude(JsonInclude.Include.NON_NULL)

    List unLicensed;

    Groups(List licensed) {
      this.licensed = licensed;
      this.unLicensed = null;
    }

  }

}
