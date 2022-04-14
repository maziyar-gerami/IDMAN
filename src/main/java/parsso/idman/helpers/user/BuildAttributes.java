package parsso.idman.helpers.user;


import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.Variables;
import parsso.idman.models.users.User;
import parsso.idman.repos.UserRepo;

import javax.naming.Name;
import javax.naming.directory.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
public class BuildAttributes {
    final ZoneId zoneId = ZoneId.of(Variables.ZONE);
    final MongoTemplate mongoTemplate;
    private final UserRepo.UsersOp.Retrieve usersOpRetrieve;
    private final LdapTemplate ldapTemplate;
    private final Operations operations;

    @Value("${spring.ldap.base.dn}")
    protected String BASE_DN;

    @Autowired
    BuildAttributes(MongoTemplate mongoTemplate, LdapTemplate ldapTemplate,
                    Operations operations,UserRepo.UsersOp.Retrieve usersOpRetrieve) {
        this.mongoTemplate = mongoTemplate;
        this.usersOpRetrieve = usersOpRetrieve;
        this.ldapTemplate = ldapTemplate;
        this.operations = operations;
    }




    public Attributes build(User p) {

        BasicAttribute ocattr = new BasicAttribute("objectclass");
        ocattr.add("top");
        ocattr.add("person");
        ocattr.add("inetOrgPerson");
        ocattr.add("organizationalPerson");
        ocattr.add("pwdPolicy");

        Attributes attrs = new BasicAttributes();
        attrs.put(ocattr);
        String uid = new String(p.get_id().toString().trim().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        attrs.put("uid", uid);
        String givenName = new String(p.getFirstName().trim().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        attrs.put("givenName", givenName.equals("") ? " " : givenName.trim());
        String sn = new String(p.getLastName().trim().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        attrs.put("sn", sn.equals("") ? " " : sn.trim());
        String userPassword = new String(p.getUserPassword().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        attrs.put("userPassword", userPassword);
        String displayName = new String(p.getDisplayName().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        attrs.put("displayName", displayName.trim());
        String mobile = new String(p.getMobile().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        attrs.put("mobile", mobile);
        String employeeNumber = new String(p.getEmployeeNumber().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        attrs.put("employeeNumber", p.getEmployeeNumber().equals("") ? "0" : employeeNumber);
        String mail = new String(p.getMail().trim().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        attrs.put("mail", mail);
        attrs.put("cn", givenName + ' ' + sn);

        if (p.getMemberOf() != null && p.getMemberOf().size() != 0) {
            if (!(p.getMemberOf().size() == 1 && p.getMemberOf().get(0).equals(""))) {
                Attribute attr = new BasicAttribute("ou");
                for (int i = 0; i < p.getMemberOf().size(); i++)
                    attr.add(p.getMemberOf().get(i));
                attrs.put(attr);
            }
        }

        String description = new String(p.getDescription().trim().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        if (!description.equals(""))
            attrs.put("description", description);
        else
            attrs.put("description", " ");

        if (p.isLocked())
            attrs.put("pwdAccountLockedTime", p.isEnabled());

        attrs.put("pwdAttribute", "userPassword");

        attrs.put("pwdReset", "TRUE");

        return attrs;
    }

    @SneakyThrows
    public DirContextOperations buildAttributes(String doerID, String uid, User p, Name dn) {

        //retrieve old user
        User old = usersOpRetrieve.retrieveUsers(uid);
        DirContextOperations context = ldapTemplate.lookupContext(dn);

        //First name (givenName) *
        if (p.getFirstName() != null)
            if (!p.getFirstName().equals("")) {
                String givenName = new String(p.getFirstName().trim().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                context.setAttributeValue("givenName", givenName);
            }

        //Last Name (sn) *
        if (p.getLastName() != null)
            if (!p.getLastName().equals("")) {
                String sn = new String(p.getLastName().trim().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                context.setAttributeValue("sn", sn);
            }

        //Persian name (displayName) *
        if (p.getDisplayName() != null)
            if (!p.getDisplayName().equals("")) {
                String displayName = new String(p.getDisplayName().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                context.setAttributeValue("displayName", displayName);
            }

        //attribute (mobile) *
        if (p.getMobile() != null)
            if (!p.getMobile().equals("")) {
                String mobile = new String(p.getMobile().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                context.setAttributeValue("mobile", mobile);
            }

        //Employee Number attribute (employeeNumber)
        if (p.getEmployeeNumber() != null) {
            if (!p.getEmployeeNumber().equals(""))
                context.setAttributeValue("employeeNumber", p.getEmployeeNumber());
            else if (p.getEmployeeNumber().equals(""))
                context.removeAttributeValue("employeeNumber", old.getEmployeeNumber());
        }

        //Mail Address attribute (Mail) *
        if (p.getMail() != null)
            if (!p.getMail().equals("")) {
                context.setAttributeValue("mail", p.getMail().trim());
            }

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
            switch (p.getCStatus()) {
                case "enable":
                    operations.enable(doerID, uid);
                    break;
                case "disable":
                    operations.disable(doerID, uid);
                    break;
                case "unlock":
                    operations.unlock(doerID, uid);
                    break;
            }
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
            if (p.getMemberOf().size() != 0 && !p.getMemberOf().get(0).equals("")) {
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
        ZoneOffset currentOffsetForMyZone = zoneId.getRules().getOffset(instant);

        if (p.getUsersExtraInfo() != null && p.getUsersExtraInfo().getResetPassToken() != null)
            mongoTemplate.save(p.getUsersExtraInfo(), Variables.col_usersExtraInfo);

        //EndTime
        return context;
    }

}
