package parsso.idman.models.other;

import lombok.Getter;
import lombok.Setter;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

@Setter
@Getter
@Entry(objectClasses = {})
public class PWD {
  @Id
  String pwdFailureCountInterval;
  String pwdGraceAuthNLimit;
  String pwdInHistory;
  String pwdLockout;
  String pwdLockoutDuration;
  String pwdMaxAge;
  String pwdMaxFailure;
}
