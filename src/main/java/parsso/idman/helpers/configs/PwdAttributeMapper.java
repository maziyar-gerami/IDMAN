package parsso.idman.helpers.configs;

import org.springframework.ldap.core.AttributesMapper;
import parsso.idman.models.other.PWD;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

public class PwdAttributeMapper implements AttributesMapper<PWD> {
  @Override
  public PWD mapFromAttributes(Attributes attributes) throws NamingException {
    PWD pwd = new PWD();

    if (attributes == null || attributes.get("pwdCheckQuality") == null)
      return null;

    pwd.setPwdFailureCountInterval(null != attributes.get("pwdFailureCountInterval")
        ? attributes.get("pwdFailureCountInterval").get().toString()
        : null);
    pwd.setPwdInHistory(
        null != attributes.get("pwdInHistory") ? attributes.get("pwdInHistory").get().toString() : null);
    pwd.setPwdLockout(null != attributes.get("pwdLockout") ? attributes.get("pwdLockout").get().toString() : null);
    pwd.setPwdLockoutDuration(
        null != attributes.get("pwdLockoutDuration") ? attributes.get("pwdLockoutDuration").get().toString()
            : null);
    pwd.setPwdMaxAge(null != attributes.get("pwdMaxAge") ? attributes.get("pwdMaxAge").get().toString() : null);
    pwd.setPwdMaxFailure(
        null != attributes.get("pwdMaxFailure") ? attributes.get("pwdMaxFailure").get().toString() : null);
    return pwd;
  }

}