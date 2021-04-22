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
        attrs.put("mobile", p.getMobile().equals("") || p.getMobile() == null ? " " : p.getMobile());
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

        //givenName (First Name attribute)
        if (p.getFirstName() != "" && p.getFirstName() != null)
            context.setAttributeValue("givenName", p.getFirstName());
        else if (p.getFirstName().equals(""))
            context.removeAttributeValue("givenName", old.getFirstName());

        //lastName
        if (p.getLastName() != "" && p.getLastName() != null)
            context.setAttributeValue("sn", p.getLastName());
        else if (p.getLastName().equals(""))
            context.removeAttributeValue("sn", old.getFirstName());


        //displayName attribute (Persian name attribute)
        if (p.getDisplayName() != "" && p.getDisplayName() != null)
            context.setAttributeValue("displayName", p.getDisplayName());
        else if (p.getDisplayName().equals(""))
            context.removeAttributeValue("displayName", old.getDisplayName());

        //mobile attribute (Mobile attribute)
        if (p.getMobile() != "" && p.getMobile() != null)
            context.setAttributeValue("mobile", p.getMobile());
        else if (p.getMobile().equals("") && old.getMobile()!=null && !old.getMobile().equals(""))
            context.removeAttributeValue("mobile", old.getMobile());

        //employeeNumber attribute (Employee Number attribute)
        if (p.getEmployeeNumber() != null && !p.getEmployeeNumber().equals(""))
            context.setAttributeValue("employeeNumber", p.getEmployeeNumber());
        else if (p.getEmployeeNumber().equals(""))
            context.removeAttributeValue("employeeNumber", old.getEmployeeNumber());

        //mail attribute (Mail attribute)
        if (p.getMail() != null)
            context.setAttributeValue("mail", p.getMail());
        else if (p.getMail().equals(""))
            context.removeAttributeValue("mail", old.getMail());

        //Description attribute
        if (p.getDescription() != "" && p.getDescription() != null)
            context.setAttributeValue("description", p.getDescription());
        else if ((p.getDescription().equals("")))
            context.removeAttributeValue("description", old.getDescription());

        //cn attribute (English full name that computing from last name and firs name)
        if (!(p.getFirstName().equals("")) && (!(p.getLastName().equals(""))))
                context.setAttributeValue("cn", p.getFirstName() + ' ' + p.getLastName());
        else if (p.getFirstName().equals(""))
                context.setAttributeValue("cn", old.getLastName());
        else if (p.getLastName().equals(""))
            context.setAttributeValue("cn", old.getFirstName());
        else if ((p.getFirstName().equals("")) && (p.getLastName().equals("")))
            context.setAttributeValue("cn", "");

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


        //End access time of a user
        if (p.getEndTime() != null) {
            String time;
            try {
                time = Time.convertDateTimeJalali(p.getEndTime());
            }catch (Exception e){
                time = p.getEndTime();
            }
            context.setAttributeValue("pwdEndTime", time + "Z");
        } else
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
