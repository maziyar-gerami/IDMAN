package parsso.idman.RepoImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Group;
import parsso.idman.Models.User;
import parsso.idman.Repos.GroupRepo;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchControls;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class GroupRepoImpl implements GroupRepo {

    //public static final String BASE_DN = "dc=partition1,dc=com";;

    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;

    @Autowired
    private LdapTemplate ldapTemplate;

    @Override
    public String remove(String id) {

        Name dn = buildDn(id);
        ldapTemplate.unbind(dn);
        return id + " removed successfully";
    }

    @Override
    public String remove() {

        List <Group> allous = retrieve();
        for (Group ou: allous) {
            Name dn = buildDn(ou.getId());
            ldapTemplate.unbind(dn);

        }

        return "All Groups removed successfully";
    }



    @Override
    public Group retrieveOu(String uid) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        List<Group> groups= retrieve();

        for (Group group:groups ) {
            if (group.getId().equals(uid))
                return group;
        }
        return null;
    }

    @Override
    public Group retrieveOu() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<Group> groupList = ldapTemplate.search(query().where("objectClass").is("extensibleObject"),
                new OUAttributeMapper());
        return groupList.get(0);
    }

    @Override
    public List<Group> retrieveCurrentUserGroup(User user) {
        List<String> memberOf = user.getMemberOf();
        List<Group> groups = new ArrayList<Group>();
        try{
            for(int i = 0; i < memberOf.size(); ++i){
                groups.add(retrieveOu(memberOf.get(i)));
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return groups;
    }

    private Attributes buildAttributes(String uid, Group group) {

        Date date = new Date();

        BasicAttribute ocattr = new BasicAttribute("objectclass");
        ocattr.add("extensibleObject");
        ocattr.add("organizationalUnit");
        ocattr.add("top");

        Attributes attrs = new BasicAttributes();
        attrs.put(ocattr);
        attrs.put("name", group.getName());
        attrs.put("ou",uid);
        attrs.put("description" , group.getDescription());
        return attrs;
    }

    public Name buildDn(String  id) {
        return LdapNameBuilder.newInstance(BASE_DN).add("ou", "Groups").add("ou", id).build();
    }
    public Name buildBaseDn() {
        return LdapNameBuilder.newInstance(BASE_DN).add("ou", "Groups").build();
    }

    @Override
    public List<Group> retrieve() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        final AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "extensibleObject"));

        return ldapTemplate.search(BASE_DN, filter.encode(),
                new OUAttributeMapper());

    }

    @Override
    public String create(Group ou) {
        Date date = new Date();
        String timestamp = String.valueOf(date.getTime());
        Name dn = buildDn(timestamp);
        ldapTemplate.bind(dn, null, buildAttributes(timestamp, ou));
        return ou.getName() + " created successfully";
    }

    @Override
    public String update(String id, Group ou) {
        Name dn = buildDn(id);

        Group group = retrieveOu(id);

        ldapTemplate.rebind(dn, null, buildAttributes(group.getId(),ou));
        return ou.getName() + " updated successfully";
    }


    private class OUAttributeMapper implements AttributesMapper<Group> {

        @Override
        public Group mapFromAttributes(Attributes attributes) throws NamingException {
            Group group = new Group();

            group.setId(null != attributes.get("ou") ? attributes.get("ou").get().toString() : null);
            group.setName(null != attributes.get("name") ? attributes.get("name").get().toString() : null);
            group.setDescription(null != attributes.get("description") ? attributes.get("description").get().toString() : null);
            return group;
        }
    }
}
