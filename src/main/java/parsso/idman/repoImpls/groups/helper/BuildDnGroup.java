package parsso.idman.repoImpls.groups.helper;

import org.springframework.ldap.support.LdapNameBuilder;

import javax.naming.Name;

public class BuildDnGroup {

    final String BASE_DN;

    public BuildDnGroup(String BASE_DN) {
        this.BASE_DN = BASE_DN;
    }

    public Name buildDn(String id) {
        return LdapNameBuilder.newInstance("ou=" + "groups" + "," + BASE_DN).add("ou", id).build();
    }
}
