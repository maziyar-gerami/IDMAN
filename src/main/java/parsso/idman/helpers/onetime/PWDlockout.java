package parsso.idman.helpers.onetime;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;

import parsso.idman.configs.Prefs;
import parsso.idman.helpers.UniformLogger;
import parsso.idman.helpers.Variables;
import parsso.idman.helpers.user.BuildDnUser;
import parsso.idman.helpers.user.SimpleUserAttributeMapper;
import parsso.idman.impls.users.oprations.retrieve.sub.FullAttributes;
import parsso.idman.models.logs.ReportMessage;
import parsso.idman.models.other.OneTime;
import parsso.idman.models.users.User.UserLoggedIn;
import parsso.idman.models.users.User;

import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import java.util.Date;
import java.util.List;

public class PWDlockout {
  final MongoTemplate mongoTemplate;
  final LdapTemplate ldapTemplate;
  final UniformLogger uniformLogger;

  public PWDlockout(LdapTemplate ldapTemplate,
      MongoTemplate mongoTemplate, UniformLogger uniformLogger) {
    this.ldapTemplate = ldapTemplate;
    this.mongoTemplate = mongoTemplate;
    this.uniformLogger = uniformLogger;
  }

  public void run() {
    SearchControls searchControls = new SearchControls();
    searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
    String[] array = { "uid", "pwdLockout" };
    searchControls.setReturningAttributes(array);

    OneTime oneTime;
    Query query = new Query(Criteria.where("_id").is(Variables.PWD_LOCKOUT));
    try {
      oneTime = mongoTemplate.findOne(query, OneTime.class, Variables.col_OneTime);
    } catch (NullPointerException e) {
      oneTime = new OneTime(Variables.PWD_LOCKOUT, false, 0L);
    }

    if (oneTime == null)
      oneTime = new OneTime(Variables.PWD_LOCKOUT);
    if (oneTime.isRun())
      return;

    EqualsFilter equalsFilter = new EqualsFilter("objectclass", "person");

    List<UserLoggedIn> usersLoggedIn = ldapTemplate.search("ou=People," + Prefs.get(Variables.PREFS_BASE_DN), equalsFilter.encode(),
        searchControls, new SimpleUserAttributeMapper.LoggedInUserAttributeMapper());

    int c = 0;
    char[] animationChars = new char[] { '|', '/', '-', '\\' };
      for (User user : new FullAttributes(ldapTemplate, mongoTemplate).get()) {
        ModificationItem[] modificationItems = new ModificationItem[1];

        modificationItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
              new BasicAttribute("pwdLockout", "TRUE"));
      

        try {
          ldapTemplate.modifyAttributes(new BuildDnUser(Prefs.get(Variables.PREFS_BASE_DN)).buildDn(user.get_id().toString()),
              modificationItems);
      } catch (Exception e) {
        uniformLogger.info("System", new ReportMessage(Variables.MODEL_USER, user.get_id().toString(),
            Variables.ATTR_LOGGEDIN, Variables.ACTION_SET, Variables.RESULT_FAILED, "Writing to DB"));
      }
      int i = (++c * 100 / usersLoggedIn.size());

      System.out.print("Processing: " + i + "% " + animationChars[i % 4] + "\r");
    }

    OneTime oneTime1 = new OneTime(Variables.PWD_LOCKOUT, true, new Date().getTime());
    mongoTemplate.save(oneTime1, Variables.col_OneTime);

    System.out.println("Processing: Done!");
  }
}
