package parsso.idman.helpers.reloadConfigs;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import parsso.idman.models.other.PWD;
import parsso.idman.models.other.Property;

import javax.naming.Name;
import javax.naming.directory.*;
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
    @Autowired
    PwdAttributeMapper pwdAttributeMapper;

    private Name buildDn() {
        return LdapNameBuilder.newInstance("cn=DefaultPPolicy,ou=Policies," + BASE_DN).build();
    }

    public boolean update(List<Property> settings) {

        ModificationItem[] items = new ModificationItem[9];
        Attribute[] attrs = new Attribute[9];

        for (Property setting : settings) {
            switch (setting.get_id()) {
                case "pwdCheckQuality":
                    attrs[0] = new BasicAttribute("pwdCheckQuality", setting.getValue());
                    items[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[0]);
                    continue;
                case "pwdExpireWarning":
                    attrs[1] = new BasicAttribute("pwdExpireWarning", setting.getValue());
                    items[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[1]);
                    continue;
                case "pwdFailureCountInterval":
                    attrs[2] = new BasicAttribute("pwdFailureCountInterval", setting.getValue());
                    items[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[2]);
                    continue;

                case "pwdGraceAuthNLimit":
                    attrs[3] = new BasicAttribute("pwdGraceAuthNLimit", setting.getValue());
                    items[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[3]);
                    continue;

                case "pwdInHistory":
                    attrs[4] = new BasicAttribute("pwdInHistory", setting.getValue());
                    items[4] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[4]);
                    continue;

                case "pwdLockout":
                    attrs[5] = new BasicAttribute("pwdLockout", setting.getValue());
                    items[5] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[5]);
                    continue;

                case "pwdLockoutDuration":
                    attrs[6] = new BasicAttribute("pwdLockoutDuration", setting.getValue());
                    items[6] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[6]);
                    continue;
                case "pwdMaxFailure":
                    attrs[7] = new BasicAttribute("pwdMaxFailure", setting.getValue());
                    items[7] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[7]);
                    continue;


                case "pwdMinLength":
                    attrs[8] = new BasicAttribute("pwdMinLength", setting.getValue());
                    items[8] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[8]);
            }

        }

        try {
            ldapTemplate.modifyAttributes(buildDn(), items);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public PWD retrieve(){
        SearchControls searchControls = new SearchControls();
        searchControls.setReturningAttributes(new String[]{"*", "+"});
        return ldapTemplate.search(buildDn(),new EqualsFilter("objectClass","pwdPolicy").encode(),pwdAttributeMapper).get(0);
    }

}
