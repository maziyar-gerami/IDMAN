package parsso.idman.impls.users.supplementary.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import parsso.idman.configs.Prefs;
import parsso.idman.helpers.Variables;
import parsso.idman.repos.users.oprations.sub.UsersRetrieveRepo;

@Service
public class Authenticate {
  final LdapTemplate ldapTemplate;
  final UsersRetrieveRepo usersOpRetrieve;

  @Autowired
  public Authenticate(LdapTemplate ldapTemplate, UsersRetrieveRepo usersOpRetrieve) {
    this.ldapTemplate = ldapTemplate;
    this.usersOpRetrieve = usersOpRetrieve;
  }

  public int authenticate(String userId, String password) {
    AndFilter andFilter = new AndFilter();
    andFilter.and(new EqualsFilter("objectclass", "person"));
    andFilter.and(new EqualsFilter("uid", userId));

    if (ldapTemplate.authenticate("ou=People," + Prefs.get(Variables.PREFS_BASE_DN), andFilter.toString(), password)) {
      if (userId.equals("su") || usersOpRetrieve.retrieveUserMain(userId).isLoggedIn())
        return 1;
      else
        return 2;
    } else
      return 0;
  }
}
