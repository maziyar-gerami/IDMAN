package parsso.idman.impls.users.supplementary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;
import parsso.idman.repos.UserRepo;

@Service
public class Authenticate {
  final LdapTemplate ldapTemplate;
  final UserRepo.UsersOp.Retrieve usersOpRetrieve;
  @Value("${spring.ldap.base.dn}")
  private String BASE_DN;

  @Autowired
  public Authenticate(LdapTemplate ldapTemplate, UserRepo.UsersOp.Retrieve usersOpRetrieve) {
    this.ldapTemplate = ldapTemplate;
    this.usersOpRetrieve = usersOpRetrieve;
  }

  public int authenticate(String userId, String password) {
    AndFilter andFilter = new AndFilter();
    andFilter.and(new EqualsFilter("objectclass", "person"));
    andFilter.and(new EqualsFilter("uid", userId));

    if (ldapTemplate.authenticate("ou=People," + BASE_DN, andFilter.toString(), password)) {
      if (usersOpRetrieve.retrieveUserMain(userId).isLoggedIn())
        return 1;
      else
        return 2;
    } else
      return 0;
  }
}
