package parsso.idman.backend.RepoImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import parsso.idman.backend.Models.OrganizationalUnit;
import parsso.idman.backend.Models.Person;
import parsso.idman.backend.Repos.OusRepo;
import parsso.idman.backend.Repos.PersonRepo;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchControls;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class OuRepoImpl implements OusRepo {

    public static final String BASE_DN = "";

    @Autowired
    private LdapTemplate ldapTemplate;


    @Override
    public String remove(String name) {
        Name dn = buildDn(name);
        System.out.println(dn.toString());
        ldapTemplate.unbind(dn);
        return name + " removed successfully";
    }

    @Override
    public OrganizationalUnit retrieveOu(String name) {

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<OrganizationalUnit> organizationalUnitList = ldapTemplate.search(query().where("ou").is(name),
                new OuRepoImpl.OUAttributeMapper());
        return organizationalUnitList.get(0);


    }

    private Attributes buildAttributes(OrganizationalUnit organizationalUnit) {

        BasicAttribute ocattr = new BasicAttribute("objectclass");
        ocattr.add("extensibleObject");
        ocattr.add("organizationalUnit");
        ocattr.add("top");

        Attributes attrs = new BasicAttributes();
        attrs.put(ocattr);
        attrs.put("ou", organizationalUnit.getName());
        return attrs;
    }

    public Name buildDn(String name) {
        return LdapNameBuilder.newInstance(BASE_DN).add("ou", "Groups").add("ou", name).build();
    }
    public Name buildBaseDn() {
        return LdapNameBuilder.newInstance(BASE_DN).add("ou", "Groups").build();
    }

    @Override
    public List<OrganizationalUnit> retrieve() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        List<OrganizationalUnit> ous = ldapTemplate.search(query().where("objectClass").is("extensibleObject"),
                new OuRepoImpl.OUAttributeMapper());
        return ous;
    }

    @Override
    public String create(OrganizationalUnit ou) {
        Name dn = buildDn(ou.getName());
        ldapTemplate.bind(dn, null, buildAttributes(ou));
        return ou.getName() + " created successfully";
    }

    @Override
    public String update(OrganizationalUnit ou) {
        Name dn = buildDn(ou.getName());
        ldapTemplate.rebind(dn, null, buildAttributes(ou));
        return ou.getName() + " updated successfully";
    }


    private class OUAttributeMapper implements AttributesMapper<OrganizationalUnit> {

        @Override
        public OrganizationalUnit mapFromAttributes(Attributes attributes) throws NamingException {
            OrganizationalUnit organizationalUnit = new OrganizationalUnit();
            organizationalUnit.setName(null != attributes.get("ou") ? attributes.get("ou").get().toString() : null);
            return organizationalUnit;
        }
    }
}
