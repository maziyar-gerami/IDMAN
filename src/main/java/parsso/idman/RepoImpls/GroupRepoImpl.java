package parsso.idman.RepoImpls;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class GroupRepoImpl implements GroupRepo {

    public static final String BASE_DN = "dc=example,dc=com";;


    @Autowired
    private LdapTemplate ldapTemplate;

    @Override
    public String remove(String name) {

        Name dn = buildDn(name);
        ldapTemplate.unbind(dn);
        return name + " removed successfully";
    }

    @Override
    public String remove() {

        List <Group> allous = retrieve();
        for (Group ou: allous) {
            Name dn = buildDn(ou.getName());
            ldapTemplate.unbind(dn);

        }

        return "All Groups removed successfully";
    }



    @Override
    public Group retrieveOu(String name) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectClass", "extensibleObject"));
        filter.and(new EqualsFilter("ou", name));

        List<Group> groupList = ldapTemplate.search(BASE_DN , filter.encode(),
                new GroupRepoImpl.OUAttributeMapper());
        return groupList.get(0);
    }

    @Override
    public Group retrieveOu() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<Group> groupList = ldapTemplate.search(query().where("objectClass").is("extensibleObject"),
                new GroupRepoImpl.OUAttributeMapper());
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

    private Attributes buildAttributes(Group group) {

        BasicAttribute ocattr = new BasicAttribute("objectclass");
        ocattr.add("extensibleObject");
        ocattr.add("organizationalUnit");
        ocattr.add("top");

        Attributes attrs = new BasicAttributes();
        attrs.put(ocattr);
        attrs.put("ou", group.getName());
        attrs.put("uid",String.valueOf(group.getId()));
        attrs.put("description" , group.getDescription());
        return attrs;
    }

    public Name buildDn(String  name) {
        return LdapNameBuilder.newInstance(BASE_DN).add("ou", "Groups").add("ou", name).build();
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
                new GroupRepoImpl.OUAttributeMapper());

    }

    @Override
    public String create(Group ou) {
        Name dn = buildDn(ou.getName());
        ldapTemplate.bind(dn, null, buildAttributes(ou));
        return ou.getName() + " created successfully";
    }

    @Override
    public String update(String name, Group ou) {
        Name dn = buildDn(name);

        ldapTemplate.rebind(dn, null, buildAttributes(ou));
        return ou.getName() + " updated successfully";
    }


    private class OUAttributeMapper implements AttributesMapper<Group> {

        @Override
        public Group mapFromAttributes(Attributes attributes) throws NamingException {
            Group group = new Group();

            group.setId(null != attributes.get("uid") ? attributes.get("uid").get().toString() : null);
            group.setName(null != attributes.get("ou") ? attributes.get("ou").get().toString() : null);
            group.setDescription(null != attributes.get("description") ? attributes.get("description").get().toString() : null);
            return group;
        }
    }
}
