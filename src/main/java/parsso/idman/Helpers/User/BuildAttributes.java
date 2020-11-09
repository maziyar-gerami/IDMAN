package parsso.idman.Helpers.User;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Time;
import parsso.idman.Models.User;
import parsso.idman.Repos.UserRepo;
import parsso.idman.utils.Convertor.DateConverter;

import javax.naming.Name;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import java.util.UUID;

@Service
public class BuildAttributes {

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

        Attributes attrs = new BasicAttributes();
        attrs.put(ocattr);

        attrs.put("uid", p.getUserId());
        attrs.put("givenName", p.getFirstName().equals("") ? " " : p.getDisplayName());
        attrs.put("sn", p.getLastName().equals("") ? " " : p.getLastName());
        attrs.put("userPassword", p.getUserPassword() != null ? p.getUserPassword() : defaultPassword);
        attrs.put("displayName", p.getDisplayName());
        attrs.put("mobile", p.getMobile().equals("") || p.getMobile() == null ? " " : p.getMobile());
        attrs.put("mail", p.getMail());
        attrs.put("cn", p.getFirstName() + ' ' + p.getLastName());
        if (p.getTokens()!=null && p.getTokens().getResetPassToken() != null)
            attrs.put("resetPassToken", p.getTokens().getResetPassToken());
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

        if (p.getPhotoName() != null)
            attrs.put("photoName", p.getPhotoName());
        else
            attrs.put("photoName", " ");

        if (p.isLocked())
            attrs.put("pwdAccountLockedTime", p.isEnabled());



        return attrs;
    }

    @SneakyThrows
    public DirContextOperations buildAttributes(String uid, User p, Name dn) {


        User old = userRepo.retrieveUser(uid);

        DirContextOperations context = ldapTemplate.lookupContext(dn);

        if (p.getFirstName() != "" && p.getFirstName() != null)
            context.setAttributeValue("givenName", p.getFirstName());
        if (p.getLastName() != "" && p.getLastName() != null) context.setAttributeValue("sn", p.getLastName());
        if (p.getDisplayName() != "" && p.getDisplayName() != null)
            context.setAttributeValue("displayName", p.getDisplayName());
        if (p.getUserPassword() != null && p.getUserPassword() != "")
            context.setAttributeValue("userPassword", p.getUserPassword());
        if (p.getMobile() != "" && p.getMobile() != null) context.setAttributeValue("mobile", p.getMobile());
        if (p.getMail() != "" && p.getFirstName() != null) context.setAttributeValue("mail", p.getMail());
        if ((p.getFirstName()) != null || (p.getLastName() != null)) {
            if (p.getFirstName() == null)
                context.setAttributeValue("cn", userRepo.retrieveUser(uid).getFirstName() + ' ' + p.getLastName());

            else if (p.getLastName() == null)
                context.setAttributeValue("cn", p.getFirstName() + ' ' + userRepo.retrieveUser(uid).getLastName());

            else context.setAttributeValue("cn", p.getFirstName() + ' ' + p.getLastName());
        }
        if (p.getMail() != null) context.setAttributeValue("photoName", p.getPhotoName());

        if (p.getCStatus() != null) {

            if (p.getCStatus().equals("enable"))
                userRepo.enable(uid);
            else if (p.getCStatus().equals("disable"))
                userRepo.disable(uid);
            else if (p.getCStatus().equals("unlock"))
                userRepo.unlock(uid);

        }


        if (p.getTokens()!=null && p.getTokens().getResetPassToken() != null) context.setAttributeValue("resetPassToken", p.getTokens().getResetPassToken());


        if (p.getMemberOf() != null) {
            if (p.getMemberOf().size() != 0) {
                for (int i = 0; i < p.getMemberOf().size(); i++) {
                    if (i == 0) context.setAttributeValue("ou", p.getMemberOf().get(i));
                    else context.addAttributeValue("ou", p.getMemberOf().get(i));
                }
            } else
                for (String id : old.getMemberOf()) {
                    context.removeAttributeValue("ou", id);
                }
        }

        if (p.getDescription() != "" && p.getDescription() != null)
            context.setAttributeValue("description", p.getDescription());
        if (p.getPhotoName() != "" && p.getPhotoName() != null)
            context.setAttributeValue("photoName", p.getPhotoName());
        else
            context.setAttributeValue("photoName", old.getPhotoName());


        if (p.getEndTime() != null && p.getEndTime() != null && !(p.getEndTime().equals(old.getEndTime())))
            context.setAttributeValue("pwdEndTime", Time.setEndTime(p.getEndTime()));



        return context;
    }

}
