package parsso.idman.Models.Users;

import lombok.Getter;
import lombok.Setter;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

@Setter
@Getter
public class Pwd {

    String pwdmaxage;
    String pwdsafemodify;
    String pwdfailurecountinterval;
    String pwdminlength;
    String pwdexpirewarning;
    String pwdmustchange;
    String pwdallowuserchange;
    String pwdlockout;
    String pwdlockoutduration;
    String pwdcheckquality;
    String pwdgraceauthnlimit;
    String pwdinhistory;
    String pwdmaxfailure;

    @Service
    public static class PwdAttributeMapper implements AttributesMapper<Pwd> {

        Pwd pwd = new Pwd();

        @Override
        public Pwd mapFromAttributes(Attributes attributes) throws NamingException {
            pwd.pwdmaxage = attributes.get("pwdmaxage").get().toString();
            pwd.pwdsafemodify = attributes.get("pwdSafeModify").toString();
            pwd.pwdfailurecountinterval = attributes.get("pwdFailureCountInterval").toString();
            pwd.pwdminlength = attributes.get("pwdMinLength").toString();
            pwd.pwdexpirewarning = attributes.get("pwdExpireWarning").toString();
            pwd.pwdmustchange = attributes.get("pwdMustChange").toString();
            pwd.pwdallowuserchange = attributes.get("pwdAllowUserChange").toString();
            pwd.pwdlockout = attributes.get("pwdLockout").toString();


                pwd.pwdlockoutduration = attributes.get("pwdLockoutDuration").toString();

                pwd.pwdgraceauthnlimit = attributes.get("pwdGraceAuthNlimit").toString();


                pwd.pwdlockoutduration = attributes.get("pwdLockoutDuration").toString();


                pwd.pwdinhistory = attributes.get("pwdInHistory").toString();


                pwd.pwdmaxfailure = attributes.get("pwdMaxFailure").toString();


            return pwd;
        }
    }


}
