package parsso.idman.helpers.user;


import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.Name;

public class BuildDnUser {
    String BASE_DN;

    public BuildDnUser(String BASE_DN) {
        this.BASE_DN = BASE_DN;
    }

    public Name buildDn(String userId) {
        return LdapNameBuilder.newInstance(BASE_DN).add("ou", "People").add("uid", userId).build();
    }

}
