package parsso.idman.Helpers.User;


import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.IdmanApplication;
import parsso.idman.Models.Time;
import parsso.idman.Models.User;
import parsso.idman.Repos.UserRepo;

import javax.naming.Name;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

@Service
public class BuildAttributes {

    final static Logger logger = LoggerFactory.getLogger(IdmanApplication.class);

    @Value("${default.user.password}")
    private String defaultPassword;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private LdapTemplate ldapTemplate;


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
    public DirContextOperations buildAttributes(String uid, User p, Name dn) {

        User old = userRepo.retrieveUsers(uid);

        DirContextOperations context = ldapTemplate.lookupContext(dn);

        if (p.getFirstName() != "" && p.getFirstName() != null)
            context.setAttributeValue("givenName", p.getFirstName());
        if (p.getLastName() != "" && p.getLastName() != null) context.setAttributeValue("sn", p.getLastName());
        if (p.getDisplayName() != "" && p.getDisplayName() != null)
            context.setAttributeValue("displayName", p.getDisplayName());
        if (p.getUserPassword() != null && p.getUserPassword() != "")
            context.setAttributeValue("userPassword", p.getUserPassword());
        if (p.getMobile() != "" && p.getMobile() != null) context.setAttributeValue("mobile", p.getMobile());
        if (p.getEmployeeNumber() != null && p.getEmployeeNumber() != "") context.setAttributeValue("employeeNumber", p.getEmployeeNumber());
        if (p.getMail() != null) context.setAttributeValue("mail", p.getMail());

        //if (p.getTimeStamp() > 0)
            //context.setAttributeValue("createtimestamp", Long.valueOf(p.getTimeStamp()).toString().substring(0,14));

            if (p.getMail() != "" && p.getFirstName() != null) context.setAttributeValue("mail", p.getMail());
        if ((p.getFirstName()) != null || (p.getLastName() != null)) {
            if (p.getFirstName() == null)
                context.setAttributeValue("cn", userRepo.retrieveUsers(uid).getFirstName() + ' ' + p.getLastName());

            else if (p.getLastName() == null)
                context.setAttributeValue("cn", p.getFirstName() + ' ' + userRepo.retrieveUsers(uid).getLastName());

            else context.setAttributeValue("cn", p.getFirstName() + ' ' + p.getLastName());
        }

        if (p.getCStatus() != null) {

            if (p.getCStatus().equals("enable"))
                userRepo.enable(uid);
            else if (p.getCStatus().equals("disable"))
                userRepo.disable(uid);
            else if (p.getCStatus().equals("unlock"))
                userRepo.unlock(uid);

            logger.warn("User \""+p.getUserId()+"\" access level changed from \""+old.getStatus()+"\" to \""+p.getStatus()+"\"");

        }


        //if (p.getTokens()!=null && p.getTokens().getResetPassToken() != null) context.setAttributeValue("resetPassToken", p.getTokens().getResetPassToken());


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

        if (p.getDescription() != "" && p.getDescription() != null)
            context.setAttributeValue("description", p.getDescription());


        if (p.getEndTime() != null) {
            String time = Time.convertDateTimeJalali(p.getEndTime());
            context.setAttributeValue("pwdEndTime", time + "Z");
        }

        if (p.getEmployeeNumber() != "" && old.getEmployeeNumber() != null)
            context.setAttributeValue("employeeNumber", p.getEmployeeNumber());


        return context;
    }

}
