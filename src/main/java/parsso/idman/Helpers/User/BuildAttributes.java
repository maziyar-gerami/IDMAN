package parsso.idman.Helpers.User;


import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Logs.ReportMessage;
import parsso.idman.Models.Time;
import parsso.idman.Models.Users.User;
import parsso.idman.Repos.UserRepo;

import javax.naming.Name;
import javax.naming.directory.*;

@Service
public class BuildAttributes {

    @Value("${default.user.password}")
    private String defaultPassword;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private LdapTemplate ldapTemplate;

    @Autowired
    BuildDnUser buildDnUser;


    public Attributes BuildAttributes(User p) {

        BasicAttribute ocattr = new BasicAttribute("objectclass");
        ocattr.add("top");
        ocattr.add("person");
        ocattr.add("inetOrgPerson");
        ocattr.add("organizationalPerson");
        ocattr.add("pwdPolicy");

        Attributes attrs = new BasicAttributes();
        attrs.put(ocattr);

        attrs.put("uid", p.getUserId());
        attrs.put("givenName", p.getFirstName().equals("") ? " " : p.getFirstName());
        attrs.put("sn", p.getLastName().equals("") ? " " : p.getLastName());
        attrs.put("userPassword", p.getUserPassword() != null ? p.getUserPassword() : defaultPassword);
        attrs.put("displayName", p.getDisplayName());
        attrs.put("mobile", p.getMobile());
        attrs.put("employeeNumber", p.getEmployeeNumber() == null || p.getEmployeeNumber().equals("") ? "0" : p.getEmployeeNumber());
        attrs.put("mail", p.getMail());
        attrs.put("cn", p.getFirstName() + ' ' + p.getLastName());
        if (p.getUsersExtraInfo() != null && p.getUsersExtraInfo().getResetPassToken() != null)
            attrs.put("resetPassToken", p.getUsersExtraInfo().getResetPassToken());
        if (p.getMemberOf() != null && p.getMemberOf().size() != 0) {
            Attribute attr = new BasicAttribute("ou");
            for (int i = 0; i < p.getMemberOf().size(); i++)
                attr.add(p.getMemberOf().get(i));
            attrs.put(attr);
        }
        if (p.getDescription() != null && !(p.getDescription().equals("")))
            attrs.put("description", p.getDescription());
        else
            attrs.put("description", " ");

        if (p.isLocked())
            attrs.put("pwdAccountLockedTime", p.isEnabled());

        if (p.getEndTime() != null) {
            attrs.put("pwdEndTime", Time.setEndTime(p.getEndTime()) + 'Z');
        }

        attrs.put("pwdAttribute", "userPassword");

        return attrs;
    }

    @SneakyThrows
    public DirContextOperations buildAttributes(String doerID, String uid, User p, Name dn) {

        //retrieve old user
        User old = userRepo.retrieveUsers(uid);
        DirContextOperations context = ldapTemplate.lookupContext(dn);

        //First name (givenName) *
        if (p.getFirstName()!=null)
            if (p.getFirstName() != "")
                context.setAttributeValue("givenName", p.getFirstName());


        //Last Name (sn) *
        if (p.getLastName()!=null)
            if (p.getLastName() != "")
                context.setAttributeValue("sn", p.getLastName());

        //Persian name (displayName) *
        if (p.getDisplayName()!=null)
            if (p.getDisplayName() != "")
                context.setAttributeValue("displayName", p.getDisplayName());

        //attribute (Mobile) *
        if (p.getMobile() != null)
            if (p.getMobile() != "")
                context.setAttributeValue("mobile", p.getMobile());

        //Employee Number attribute (employeeNumber)
        if (p.getEmployeeNumber() != null){
            if (!p.getEmployeeNumber().equals(""))
                context.setAttributeValue("employeeNumber", p.getEmployeeNumber());
            else if (p.getEmployeeNumber().equals(""))
                context.removeAttributeValue("employeeNumber", old.getEmployeeNumber());
        }

        //Mail Address attribute (Mail) *
        if (p.getMail() != null)
            if (p.getMail() != "")
                context.setAttributeValue("mail", p.getMail());

        //Description attribute
        if(p.getDescription() != null) {
            if (!p.getDescription().equals(""))
                context.setAttributeValue("description", p.getDescription());
            else if (p.getDescription().equals(""))
                context.removeAttributeValue("description", old.getDescription());
        }

        //cn attribute (English full name that computing from last name and firs name)
        if(p.getFirstName()!=null && p.getLastName()!=null) {
            if (!(p.getFirstName().equals("")) && (!(p.getLastName().equals(""))))
                context.setAttributeValue("cn", p.getFirstName() + ' ' + p.getLastName());
            else if (p.getFirstName().equals(""))
                context.setAttributeValue("cn", old.getLastName());
            else if (p.getLastName().equals(""))
                context.setAttributeValue("cn", old.getFirstName());
            else if ((p.getFirstName().equals("")) && (p.getLastName().equals("")))
                context.setAttributeValue("cn", "");
        }

        //cStatus for changing status
        if (p.getCStatus() != null) {
            if (p.getCStatus().equals("enable")) userRepo.enable(doerID, uid);
            else if (p.getCStatus().equals("disable")) userRepo.disable(doerID, uid);
            else if (p.getCStatus().equals("unlock")) userRepo.unlock(doerID, uid);
        }else {
            if (p.getStatus() != null){
                String oldStatus = old.getStatus();
                String newStatus = p.getStatus();

                if (oldStatus.equals("enable") && newStatus.equals("disable"))
                    userRepo.disable(doerID, uid);

                else if (oldStatus.equals("disable") && newStatus.equals("enable"))
                    userRepo.enable(doerID, uid);

                else if (oldStatus.equals("lock") && newStatus.equals("enable"))
                    userRepo.unlock(doerID, uid);

            }

        }

        //ou attribute stating membrane in group
        if (p.getMemberOf() != null) {
            if (p.getMemberOf().size() != 0) {
                for (int i = 0; i < p.getMemberOf().size(); i++) {
                    if (i == 0) context.setAttributeValue("ou", p.getMemberOf().get(i));
                    else context.addAttributeValue("ou", p.getMemberOf().get(i));
                }
            } else if (old.getMemberOf() != null)
                for (String id : old.getMemberOf()) {
                    context.removeAttributeValue("ou", id);
                }
        }


        //EndTime
        if (p.getEndTime()!=null && p.getEndTime() != "")
            context.setAttributeValue("pwdEndTime",Time.setEndTime(p.getEndTime()) + 'Z');
            else
                context.removeAttributeValue("pwdEndTime", old.getEndTime());


        if(p.getUsersExtraInfo()!=null && p.getUsersExtraInfo().getResetPassToken()!=null)
            mongoTemplate.save(p.getUsersExtraInfo(), "IDMAN_UsersExtraInfo");

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        if(p.getEndTime()!=null && old.getEndTime()!=null)  {

            modificationItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("pwdEndTime"));
            ldapTemplate.modifyAttributes(buildDnUser.buildDn(p.getUserId()),modificationItems);

        } else if(p.getEndTime()==null && old.getEndTime()!=null)  {
            modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("pwdEndTime"));
            ldapTemplate.modifyAttributes(buildDnUser.buildDn(p.getUserId()),modificationItems);

        }else if(p.getEndTime()!=null && old.getEndTime()!=null)  {
            modificationItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("pwdEndTime"));
            ldapTemplate.modifyAttributes(buildDnUser.buildDn(p.getUserId()),modificationItems);
        }


        return context;
    }

}
