package parsso.idman.helpers.user;


import org.springframework.ldap.core.AttributesMapper;
import org.springframework.stereotype.Service;
import parsso.idman.models.users.User.UserLoggedIn;
import parsso.idman.models.users.UsersExtraInfo;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.LinkedList;
import java.util.List;

@Service
public class SimpleUserAttributeMapper implements AttributesMapper<UsersExtraInfo> {
    @Override
    public UsersExtraInfo mapFromAttributes(Attributes attributes) throws NamingException {
        UsersExtraInfo user = new UsersExtraInfo();

        if (attributes == null || attributes.get("uid") == null)
            return null;

        user.set_id(null != attributes.get("uid") ? attributes.get("uid").get().toString() : null);
        user.setDisplayName(null != attributes.get("displayName") ? attributes.get("displayName").get().toString() : null);
        //user.setPasswordChangedTime(null != attributes.get("pwdChangedTime") ? Long.valueOf(attributes.get("pwdChangedTime").get().toString().substring(0, 14)) : Long.valueOf(attributes.get("createtimestamp").get().toString().substring(0, 14)));

        int nGroups = (null == attributes.get("ou") ? 0 : attributes.get("ou").size());
        List<String> ls = new LinkedList<>();
        for (int i = 0; i < nGroups; i++) ls.add(attributes.get("ou").get(i).toString());

        if (null != attributes.get("pwdAccountLockedTime"))
            if (attributes.get("pwdAccountLockedTime").get().toString().equals("00010101000000Z"))
                user.setStatus("disable");
            else
                user.setStatus("lock");
        else
            user.setStatus("enable");

        user.setMemberOf(ls);

        int nPass = 0;
        try {
            nPass = attributes.get("pwdHistory").size();
        } catch (Exception ignored) {
        }

        user.setLoggedIn(nPass > 0);

        return user;
    }

    @Service
    public static class LoggedInUserAttributeMapper implements AttributesMapper<UserLoggedIn> {
        @Override
        public UserLoggedIn mapFromAttributes(Attributes attributes) throws NamingException {
            UserLoggedIn user = new UserLoggedIn();

            if (attributes == null || attributes.get("uid") == null)
                return null;

            user.set_id(null != attributes.get("uid") ? attributes.get("uid").get().toString() : null);

            int nPass = 0;
            try {
                nPass = attributes.get("pwdHistory").size();
            } catch (Exception ignored) {
            }

            user.setLoggedIn(nPass > 0);
            return user;
        }
    }

}