package parsso.idman.impls.users.oprations.retrieve.helper;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;

import parsso.idman.configs.Prefs;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.SimpleUserAttributeMapper;

import javax.naming.directory.SearchControls;

public class UsersCount {
  MongoTemplate mongoTemplate;
  LdapTemplate ldapTemplate;

  public UsersCount(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public UsersCount(LdapTemplate ldapTemplate) {
    this.ldapTemplate = ldapTemplate;
  }

  public int mongoSize(String groupFilter, String searchUid, String searchDisplayName, String mobile,
      String userStatus) {

    return (int) mongoTemplate.count(
        MongoQuery.builder(groupFilter, searchUid, searchDisplayName, mobile, userStatus),
        Variables.col_usersExtraInfo);
  }

  public int ldapSize() {
    SearchControls searchControls = new SearchControls();
    searchControls.setReturningAttributes(new String[] { "*", "+" });
    searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

    return ldapTemplate.search("ou=People," + Prefs.get(Variables.PREFS_BASE_DN), new EqualsFilter("objectClass", "person").encode(),
        searchControls, new SimpleUserAttributeMapper()).size();

  }

}
