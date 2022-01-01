package parsso.idman.helpers.reloadConfigs;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import parsso.idman.models.other.Setting;

import javax.naming.Name;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.util.List;

@Getter
@Service
public class PasswordSettings {
    private final LdapTemplate ldapTemplate;
    @Autowired
    public PasswordSettings(LdapTemplate ldapTemplate){
        this.ldapTemplate = ldapTemplate;
    }
    @Value("${spring.ldap.base.dn}")
    private String BASE_DN;

    private Name buildDn() {
        return LdapNameBuilder.newInstance("cn=DefaultPPolicy,ou=Policies," + BASE_DN).build();
    }

    public void update(List<Setting> settings) {

        ModificationItem[] items = new ModificationItem[9];
        Attribute[] attrs = new Attribute[9];

        for (Setting setting : settings) {
            switch (setting.get_id()) {
                case "pwd.check.quality":
                    attrs[0] = new BasicAttribute("pwdCheckQuality", setting.getValue());
                    items[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[0]);
                    continue;

                case "pwd.expire.warning":

                    attrs[1] = new BasicAttribute("pwdExpireWarning", setting.getValue());
                    items[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[1]);
                    continue;
                case "pwd.failure.count.interval":

                    attrs[2] = new BasicAttribute("pwdFailureCountInterval", setting.getValue());
                    items[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[2]);
                    continue;
                case "pwd.grace.auth.n.limit":

                    attrs[3] = new BasicAttribute("pwdGraceAuthNLimit", setting.getValue());
                    items[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[3]);
                    continue;
                case "pwd.in.history":
                    attrs[4] = new BasicAttribute("pwdInHistory", setting.getValue());
                    items[4] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[4]);
                    continue;
                case "pwd.lockout":
                    attrs[5] = new BasicAttribute("pwdLockout", setting.getValue());
                    items[5] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[5]);
                    continue;
                case "pwd.lockout.duration":
                    attrs[6] = new BasicAttribute("pwdLockoutDuration", setting.getValue());
                    items[6] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[6]);
                    continue;
                case "pwd.max.failure":
                    attrs[7] = new BasicAttribute("pwdMaxFailure", setting.getValue());
                    items[7] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[7]);
                    continue;
                case "pwd.min.length":
                    attrs[8] = new BasicAttribute("pwdMinLength", setting.getValue());
                    items[8] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[8]);
            }

        }

        try {
            ldapTemplate.modifyAttributes(buildDn(), items);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
