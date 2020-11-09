package parsso.idman.Helpers.User;

import org.springframework.ldap.core.AttributesMapper;
import parsso.idman.Models.SimpleUser;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.LinkedList;
import java.util.List;

public class SimpleUserAttributeMapper implements AttributesMapper<SimpleUser> {

    @Override
    public SimpleUser mapFromAttributes(Attributes attributes) throws NamingException {
        SimpleUser user = new SimpleUser();

        user.setUserId(null != attributes.get("uid") ? attributes.get("uid").get().toString() : null);
        user.setDisplayName(null != attributes.get("displayName") ? attributes.get("displayName").get().toString() : null);
        int nGroups = (null == attributes.get("ou") ? 0 : attributes.get("ou").size());
        List<String> ls = new LinkedList<>();
        for (int i = 0; i < nGroups; i++) ls.add(attributes.get("ou").get(i).toString());
        user.setMemberOf(ls);

        return user;
    }
}