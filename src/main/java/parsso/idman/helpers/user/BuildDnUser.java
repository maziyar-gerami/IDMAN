package parsso.idman.helpers.user;

import org.springframework.ldap.support.LdapNameBuilder;

import parsso.idman.configs.Prefs;
import parsso.idman.helpers.Variables;

import javax.naming.Name;

public class BuildDnUser {

  public static Name buildDn(String userId) {
    return LdapNameBuilder.newInstance(Prefs.get(Variables.PREFS_BASE_DN)).add("ou", "People").add("uid", userId)
        .build();
  }

}
