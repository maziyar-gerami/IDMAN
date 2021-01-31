package parsso.idman.Helpers.Config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

@Service
public class PasswordRegulation {
    @Autowired
    LdapTemplate ldapTemplate;

    @Value("${pwd.min.lentgh}")
    String pwd_min_lentgh;

    @Value("${pwd.max.failure}")
    String pwd_max_failure;

    @Value("${pwd.lockout.duration}")
    String pwd_lockout_duration;

    @Value("${pwd.lockout}")
    String pwd_lockout;

    @Value("${pwd.in.history}")
    String pwd_in_history;

    @Value("${pwd.grace.auth.n.limit}")
    String pwd_grace_auth_n_limit;

    @Value("${pwd.expire.warning}")
    String pwd_expire_warning;

    @Value("${pwd.failure.count.interval}")
    String pwd_failure_count_interval;

    @Value("${pwd.check.quality}")
    String pwd_check_quality;



    private Name buidDn(){
        return LdapNameBuilder.newInstance("ads-pwdId=default,ou=passwordPolicies,ads-interceptorId=authenticationInterceptor,ou=interceptors,ads-directoryServiceId=default,ou=config").build();
    }

    public void update(){

        DirContextOperations context = ldapTemplate.lookupContext(buidDn());

        Attribute attr1 = new BasicAttribute("ads-pwdcheckquality", pwd_check_quality);
        ModificationItem item1 = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr1);

        Attribute attr2 = new BasicAttribute("ads-pwdexpirewarning", pwd_expire_warning);
        ModificationItem item2 = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr2);

        Attribute attr3 = new BasicAttribute("ads-pwdfailurecountinterval", pwd_failure_count_interval);
        ModificationItem item3 = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr3);

        Attribute attr4 = new BasicAttribute("ads-pwdgraceauthnlimit", pwd_grace_auth_n_limit);
        ModificationItem item4 = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr4);

        Attribute attr5 = new BasicAttribute("ads-pwdinhistory", pwd_in_history);
        ModificationItem item5 = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr5);

        Attribute attr6 = new BasicAttribute("ads-pwdlockout", pwd_lockout);
        ModificationItem item6 = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr6);

        Attribute attr7 = new BasicAttribute("ads-pwdlockoutduration", pwd_lockout_duration);
        ModificationItem item7 = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr7);

        Attribute attr8 = new BasicAttribute("ads-pwdmaxfailure", pwd_max_failure);
        ModificationItem item8 = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr8);

        Attribute attr9 = new BasicAttribute("ads-pwdminlength", pwd_min_lentgh);
        ModificationItem item9 = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr9);


        ldapTemplate.modifyAttributes(buidDn(), new ModificationItem[] {item1,item2,item3,item4,item5,item6,item7,item8,item9});

        try {
            ldapTemplate.modifyAttributes(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
