package parsso.idman.Helpers.User;


import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Helpers.TimeHelper;
import parsso.idman.Helpers.Variables;
import parsso.idman.Models.Users.User;
import parsso.idman.Repos.UserRepo;

import javax.naming.Name;
import javax.naming.directory.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
public class BuildAttributes {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    BuildDnUser buildDnUser;
    ZoneId zoneId = ZoneId.of(Variables.ZONE);
    @Value("${default.user.password}")
    private String defaultPassword;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private LdapTemplate ldapTemplate;
    @Autowired
    private Operations operations;

    public Attributes BuildAttributes(User p) {

        BasicAttribute ocattr = new BasicAttribute("objectclass");
        ocattr.add("top");
        ocattr.add("person");
        ocattr.add("inetOrgPerson");
        ocattr.add("organizationalPerson");
        ocattr.add("pwdPolicy");

        Attributes attrs = new BasicAttributes();
        attrs.put(ocattr);

        attrs.put("uid", p.getUserId().trim());
        attrs.put("givenName", p.getFirstName().equals("") ? " " : p.getFirstName().trim());
        attrs.put("sn", p.getLastName().equals("") ? " " : p.getLastName().trim());
        attrs.put("userPassword", p.getUserPassword() != null ? p.getUserPassword() : defaultPassword);
        attrs.put("displayName", p.getDisplayName().trim());
        attrs.put("mobile", p.getMobile());
        attrs.put("employeeNumber", p.getEmployeeNumber() == null || p.getEmployeeNumber().equals("") ? "0" : p.getEmployeeNumber());
        attrs.put("mail", p.getMail().trim());
        attrs.put("cn", p.getFirstName().trim() + ' ' + p.getLastName().trim());
        if (p.getUsersExtraInfo() != null && p.getUsersExtraInfo().getResetPassToken() != null)
            attrs.put("resetPassToken", p.getUsersExtraInfo().getResetPassToken());
        if (p.getMemberOf() != null && p.getMemberOf().size() != 0) {
            if (!(p.getMemberOf().size() == 1 && p.getMemberOf().get(0).equals(""))) {
                Attribute attr = new BasicAttribute("ou");
                for (int i = 0; i < p.getMemberOf().size(); i++)
                    attr.add(p.getMemberOf().get(i));
                attrs.put(attr);
            }
        }
        if (p.getDescription() != null && !(p.getDescription().equals("")))
            attrs.put("description", p.getDescription());
        else
            attrs.put("description", " ");

        if (p.isLocked())
            attrs.put("pwdAccountLockedTime", p.isEnabled());

        if (p.getEndTime() != null) {

            try {
                if(p.getEndTime().length() ==13)
                    attrs.put("pwdEndTime", TimeHelper.epochToDateLdapFormat(Long.valueOf(p.getEndTime())));
                if(p.getEndTime().length() ==10)
                    attrs.put("pwdEndTime", TimeHelper.epochToDateLdapFormat(Long.valueOf(p.getEndTime())*1000));
                if (p.getEndTime().contains("."))
                    attrs.put("pwdEndTime", TimeHelper.epochToDateLdapFormat(TimeHelper.convertDateToEpoch(p.getEndTime())));



            }catch (Exception e){
                attrs.put("pwdEndTime", TimeHelper.epochToDateLdapFormat(Long.valueOf(p.getEndTime().substring(0, p.getEndTime().indexOf('.')))*1000));
            }

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
        if (p.getFirstName() != null)
            if (p.getFirstName() != "")
                context.setAttributeValue("givenName", p.getFirstName().trim());


        //Last Name (sn) *
        if (p.getLastName() != null)
            if (p.getLastName() != "")
                context.setAttributeValue("sn", p.getLastName().trim());

        //Persian name (displayName) *
        if (p.getDisplayName() != null)
            if (p.getDisplayName() != "")
                context.setAttributeValue("displayName", p.getDisplayName().trim());

        //attribute (Mobile) *
        if (p.getMobile() != null)
            if (p.getMobile() != "")
                context.setAttributeValue("mobile", p.getMobile());

        //Employee Number attribute (employeeNumber)
        if (p.getEmployeeNumber() != null) {
            if (!p.getEmployeeNumber().equals(""))
                context.setAttributeValue("employeeNumber", p.getEmployeeNumber());
            else if (p.getEmployeeNumber().equals(""))
                context.removeAttributeValue("employeeNumber", old.getEmployeeNumber());
        }

        //Mail Address attribute (Mail) *
        if (p.getMail() != null)
            if (p.getMail() != "")
                context.setAttributeValue("mail", p.getMail().trim());

        //Description attribute
        if (p.getDescription() != null) {
            if (!p.getDescription().equals(""))
                context.setAttributeValue("description", p.getDescription());
            else if (p.getDescription().equals(""))
                context.removeAttributeValue("description", old.getDescription());
        }

        //cn attribute (English full name that computing from last name and firs name)
        if (p.getFirstName() != null && p.getLastName() != null) {
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
            if (p.getCStatus().equals("enable")) operations.enable(doerID, uid);
            else if (p.getCStatus().equals("disable")) operations.disable(doerID, uid);
            else if (p.getCStatus().equals("unlock")) operations.unlock(doerID, uid);
        } else {
            if (p.getStatus() != null) {
                String oldStatus = old.getStatus();
                String newStatus = p.getStatus();

                if (oldStatus.equals("enable") && newStatus.equals("disable"))
                    operations.disable(doerID, uid);

                else if (oldStatus.equals("disable") && newStatus.equals("enable"))
                    operations.enable(doerID, uid);

                else if (oldStatus.equals("lock") && newStatus.equals("enable"))
                    operations.unlock(doerID, uid);

            }

        }

        //ou attribute stating membrane in group
        if (p.getMemberOf() != null) {
            if (p.getMemberOf().size() != 0 && p.getMemberOf().get(0) != ("")) {
                for (int i = 0; i < p.getMemberOf().size(); i++) {
                    if (old.getMemberOf() == null && i == 0) context.setAttributeValue("ou", p.getMemberOf().get(i));
                    else context.addAttributeValue("ou", p.getMemberOf().get(i));
                }
            } else if (old.getMemberOf() != null)
                for (String id : old.getMemberOf()) {
                    context.removeAttributeValue("ou", id);
                }

            if (old.getMemberOf() != null)
                for (String group : old.getMemberOf()) {
                    if (!p.getMemberOf().contains(group))
                        context.removeAttributeValue("ou", group);
                }
        }

        Instant instant = Instant.now(); //can be LocalDateTime
        ZoneId systemZone = zoneId; // my timezone
        ZoneOffset currentOffsetForMyZone = systemZone.getRules().getOffset(instant);


        //EndTime

        if (p.getEndTime() != null && p.getEndTime() != "") {
            if (p.getEndTime().length()==10)
                context.setAttributeValue("pwdEndTime", TimeHelper.epochToDateLdapFormat(Long.valueOf(p.getEndTime())*1000));
            else
                context.setAttributeValue("pwdEndTime", TimeHelper.epochToDateLdapFormat(Long.valueOf(p.getEndTime())));


        } else
            context.removeAttributeValue("pwdEndTime", old.getEndTime());


        if (p.getUsersExtraInfo() != null && p.getUsersExtraInfo().getResetPassToken() != null)
            mongoTemplate.save(p.getUsersExtraInfo(), Variables.col_usersExtraInfo);

        ModificationItem[] modificationItems;
        modificationItems = new ModificationItem[1];

        if (p.getEndTime() != null && old.getEndTime() != null) {

            modificationItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("pwdEndTime"));
            ldapTemplate.modifyAttributes(buildDnUser.buildDn(p.getUserId()), modificationItems);

        } else if (p.getEndTime() == null && old.getEndTime() != null) {
            modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("pwdEndTime"));
            ldapTemplate.modifyAttributes(buildDnUser.buildDn(p.getUserId()), modificationItems);

        } else if (p.getEndTime() != null && old.getEndTime() != null) {
            modificationItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("pwdEndTime"));
            ldapTemplate.modifyAttributes(buildDnUser.buildDn(p.getUserId()), modificationItems);
        }

        return context;
    }

}
