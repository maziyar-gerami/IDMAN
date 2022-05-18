package parsso.idman.impls.groups;

import javax.naming.directory.SearchControls;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import net.minidev.json.JSONArray;
import parsso.idman.impls.groups.helper.OUAttributeMapper;
import parsso.idman.models.groups.Group;
import parsso.idman.models.license.License;
import parsso.idman.models.services.serviceType.MicroService;
import parsso.idman.models.users.User;
import parsso.idman.repos.GroupRepo;
import parsso.idman.repos.ServiceRepo;

@Service
public class RetrieveGroup implements GroupRepo.Retrieve {
  final LdapTemplate ldapTemplate;
  final MongoTemplate mongoTemplate;
  final ServiceRepo serviceRepo;
  @Value("${spring.ldap.base.dn}")
  protected String BASE_DN;

  @Autowired
  public RetrieveGroup(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate, ServiceRepo serviceRepo) {
    this.ldapTemplate = ldapTemplate;
    this.mongoTemplate = mongoTemplate;
    this.serviceRepo = serviceRepo;
  }

  public Group retrieve(boolean simple, String uid) {

    for (Group group : retrieve()) {
      if (!simple && group.getId().equalsIgnoreCase(uid)) {
        group.setService(servicesOfGroup(uid));
        return group;
      } else if (simple && group.getId().equalsIgnoreCase(uid))
        return group;
    }
    return null;
  }
  public License servicesOfGroup(String ouID) {
    List<MicroService> licensed = new LinkedList<>();

    List<parsso.idman.models.services.Service> allServices = serviceRepo.listServicesFull();

    for (parsso.idman.models.services.Service service : allServices)
      if (service.getAccessStrategy().getRequiredAttributes().get("ou") != null)
        for (Object name : (JSONArray) ((JSONArray) (service.getAccessStrategy().getRequiredAttributes()
            .get("ou"))).get(1))
          if (ouID.equalsIgnoreCase(name.toString()))
            licensed.add(new MicroService(service));

    return new License(licensed, null);

  }


  public List<Group> retrieve(User user) {
    List<Group> groups = new ArrayList<>();
    try {
      for (String s : user.getMemberOf()) {
        groups.add(retrieve(false, s));
      }
    } catch (NullPointerException ignored) {

    }
    return groups;
  }

  public List<Group> retrieve() {
    SearchControls searchControls = new SearchControls();
    searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

    final AndFilter filter = new AndFilter();
    filter.and(new EqualsFilter("objectclass", "organizationalUnit"));

    return ldapTemplate.search("ou=groups," + BASE_DN, filter.encode(),
        new OUAttributeMapper(mongoTemplate));
  }
}
