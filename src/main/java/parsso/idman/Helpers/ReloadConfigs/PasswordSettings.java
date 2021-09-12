package parsso.idman.Helpers.ReloadConfigs;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import parsso.idman.Models.Logs.Setting;

import javax.naming.Name;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.util.List;

@Getter
@Service
public class PasswordSettings {
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

	private Name buidDn() {
		return LdapNameBuilder.newInstance("ads-pwdId=default,ou=passwordPolicies,ads-interceptorId=authenticationInterceptor,ou=interceptors,ads-directoryServiceId=default,ou=config").build();
	}

	public void update(List<Setting> settings) {

		ModificationItem[] items = new ModificationItem[9];
		Attribute[] attrs = new Attribute[9];

		for (Setting setting : settings) {
			if (setting.getName().equals("pwd.check.quality")) {
				attrs[0] = new BasicAttribute("ads-pwdcheckquality", setting.getValue());
				items[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[0]);
				continue;

			} else if (setting.getName().equals("pwd.expire.warning")) {

				attrs[1] = new BasicAttribute("ads-pwdexpirewarning", setting.getValue());
				items[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[1]);
				continue;
			} else if (setting.getName().equals("pwd.failure.count.interval")) {

				attrs[2] = new BasicAttribute("ads-pwdfailurecountinterval", setting.getValue());
				items[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[2]);
				continue;
			} else if ((setting.getName().equals("pwd.grace.auth.n.limit"))) {

				attrs[3] = new BasicAttribute("ads-pwdgraceauthnlimit", setting.getValue());
				items[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[3]);
				continue;
			} else if ((setting.getName().equals("pwd.in.history"))) {
				attrs[4] = new BasicAttribute("ads-pwdinhistory", setting.getValue());
				items[4] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[4]);
				continue;
			} else if ((setting.getName().equals("pwd.lockout"))) {
				attrs[5] = new BasicAttribute("ads-pwdlockout", setting.getValue());
				items[5] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[5]);
				continue;
			} else if ((setting.getName().equals("pwd.lockout.duration"))) {
				attrs[6] = new BasicAttribute("ads-pwdlockoutduration", setting.getValue());
				items[6] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[6]);
				continue;
			} else if ((setting.getName().equals("pwd.max.failure"))) {
				attrs[7] = new BasicAttribute("ads-pwdmaxfailure", setting.getValue());
				items[7] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[7]);
				continue;
			} else if ((setting.getName().equals("pwd.min.lentgh"))) {
				attrs[8] = new BasicAttribute("ads-pwdminlength", setting.getValue());
				items[8] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attrs[8]);
				continue;
			}

		}

		try {
			ldapTemplate.modifyAttributes(buidDn(), items);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
