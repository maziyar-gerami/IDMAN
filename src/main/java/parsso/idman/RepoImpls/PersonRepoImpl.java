package parsso.idman.RepoImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import parsso.idman.Configs.MyConstants;
import parsso.idman.Email.EmailSend;
import parsso.idman.Models.Person;
import parsso.idman.Repos.PersonRepo;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class PersonRepoImpl implements PersonRepo {

    public static final String BASE_DN = "";
    String currentUserId;

    @Autowired
    private LdapTemplate ldapTemplate;

    //PersonRepoImpl(){
    //    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    //    if (principal instanceof UserDetails) {
    //        String currentUserId = ((UserDetails)principal).getUsername();
    //    } else {
    //        String currentUserId = principal.toString();
    //    }

    //}

    @Override
    public String create(Person p) {

        Person person = retrievePerson(p.getUserId());

        if (person.getUserId() == null) {
            Name dn = buildDn(p.getUserId());
            ldapTemplate.bind(dn, null, buildAttributes(p));
            return p.getUserId() + " created successfully";
        } else {
            update(p);
            return p.getUserId() + " was found and existing record updated";
        }
    }

    @Override
    public String update(Person p) {
        Name dn = buildDn(p.getUserId());
        //TODO: see below. build attribute is 1
        ldapTemplate.rebind(dn, null, buildAttributes(p));
        return p.getUserId() + " updated successfully";
    }


    @Override
    public String remove(String userId) {
        Name dn = buildDn(String.valueOf(userId));
        ldapTemplate.unbind(dn);
        return userId + " removed successfully";
    }

    @Override
    public String remove() {
        List<Person> people = retrieve();
        for (Person person : people) {
            Name dn = buildDn(person.getUserId());
            ldapTemplate.unbind(dn);
        }

        return "All users removed successfully";
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
        attrs.put("cn", p.getFirstName() + ' ' + p.getLastName());
        if (p.getUserPassword() != null)
            attrs.put("userPassword", p.getUserPassword());
        System.out.println("**************");
        if (p.getToken() != null) {
            attrs.put("l", p.getToken());
            System.out.println(p.getToken());

        }
        if (p.getMemberOf() != null) {
            Attribute attr = new BasicAttribute("ou");
            for (int i = 0; i < p.getMemberOf().size(); i++)
                attr.add(p.getMemberOf().get(i));
            attrs.put(attr);
        }

        attrs.put("description", p.getDescription());
        return attrs;
    }

    public Name buildDn(String userId) {
        return LdapNameBuilder.newInstance(BASE_DN).add("ou", "People").add("uid", userId).build();
    }

    public Name buildGroupDn(String groupID) {
        return LdapNameBuilder.newInstance(BASE_DN).add("ou", "Groups").add("ou", groupID).build();
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
        Person person = new Person();
        if (!((ldapTemplate.search(query().where("uid").is(userId), new PersonAttributeMapper())).toString() == "[]"))

            person = ldapTemplate.search(query().where("uid").is(userId), new PersonAttributeMapper()).get(0);
        return person;
    }

    @Override
    public Person checkMail(String email) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        Person person = new Person();
        List<Person> people = ldapTemplate.search(query().where("mail").is(email), new PersonAttributeMapper());
        System.out.println(people.toArray());
        if (people.toString() == "[]") {
            return null;
        }
        else {
            return (people.get(0));
        }

    }

    @Override
    public String sendEmail(String email) {
        if (checkMail(email) != null) {
            Person person = checkMail(email);
            String token = insertToken(person);
            EmailSend emailSend = new EmailSend();


            String fullUrl = createUrl(MyConstants.baseUrl,MyConstants.sendEmailController,person.getUserId(), person.getToken());

            emailSend.sendMail(email, "\n"+ fullUrl);
            return "Email sent";
        } else
            return "Email Not found";
    }

    private String createUrl(String BaseUrl, String controllerAddress, String userId, String token){

        return BaseUrl + "/idman" + controllerAddress + userId + "/" + token.toString();

    }

    @Override
    public Person checkToken(String userId, String token) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        Person person = new Person();

        person = ldapTemplate.search(query().where("uid").is(userId), new PersonAttributeMapper()).get(0);

        //person = people.get(0);

        String tok = person.getToken().toString();

        if (token.equals(tok)) {
            return person;
        } else
            return null;

    }


    private String passwordResetToken(String userId) {

        Date date = new Date();

        return UUID.randomUUID().toString().toUpperCase()
                + userId
                + date.getTime();
    }

    public String insertToken(Person person) {
        String token = passwordResetToken(person.getUserId());
        person.setToken(token);
        Name dn = buildDn(person.getUserId());
        ldapTemplate.rebind(dn, null, buildAttributes(person));

        return "Token for " + person.getUserId() + " is created";
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
            int nGroups;
            if (attributes.get("ou") == null)
                nGroups = 0;
            else
                nGroups = attributes.get("ou").size();

            List<String> ls = new LinkedList<>();
            for (int i = 0; i < nGroups; i++)
                ls.add(attributes.get("ou").get(i).toString());
            person.setToken(null != attributes.get("l") ? attributes.get("l").get().toString() : null);
            person.setMemberOf(null != attributes.get("ou") ? ls : null);
            person.setUserPassword(null != attributes.get("userPassword") ? attributes.get("userPassword").get().toString() : null);
            person.setDescription(null != attributes.get("description") ? attributes.get("description").get().toString() : null);
            return person;
        }
    }

    public String updatePass(String userId, String pass, String token) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        Person person = new Person();
        person = ldapTemplate.search(query().where("uid").is(userId), new PersonAttributeMapper()).get(0);
        
        if (token.equals(person.getToken())) {
            person.setUserPassword(pass);
            Name dn = buildDn(person.getUserId());
            ldapTemplate.rebind(dn, null, buildAttributes(person));
            return userId + " passwords was updated";
        } else {
            return "userId or token was incorrect";
        }
    }
}
