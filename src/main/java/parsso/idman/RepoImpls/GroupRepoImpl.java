package parsso.idman.RepoImpls;


import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class GroupRepoImpl implements GroupRepo {

    Logger logger = LoggerFactory.getLogger(Group.class);

    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;

    @Autowired
    private LdapTemplate ldapTemplate;

    @Override
    public HttpStatus remove(JSONObject jsonObject) {

        List<Group> groups = new LinkedList<>();
        if (jsonObject.size() == 0)
            groups = retrieve();
        else {
            ArrayList jsonArray = (ArrayList) jsonObject.get("names");
            Iterator<String> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                Group group = retrieveOu(iterator.next());
                if (group != null)
                    groups.add(group);
            }
        }

        if (groups != null)
            for (Group group : groups) {
                Name dn = buildDn(group.getId());
                try {
                    logger.info("All groups removed");
                    ldapTemplate.unbind(dn);

                } catch (Exception e) {
                    logger.warn("removing group " + group.getName() + "  was unsuccessful");
                }

            }

        logger.info("Removing groups ended");
        return HttpStatus.OK;
    }


    @Override
    public Group retrieveOu(String uid) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        List<Group> groups = retrieve();

        for (Group group : groups) {
            if (group.getId().equals(uid))
                return group;
        }
        return null;
    }

    @Override
    public List<Group> retrieveCurrentUserGroup(User user) {
        List<String> memberOf = user.getMemberOf();
        List<Group> groups = new ArrayList<Group>();
        try {
            for (int i = 0; i < memberOf.size(); ++i) {
                groups.add(retrieveOu(memberOf.get(i)));
            }
        } catch (NullPointerException e) {

        }
        return groups;
    }

    private Attributes buildAttributes(String uid, Group group) {

        BasicAttribute ocattr = new BasicAttribute("objectclass");
        ocattr.add("extensibleObject");
        ocattr.add("organizationalUnit");
        ocattr.add("top");

        Attributes attrs = new BasicAttributes();
        attrs.put(ocattr);
        attrs.put("name", group.getName());
        attrs.put("ou", uid);
        if (group.getDescription() != "")
            attrs.put("description", group.getDescription());
        else
            attrs.put("description", " ");

        return attrs;
    }

    public Name buildDn(String id) {
        return LdapNameBuilder.newInstance(BASE_DN).add("ou", "Groups").add("ou", id).build();
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
    public HttpStatus create(Group ou) {

        List<Group> groups = retrieve();
        long max = 0;

        for (Group group : groups) {
            if (Long.valueOf(group.getId()) > max)
                max = Long.valueOf(group.getId());

        }

        Name dn = buildDn(String.valueOf(max + 1));
        try {
            ldapTemplate.bind(dn, null, buildAttributes(String.valueOf(max + 1), ou));
            logger.info("Group " + ou.getName() + " created successfully");
            return HttpStatus.OK;

        } catch (Exception e) {
            logger.warn("Creating group " + ou.getName() + " was unsuccessful");
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Override
    public HttpStatus update(String id, Group ou) {
        Name dn = buildDn(id);

        Group group = retrieveOu(id);

        try {
            ldapTemplate.rebind(dn, null, buildAttributes(group.getId(), ou));
            logger.info("Group " + ou.getId() + " updated successfully");
            return HttpStatus.OK;

        } catch (Exception e) {
            logger.warn("updating group " + ou.getName() + "  was unsuccessful");
            return HttpStatus.BAD_REQUEST;
        }
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
