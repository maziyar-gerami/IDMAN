package parsso.idman.helpers.configs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.PWD;
import parsso.idman.models.other.Property;

import javax.naming.Name;
import javax.naming.directory.*;
import java.util.List;

@Getter
@Service
public class PasswordSettings {
  private final LdapTemplate ldapTemplate;
  final UniformLogger uniformLogger;

  @Value("${spring.ldap.base.dn}")
  private String BASE_DN;

  @Autowired
  public PasswordSettings(UniformLogger uniformLogger, LdapTemplate ldapTemplate) {
    this.uniformLogger = uniformLogger;
    this.ldapTemplate = ldapTemplate;
  }

  private Name buildDn() {
    return LdapNameBuilder.newInstance("cn=DefaultPPolicy,ou=Policies," + BASE_DN).build();
  }

  public boolean update(String doer, List<Property> settings) {

    ModificationItem[] items = new ModificationItem[7];
    Attribute[] attrs = new Attribute[7];

    PWD pwd = retrieve();

    for (Property setting : settings) {
      switch (setting.get_id()) {

        case "pwdFailureCountInterval":
          attrs[0] = new BasicAttribute("pwdFailureCountInterval", setting.getValue());
          items[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[0]);
          continue;

        case "pwdGraceAuthNLimit":
          attrs[1] = new BasicAttribute("pwdGraceAuthNLimit", setting.getValue());
          items[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[1]);
          continue;

        case "pwdInHistory":
          attrs[2] = new BasicAttribute("pwdInHistory", setting.getValue());
          items[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[2]);
          continue;

        case "pwdLockout":
          attrs[3] = new BasicAttribute("pwdLockout",  setting.getValue().toString().toUpperCase());
          items[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[3]);
          continue;

        case "pwdLockoutDuration":
          attrs[4] = new BasicAttribute("pwdLockoutDuration", setting.getValue());
          items[4] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[4]);
          continue;
        case "pwdMaxFailure":
          attrs[5] = new BasicAttribute("pwdMaxFailure", setting.getValue());
          items[5] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[5]);
          continue;

        case "pwdMaxAge":
          attrs[6] = new BasicAttribute("pwdMaxAge", setting.getValue());
          items[6] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[6]);
      }

    }

    try {
      ldapTemplate.modifyAttributes(buildDn(), items);
      for (Property property : settings) {
        switch (property.get_id()) {
          case "pwdFailureCountInterval":
            if (property.getValue().toString().equalsIgnoreCase(pwd.getPwdFailureCountInterval()))
              continue;

          case "pwdGraceAuthNLimit":
            if (property.getValue().toString().equalsIgnoreCase(pwd.getPwdGraceAuthNLimit()))
              continue;

          case "pwdInHistory":
            if (property.getValue().toString().equalsIgnoreCase(pwd.getPwdInHistory()))
              continue;

          case "pwdLockout":
            if (property.getValue().toString().equalsIgnoreCase(pwd.getPwdLockout()))
              continue;

          case "pwdLockoutDuration":
            if (property.getValue().toString().equalsIgnoreCase(pwd.getPwdLockoutDuration()))
              continue;
          case "pwdMaxFailure":
            if (property.getValue().toString().equalsIgnoreCase(pwd.getPwdMaxFailure()))
              continue;

          case "pwdMaxAge":
            if (property.getValue().toString().equals(pwd.getPwdMaxAge()))
              continue;

        }
        uniformLogger.info(doer, new ReportMessage(Variables.MODEL_SETTINGS, property.get_id(),
            Variables.ACTION_UPDATE, Variables.RESULT_SUCCESS, property.getValue().toString(), ""));
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  public PWD retrieve() {
    SearchControls searchControls = new SearchControls();
    searchControls.setReturningAttributes(new String[] { "*", "+" });
    return ldapTemplate
        .search(buildDn(), new EqualsFilter("objectClass", "pwdPolicy").encode(), new PwdAttributeMapper())
        .get(0);
  }

}
