package parsso.idman.Helpers.User;


import org.springframework.ldap.core.AttributesMapper;
import org.springframework.stereotype.Service;
import parsso.idman.Models.SimpleUser;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.LinkedList;
import java.util.List;

@Service
public class SimpleUserAttributeMapper implements AttributesMapper<SimpleUser> {

    @Override
    public SimpleUser mapFromAttributes(Attributes attributes) throws NamingException {
        SimpleUser user = new SimpleUser();


        if (attributes==null||attributes.get("uid") == null)
            return  null;
        user.setUserId(null != attributes.get("uid") ? attributes.get("uid").get().toString() : null);
        user.setDisplayName(null != attributes.get("displayName") ? attributes.get("displayName").get().toString() : null);
        user.setPasswordChangedTime(null != attributes.get("pwdChangedTime") ? Long.valueOf(attributes.get("pwdChangedTime").get().toString().substring(0, 14)) : Long.valueOf(attributes.get("createtimestamp").get().toString().substring(0, 14)));

        int nGroups = (null == attributes.get("ou") ? 0 : attributes.get("ou").size());
        List<String> ls = new LinkedList<>();
        for (int i = 0; i < nGroups; i++) ls.add(attributes.get("ou").get(i).toString());

        if (null != attributes.get("pwdAccountLockedTime"))
            if (attributes.get("pwdAccountLockedTime").get().toString().equals("40400404040404.950Z"))
                user.setStatus("disabled");
            else
                user.setStatus("locked");
        else
            user.setStatus("active");


        user.setMemberOf(ls);

        return user;
    }
}