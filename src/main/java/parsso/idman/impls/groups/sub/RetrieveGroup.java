package parsso.idman.impls.groups.sub;

import javax.naming.directory.SearchControls;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;

import parsso.idman.configs.Prefs;
import parsso.idman.helpers.Variables;
import parsso.idman.impls.groups.helper.OUAttributeMapper;
import parsso.idman.impls.services.ServicesGroup;
import parsso.idman.models.groups.Group;
import parsso.idman.models.users.User;

public class RetrieveGroup {
  final LdapTemplate ldapTemplate;
  final MongoTemplate mongoTemplate;

  public RetrieveGroup(LdapTemplate ldapTemplate, MongoTemplate mongoTemplate) {
    this.ldapTemplate = ldapTemplate;
    this.mongoTemplate = mongoTemplate;
    
  }

  public Group retrieve(boolean simple, String uid) {

    for (Group group : retrieve()) {
      if (!simple && group.getId().equalsIgnoreCase(uid)) {
        group.setService(new ServicesGroup(mongoTemplate).servicesOfGroup(uid));
        return group;
      } else if (simple && group.getId().equalsIgnoreCase(uid))
        return group;
    }
    return null;
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

    return ldapTemplate.search("ou=groups," + Prefs.get(Variables.PREFS_BASE_DN), filter.encode(),
        new OUAttributeMapper(mongoTemplate));
  }
}
