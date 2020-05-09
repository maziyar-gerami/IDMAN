package parsso.idman.backend.RepoImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import parsso.idman.backend.Models.Person;
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
public class PersonRepoImpl implements PersonRepo {

    public static final String BASE_DN = "";

    @Autowired
    private LdapTemplate ldapTemplate;

    @Override
    public String create(Person p) {
        Name dn = buildDn(p.getUserId());
        ldapTemplate.bind(dn, null, buildAttributes(p));
        return p.getUserId() + " created successfully";
    }

    @Override
    public String update(Person p) {
        Name dn = buildDn(p.getUserId());
        ldapTemplate.rebind(dn, null, buildAttributes(p));
        return p.getUserId() + " updated successfully";
    }

    @Override
    public String remove(String userId) {
        Name dn = buildDn(userId);
        ldapTemplate.unbind(dn);
        return userId + " removed successfully";
    }

    private Attributes buildAttributes(Person p) {

        BasicAttribute ocattr = new BasicAttribute("objectclass");
        ocattr.add("top");
        ocattr.add("person");
        ocattr.add("inetOrgPerson");
        ocattr.add("organizationalPerson");

        Attributes attrs = new BasicAttributes();
        attrs.put(ocattr);
        attrs.put("uid", p.getUserId());
        attrs.put("givenName", p.getFirstName());
        attrs.put("sn", p.getLastName());
        attrs.put("displayName", p.getDisplayName());
        attrs.put("telephoneNumber", p.getTelephoneNumber());
        attrs.put("mail", p.getMail());
        attrs.put("cn", p.getNid());
        //attrs.put("memberOf",p.getMemberOf());
        attrs.put("userPassword", "{SSHA}" + "secret");
        attrs.put("description", p.getDescription());
        return attrs;
    }

    public Name buildDn(String userId) {
        return LdapNameBuilder.newInstance(BASE_DN).add("ou", "people").add("uid", userId).build();
    }
    public Name buildBaseDn() {
        return LdapNameBuilder.newInstance(BASE_DN).add("ou", "people").build();
    }
    @Override
    public List<Person> retrieve() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<Person> people = ldapTemplate.search(query().where("objectClass").is("person"),
                new PersonAttributeMapper());
        return people;
    }

    @Override
    public Person retrievePerson(String userId) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
         Person person = ldapTemplate.search(query().where("uid").is(userId), new PersonAttributeMapper()).get(0);
        return person;
    }

    private class PersonAttributeMapper implements AttributesMapper<Person> {

        @Override
        public Person mapFromAttributes(Attributes attributes) throws NamingException {
            Person person = new Person();
            person.setUserId(null != attributes.get("uid") ? attributes.get("uid").get().toString() : null);
            person.setFirstName(null != attributes.get("givenName") ? attributes.get("givenName").get().toString() : null);
            person.setLastName(null != attributes.get("sn") ? attributes.get("sn").get().toString() : null);
            person.setDisplayName(null != attributes.get("displayName") ? attributes.get("displayName").get().toString() : null);
            person.setTelephoneNumber(null != attributes.get("telephoneNumber") ? attributes.get("telephoneNumber").get().toString() : null);
            person.setMail(null != attributes.get("mail") ? attributes.get("mail").get().toString() : null);
            person.setNid(null != attributes.get("cn") ? attributes.get("cn").get().toString() : null);
            person.setMemberOf(null != attributes.get("member") ? attributes.get("member").get().toString() : null);
            person.setUserPassword(null != attributes.get("userPassword")? attributes.get("userPassword").get().toString():null);
            person.setDescription(null != attributes.get("description") ? attributes.get("description").get().toString() : null);
            return person;
        }
    }
}
